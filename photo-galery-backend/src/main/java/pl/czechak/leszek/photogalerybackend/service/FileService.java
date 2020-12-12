package pl.czechak.leszek.photogalerybackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.czechak.leszek.photogalerybackend.exception.FileNotFoundException;
import pl.czechak.leszek.photogalerybackend.exception.InvalidFileContentException;
import pl.czechak.leszek.photogalerybackend.exception.GalleryNotFoundException;
import pl.czechak.leszek.photogalerybackend.model.file.FileEntity;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;
import pl.czechak.leszek.photogalerybackend.repository.FileRepository;
import pl.czechak.leszek.photogalerybackend.repository.GalleryRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

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

        byte[] bytes;

        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            throw new InvalidFileContentException();
        }
        String contentType = multipartFile.getContentType();

        GalleryEntity galleryEntity = galleryRepository.findById(galleryId).orElseThrow(
                () -> new GalleryNotFoundException("Can't find gallery")
        );

        FileEntity fileEntity = new FileEntity(bytes, contentType,galleryEntity);
        galleryEntity.getFiles().add(fileEntity);
        galleryRepository.save(galleryEntity);

    }

    @Transactional
    public void deleteFile(long id) {

        FileEntity fileEntity = fileRepository.findById(id).orElseThrow(
                ()-> new FileNotFoundException("Can't find file"));

        Long galleryId = fileEntity.getGallery().getId();
        GalleryEntity galleryEntity = galleryRepository.getOne(galleryId);
        List<FileEntity> files = galleryEntity.getFiles();
        files.remove(fileEntity);
        fileRepository.delete(fileEntity);

    }

    public FileEntity getFileEntity(long id) {
        FileEntity file = fileRepository.findById(id)
                .orElseThrow();
        return file;
    }}
