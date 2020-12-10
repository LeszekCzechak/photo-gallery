package pl.czechak.leszek.photogalerybackend.controller.restController;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import pl.czechak.leszek.photogalerybackend.service.FileService;
import pl.czechak.leszek.photogalerybackend.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;
    @MockBean
    private FileController fileController;
    @MockBean
    private UserService userService;

    @Test
    void shouldPassMultipartFileToFIleService() throws Exception {

        MockMultipartFile multipartFile
                = new MockMultipartFile(
                "multipartFile",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(post("/files/1")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(String.valueOf(multipartFile)))
                .andDo((print()))
                .andExpect(status().isOk());

        ArgumentCaptor<MultipartFile> captor = ArgumentCaptor.forClass(MultipartFile.class);
        verify(fileService).addFileToGallery(1, captor.capture());

        assertEquals("multipartFile",captor.capture().getName());
        assertEquals("hello.txt",captor.capture().getOriginalFilename());

    }

    @Test
    void shouldDeleteFile() throws Exception {

        mockMvc.perform(delete("/1"))
                .andExpect(status().isOk());
    }
}
