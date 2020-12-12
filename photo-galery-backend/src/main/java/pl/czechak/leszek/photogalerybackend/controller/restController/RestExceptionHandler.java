package pl.czechak.leszek.photogalerybackend.controller.restController;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.czechak.leszek.photogalerybackend.exception.FileNotFoundException;
import pl.czechak.leszek.photogalerybackend.exception.GalleryNotFoundException;
import pl.czechak.leszek.photogalerybackend.exception.InvalidFileContentException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({FileNotFoundException.class})
    public String handleFileNotFoundException(FileNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({GalleryNotFoundException.class})
    public String handleGalleryNotFoundException(GalleryNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler({InvalidFileContentException.class})
    public String handleInvalidFileContentException(InvalidFileContentException exception) {
        return exception.getMessage();
    }

}
