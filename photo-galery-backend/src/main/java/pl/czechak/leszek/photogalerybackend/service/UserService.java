package pl.czechak.leszek.photogalerybackend.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.czechak.leszek.photogalerybackend.dto.*;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserRole;
import pl.czechak.leszek.photogalerybackend.repository.GalleryRepository;
import pl.czechak.leszek.photogalerybackend.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final GalleryRepository galleryRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(GalleryRepository galleryRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.galleryRepository = galleryRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user: " + username));
    }


    @Transactional
    public void createUser(CreateUserRequest userRequest) {

        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setUsername(userRequest.getUsername());
        newUserEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.USER);
        if (userRequest.getAdmin()) {
            roles.add(UserRole.ADMIN);
        }
        newUserEntity.setRoles(roles);
        newUserEntity.setGalleries(new ArrayList<>());

        userRepository.save(newUserEntity);
    }

    @Transactional
    public void deleteUser(long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(userEntity);
    }

    @Transactional
    public List<UserResponse> getAllUsers() {

        List<UserComplexInfo> allUserComplexInfo = convertToUserComplexInfo(userRepository.getAllUserComplexInfo());
        List<UserResponse> response = new ArrayList<>();
        fillBasicallyUserResponseList(allUserComplexInfo, response);
        addGalleryInfoToUserList(allUserComplexInfo, response);
        return response;
    }

    private void fillBasicallyUserResponseList(List<UserComplexInfo> allUserComplexInfo, List<UserResponse> response) {
        allUserComplexInfo.forEach(complexInfo -> {
            Optional<UserResponse> any = response.stream().filter(user -> user.getUserId().equals(complexInfo.getUserId())).findAny();
            if(any.isEmpty()){
                response.add(new UserResponse(complexInfo.getUserId(), complexInfo.getUsername(), new ArrayList<>()));
            }
        });
    }

    private void addGalleryInfoToUserList(List<UserComplexInfo> allUserComplexInfo, List<UserResponse> response) {
        allUserComplexInfo.forEach(complexInfo -> {
            if (Objects.nonNull(complexInfo.getGalleryId())) {
                response.stream()
                        .filter(res -> res.getUserId().equals(complexInfo.getUserId()))
                        .findFirst().get()
                        .getGalleries()
                        .add(new GalleryResponse(complexInfo.getGalleryId(), complexInfo.getGalleryName(), complexInfo.getGallerySize().intValue()));
            }
        });
    }

    private List<UserComplexInfo> convertToUserComplexInfo(List<Object[]> allUserComplexInfo) {
        return allUserComplexInfo.stream()
                .map(rawData -> new UserComplexInfo(
                        (Long) rawData[0],
                        (String) rawData[1],
                        (Long) rawData[2],
                        (String) rawData[3],
                        (Long) rawData[4]))
                .collect(Collectors.toList());
    }

//    private UserResponse UserEntityToUserResponseMapper(UserEntity userEntity) {
//        List<GalleryResponse> galleryResponses = getGalleryResponses(userEntity);
//        UserResponse userResponse = new UserResponse();
//        userResponse.setUserId(userEntity.getId());
//        userResponse.setUsername(userEntity.getUsername());
//        userResponse.setGalleries(galleryResponses);
//
//        return userResponse;
//    }

    private List<GalleryResponse> getGalleryResponses(UserEntity userEntity) {
        return userEntity.getGalleries().stream()
                .map(galleryEntity -> {
                    GalleryResponse galleryResponse = new GalleryResponse(galleryEntity.getId(), galleryEntity.getGalleryName(), galleryEntity.getFiles().size());
                    return galleryResponse;
                })
                .collect(Collectors.toList());
    }

    public LoggedUser checkLoginStatus() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        return new LoggedUser(username);
    }
}
