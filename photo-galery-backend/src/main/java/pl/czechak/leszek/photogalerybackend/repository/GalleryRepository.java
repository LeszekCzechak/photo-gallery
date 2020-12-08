package pl.czechak.leszek.photogalerybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryEntity,Long> {
}
