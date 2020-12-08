package pl.czechak.leszek.photogalerybackend.service;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.czechak.leszek.photogalerybackend.dto.AddGalleryRequest;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserRole;
import pl.czechak.leszek.photogalerybackend.repository.FileRepository;
import pl.czechak.leszek.photogalerybackend.repository.GalleryRepository;
import pl.czechak.leszek.photogalerybackend.repository.UserRepository;
import pl.czechak.leszek.photogalerybackend.util.UserContext;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class GalleryService {

    private final UserRepository userRepository;
    private final GalleryRepository galleryRepository;
    private final FileRepository fileRepository;
    private final UserContext userContext;

    public GalleryService(UserRepository userRepository, GalleryRepository galleryRepository, FileRepository fileRepository, UserContext userContext) {
        this.userRepository = userRepository;
        this.galleryRepository = galleryRepository;
        this.fileRepository = fileRepository;
        this.userContext = userContext;
    }

    @Transactional
    public GalleryEntity createGallery(AddGalleryRequest galleryRequest) {

        UserEntity userEntity = userRepository.findById(galleryRequest.getUserId())
                .orElseThrow(()-> new UsernameNotFoundException("can't find user"));

        GalleryEntity newGallery = new GalleryEntity();
        newGallery.setGalleryName(galleryRequest.getGalleryName());
        newGallery.setFiles(List.of());
        newGallery.setUser(userEntity);

        return galleryRepository.save(newGallery);

    }

    @Transactional
    public GalleryEntity getGalleryById(long galleryId) {

        GalleryEntity galleryEntity = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new DataAccessResourceFailureException("Can't find gallery"));
        return galleryEntity;
    }


    //    private FileEntity getFileEntity(MultipartFile multipartFile) {
//        byte[] bytes = new byte[0];
//
//        try {
//            bytes = multipartFile.getBytes();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String contentType = multipartFile.getContentType();
//        FileEntity file = new FileEntity(bytes, contentType);
//        return file;
//    }
}
