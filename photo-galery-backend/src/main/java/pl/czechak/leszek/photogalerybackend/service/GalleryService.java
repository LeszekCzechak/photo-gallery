package pl.czechak.leszek.photogalerybackend.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.czechak.leszek.photogalerybackend.dto.AddGalleryRequest;
import pl.czechak.leszek.photogalerybackend.exception.GalleryNotFoundException;
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
                .orElseThrow(() -> new GalleryNotFoundException("Can't find gallery"));

        UserEntity currentUser = userContext.getCurrentUser();

        Long currentUserId = currentUser.getId();
        Long galleryUserId = galleryEntity.getUser().getId();

        boolean isOwner = currentUserId.equals(galleryUserId);
        boolean isAdmin = currentUser.getRoles().contains(UserRole.ADMIN);

        if(isOwner || isAdmin){
            return galleryEntity;
        }

        throw new AccessDeniedException("Can't do that");

    }

    public void deleteGalleryById(long galleryId) {
        galleryRepository.deleteById(galleryId);
    }
}
