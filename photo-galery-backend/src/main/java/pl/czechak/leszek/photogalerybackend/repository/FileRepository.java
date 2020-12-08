package pl.czechak.leszek.photogalerybackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.czechak.leszek.photogalerybackend.model.file.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
