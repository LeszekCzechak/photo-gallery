package pl.czechak.leszek.photogalerybackend.controller.restController;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.czechak.leszek.photogalerybackend.configuration.security.EncryptorConfig;
import pl.czechak.leszek.photogalerybackend.configuration.security.SecurityConfig;
import pl.czechak.leszek.photogalerybackend.dto.AddGalleryRequest;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;
import pl.czechak.leszek.photogalerybackend.service.GalleryService;
import pl.czechak.leszek.photogalerybackend.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = GalleryController.class, excludeAutoConfiguration = {SecurityConfig.class, EncryptorConfig.class})
class GalleryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GalleryService galleryService;

    @MockBean
    private UserService userService;


    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldPassGalleryRequestToCreateGallery() throws Exception {
        String galleryRequest = "{\n" +
                "    \"galleryName\": \"TestName\",\n" +
                "    \"userId\": 72\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("/gallery/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(galleryRequest))
                .andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<AddGalleryRequest> argumentCaptor = ArgumentCaptor.forClass(AddGalleryRequest.class);
        Mockito.verify(galleryService).createGallery(argumentCaptor.capture());
        AddGalleryRequest argumentCaptorValue = argumentCaptor.getValue();

        assertThat(argumentCaptorValue.getGalleryName()).isEqualTo("TestName");
        assertThat(argumentCaptorValue.getUserId()).isEqualTo(72);

    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldReturnGalleryEntityFromGetGalleryById() throws Exception {
        //given
        long galleryId = 99L;
        String galleryName = "NameOfGallery";
        GalleryEntity galleryEntity = new GalleryEntity(galleryId, galleryName, null, null);

        when(galleryService.getGalleryById(galleryId)).thenReturn(galleryEntity);

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/gallery/" + galleryId))
                .andExpect(status()
                        .isOk());

        ArgumentCaptor<Long> argumentCaptor= ArgumentCaptor.forClass(Long.class);
        Mockito.verify(galleryService).getGalleryById(argumentCaptor.capture());
        Long argumentCaptorValue= argumentCaptor.getValue();

        assertThat(argumentCaptorValue.longValue()).isEqualTo(galleryId);

    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldPassGalleryIdToDeleteGalleryById() throws Exception {

        long galleryId= 48L;

        mockMvc.perform((delete("/gallery/"+galleryId)))
                .andExpect(status().isOk());
        Mockito.verify(galleryService).deleteGalleryById(galleryId);
    }
}
