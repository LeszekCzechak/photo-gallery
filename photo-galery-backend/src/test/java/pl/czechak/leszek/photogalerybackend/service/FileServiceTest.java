package pl.czechak.leszek.photogalerybackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import pl.czechak.leszek.photogalerybackend.model.file.FileEntity;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;
import pl.czechak.leszek.photogalerybackend.model.user.UserRole;
import pl.czechak.leszek.photogalerybackend.repository.FileRepository;
import pl.czechak.leszek.photogalerybackend.repository.GalleryRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private GalleryRepository galleryRepository;
    @Mock
    private FileRepository fileRepository;

    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new FileService(galleryRepository, fileRepository);
    }

    @Test
    void shouldAddFileToGallery() {
        //given
        long galleryId = 22L;
        long userId = 999L;
        String username = "UserTest";
        String password = "password";
        UserEntity userEntity = new UserEntity(userId, username, password, Set.of(UserRole.USER, UserRole.ADMIN), null);

        String galleryName = "GalleryName";
        GalleryEntity galleryEntity = new GalleryEntity(galleryId, galleryName, userEntity, new ArrayList<>());

        MockMultipartFile multipartFile = new MockMultipartFile(
                "multipartFile",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());

        when(galleryRepository.findById(galleryId)).thenReturn(Optional.of(galleryEntity));

        //when
        fileService.addFileToGallery(galleryId, multipartFile);
        //then

        ArgumentCaptor<GalleryEntity> galleryEntityArgumentCaptor = ArgumentCaptor.forClass(GalleryEntity.class);
        Mockito.verify(galleryRepository).save(galleryEntityArgumentCaptor.capture());
        GalleryEntity galleryEntityArgumentCaptorValue = galleryEntityArgumentCaptor.getValue();

        assertThat(galleryEntityArgumentCaptorValue.getGalleryName()).isEqualTo(galleryName);
        assertThat(galleryEntityArgumentCaptorValue.getFiles().size()).isEqualTo(1);
        assertThat(galleryEntityArgumentCaptorValue.getFiles().get(0).getContentType()).isEqualTo("text/plain");

    }

    @Test
    void shouldDeleteFile() {
        //given
        long fileId = 999L;
        byte[] bytes = new byte[]{};
        String contentType = "text/plain";

        long galleryId = 22L;
        String galleryName = "GalleryName";
        GalleryEntity galleryEntity = new GalleryEntity(galleryId, galleryName, null, new ArrayList<FileEntity>());
        FileEntity fileEntity = new FileEntity(fileId, bytes,contentType, galleryEntity);

        when(fileRepository.findById(fileId)).thenReturn(Optional.of(fileEntity));
        when(galleryRepository.getOne(galleryId)).thenReturn(galleryEntity);

        //when
        fileService.deleteFile(fileId);
        //then
        ArgumentCaptor<GalleryEntity> galleryEntityArgumentCaptor= ArgumentCaptor.forClass(GalleryEntity.class);
        Mockito.verify(galleryRepository).save(galleryEntityArgumentCaptor.capture());
        GalleryEntity galleryEntityArgumentCaptorValue = galleryEntityArgumentCaptor.getValue();

        assertThat(galleryEntityArgumentCaptorValue.getGalleryName()).isEqualTo(galleryName);
        assertThat(galleryEntityArgumentCaptorValue.getId()).isEqualTo(galleryId);

    }
}
