package pl.czechak.leszek.photogalerybackend.exception;

public class InvalidFileContentException extends PhotoGalleryException {
    public InvalidFileContentException() {
        super("invalid file content");
    }
}
