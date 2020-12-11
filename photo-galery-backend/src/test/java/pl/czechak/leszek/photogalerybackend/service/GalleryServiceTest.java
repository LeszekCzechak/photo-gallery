package pl.czechak.leszek.photogalerybackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.czechak.leszek.photogalerybackend.dto.AddGalleryRequest;
import pl.czechak.leszek.photogalerybackend.model.file.FileEntity;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserRole;
import pl.czechak.leszek.photogalerybackend.repository.FileRepository;
import pl.czechak.leszek.photogalerybackend.repository.GalleryRepository;
import pl.czechak.leszek.photogalerybackend.repository.UserRepository;
import pl.czechak.leszek.photogalerybackend.util.UserContext;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GalleryServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private GalleryRepository galleryRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserContext userContext;

    private GalleryService galleryService;

    @BeforeEach
    void setUp() {
        galleryService = new GalleryService(userRepository, galleryRepository, fileRepository, userContext);
    }


    @Test
    void shouldCreateGallery() {
        //given
        String requestGalleryName = "galleryTestingName";
        long userId = 99L;
        AddGalleryRequest galleryRequest = new AddGalleryRequest(requestGalleryName, userId);
        String username = "UserTest";
        UserEntity userEntity = new UserEntity(userId, username, "password", Set.of(UserRole.USER, UserRole.ADMIN), null);
        Optional<UserEntity> optionalUserEntity = Optional.of(userEntity);

        when(userRepository.findById(userId)).thenReturn(optionalUserEntity);

        //when
        galleryService.createGallery(galleryRequest);
        //then

        ArgumentCaptor<GalleryEntity> argumentCaptor = ArgumentCaptor.forClass(GalleryEntity.class);
        Mockito.verify(galleryRepository).save(argumentCaptor.capture());
        GalleryEntity galleryEntity = argumentCaptor.getValue();

        assertThat(galleryEntity.getGalleryName()).isEqualTo(requestGalleryName);
        assertThat(galleryEntity.getUser().getId()).isEqualTo(userId);

    }

    @Test
    void shouldReturnGalleryById() {
        //given
        String username = "UserTest";
        String password = "password";
        UserEntity userEntity = new UserEntity(123L, username, password, Set.of(UserRole.USER, UserRole.ADMIN), null);

        long galleryId = 999L;
        String galleryName = "GalleryName";
        GalleryEntity galleryEntity = new GalleryEntity(galleryId, galleryName, userEntity, new ArrayList<FileEntity>());

        when(galleryRepository.findById(galleryId)).thenReturn(Optional.of(galleryEntity));
        when(userContext.getCurrentUser()).thenReturn(userEntity);

        //when
        GalleryEntity gallery = galleryService.getGalleryById(galleryId);
        //then
        assertThat(gallery.getGalleryName()).isEqualTo(galleryName);
        assertThat(gallery.getId()).isEqualTo(galleryId);
        assertThat(gallery.getUser().getUsername()).isEqualTo(username);

    }

    @Test
    void deleteGalleryById() {
        //given
        long galleryId = 23L;
        //when
        galleryService.deleteGalleryById(galleryId);
        //then
        ArgumentCaptor<Long> argumentCaptor= ArgumentCaptor.forClass(Long.class);
        Mockito.verify(galleryRepository).deleteById(argumentCaptor.capture());
        Long argumentCaptorValue = argumentCaptor.getValue();

        assertThat(argumentCaptorValue.longValue()).isEqualTo(galleryId);
    }
}
