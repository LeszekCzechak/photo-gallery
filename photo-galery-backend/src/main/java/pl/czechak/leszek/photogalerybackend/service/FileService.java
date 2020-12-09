package pl.czechak.leszek.photogalerybackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.czechak.leszek.photogalerybackend.model.file.FileEntity;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;
import pl.czechak.leszek.photogalerybackend.repository.FileRepository;
import pl.czechak.leszek.photogalerybackend.repository.GalleryRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FileService {

    private final GalleryRepository galleryRepository;
    private final FileRepository fileRepository;

    public FileService(GalleryRepository galleryRepository, FileRepository fileRepository) {
        this.galleryRepository = galleryRepository;
        this.fileRepository = fileRepository;
    }


    @Transactional
    public void addFileToGallery(long galleryId, MultipartFile multipartFile) {

        byte[] bytes = new byte[0];

        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contentType = multipartFile.getContentType();

        GalleryEntity galleryEntity = galleryRepository.findById(galleryId).orElseThrow(
                () -> new NoSuchElementException()
        );

        FileEntity fileEntity = new FileEntity(bytes, contentType, galleryEntity);
        List<FileEntity> files = galleryEntity.getFiles();
        files.add(fileEntity);
        galleryEntity.setFiles(files);
        fileRepository.save(fileEntity);
        galleryRepository.save(galleryEntity);

    }

    public void deleteFile(long id) {

        FileEntity fileEntity = fileRepository.findById(id).orElseThrow(
                NoSuchElementException::new);

        Long galleryId = fileEntity.getGallery().getId();
        GalleryEntity galleryEntity = galleryRepository.getOne(galleryId);
        List<FileEntity> files = galleryEntity.getFiles();
        files.remove(fileEntity);
        galleryEntity.setFiles(files);
        galleryRepository.save(galleryEntity);
        fileRepository.delete(fileEntity);

    }
}
