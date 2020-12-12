package pl.czechak.leszek.photogalerybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryEntity,Long> {

   List<GalleryEntity> findAllByUser_Id(Long id);
   //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-property-expressions
}
