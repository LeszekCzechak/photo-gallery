package pl.czechak.leszek.photogalerybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByUsername(String username);

    @Query("select distinct u.id as userId, u.username as username, g.id as galleryId, g.galleryName, count(f.id) as gallerySize " +
            "from UserEntity u " +
            "left join u.galleries g " +
            "left join g.files f " +
            "group by u.id, u.username, g.id, g.galleryName")
    List<Object[]> getAllUserComplexInfo();
}
