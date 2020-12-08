package pl.czechak.leszek.photogalerybackend.util;

import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;

public interface UserContext {

    UserEntity getCurrentUser();
}
