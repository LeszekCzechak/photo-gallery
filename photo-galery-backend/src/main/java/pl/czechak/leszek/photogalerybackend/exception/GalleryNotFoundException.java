package pl.czechak.leszek.photogalerybackend.exception;

public class GalleryNotFoundException extends RuntimeException {

    public GalleryNotFoundException(String message) {
        super(message);
    }
}
