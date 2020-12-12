package pl.czechak.leszek.photogalerybackend.controller.restController;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import pl.czechak.leszek.photogalerybackend.configuration.security.EncryptorConfig;
import pl.czechak.leszek.photogalerybackend.configuration.security.SecurityConfig;
import pl.czechak.leszek.photogalerybackend.model.file.FileEntity;
import pl.czechak.leszek.photogalerybackend.service.FileService;
import pl.czechak.leszek.photogalerybackend.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = FileController.class, excludeAutoConfiguration = {SecurityConfig.class, EncryptorConfig.class})
class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FileService fileService;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldPassMultipartFileToFIleService() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "multipartFile",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        mockMvc.perform(MockMvcRequestBuilders.multipart("/files/1")
                .file(multipartFile))
                .andDo((print()))
                .andExpect(status().isNoContent());
        ArgumentCaptor<MultipartFile> captor = ArgumentCaptor.forClass(MultipartFile.class);
        verify(fileService).addFileToGallery(eq(1L), captor.capture());
        assertEquals("multipartFile", captor.getValue().getName());
        assertEquals("hello.txt", captor.getValue().getOriginalFilename());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldDeleteFile() throws Exception {
        mockMvc.perform(delete("/files/1"))
                .andExpect(status().isOk());
        verify(fileService).deleteFile(1);
    }


    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldReturnFileById() throws Exception {
        //given
        long fileId= 65L;
        byte[] bytes = new byte[]{123};
        String contentType = "text/plain";

        FileEntity fileEntity = new FileEntity(fileId, bytes,contentType, null);

        when(fileService.getFileEntity(fileId)).thenReturn(fileEntity);

        //when //then
        mockMvc.perform(get("/files/"+fileId))
                .andDo(print())
                .andExpect(status().isOk());


    }
}
