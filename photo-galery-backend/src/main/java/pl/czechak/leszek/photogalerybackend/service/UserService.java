package pl.czechak.leszek.photogalerybackend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.czechak.leszek.photogalerybackend.dto.CreateUserRequest;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserRole;
import pl.czechak.leszek.photogalerybackend.repository.GalleryRepository;
import pl.czechak.leszek.photogalerybackend.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final GalleryRepository galleryRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, GalleryRepository galleryRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.galleryRepository = galleryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find user: "+username));
    }


    public void createUser(CreateUserRequest userRequest) {

        UserEntity newUserEntity= new UserEntity();
        newUserEntity.setUsername(userRequest.getUsername());
        newUserEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.USER);
        if(userRequest.getAdmin()){
            roles.add(UserRole.ADMIN);
        }
        newUserEntity.setRoles(roles);
        newUserEntity.setGalleries(Set.of());

        userRepository.save(newUserEntity);
    }
}
