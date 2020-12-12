package pl.czechak.leszek.photogalerybackend.controller.restController;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.czechak.leszek.photogalerybackend.model.file.FileEntity;
import pl.czechak.leszek.photogalerybackend.service.FileService;


@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/{galleryId}")
    public ResponseEntity<Void> addFileToGallery(@PathVariable long galleryId, MultipartFile multipartFile) {
        fileService.addFileToGallery(galleryId, multipartFile);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFile(@PathVariable("fileId") long id) {
        fileService.deleteFile(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getById(@PathVariable long id) {

        FileEntity file = fileService.getFileEntity(id);
        MediaType contentType = MediaType.parseMediaType(file.getContentType());
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(file.getBytes());
    }


}
