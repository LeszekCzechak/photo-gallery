package pl.czechak.leszek.photogalerybackend.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.czechak.leszek.photogalerybackend.dto.CreateUserRequest;
import pl.czechak.leszek.photogalerybackend.dto.GalleryResponse;
import pl.czechak.leszek.photogalerybackend.dto.LoggedUser;
import pl.czechak.leszek.photogalerybackend.dto.UserResponse;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserRole;
import pl.czechak.leszek.photogalerybackend.repository.GalleryRepository;
import pl.czechak.leszek.photogalerybackend.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        return userRepository.findAll().stream()
                .map(userEntity -> UserEntityToUserResponseMapper(userEntity))
                .collect(Collectors.toList());
    }

    private UserResponse UserEntityToUserResponseMapper(UserEntity userEntity) {
        List<GalleryResponse> galleryResponses = getGalleryResponses(userEntity);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(userEntity.getId());
        userResponse.setUsername(userEntity.getUsername());
        userResponse.setGalleries(galleryResponses);

        return userResponse;
    }

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
