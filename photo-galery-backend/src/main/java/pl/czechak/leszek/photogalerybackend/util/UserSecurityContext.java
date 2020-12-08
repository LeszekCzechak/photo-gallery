package pl.czechak.leszek.photogalerybackend.util;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;

@Component
public class UserSecurityContext  implements UserContext{
    @Override
    public UserEntity getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (UserEntity) context.getAuthentication().getPrincipal();
    }
}
