package pl.czechak.leszek.photogalerybackend.exception;

public abstract class PhotoGalleryException extends RuntimeException {
    public PhotoGalleryException(String message) {
        super(message);
    }
}
