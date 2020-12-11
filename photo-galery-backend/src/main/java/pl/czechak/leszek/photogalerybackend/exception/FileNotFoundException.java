package pl.czechak.leszek.photogalerybackend.exception;

public class FileNotFoundException extends RuntimeException{

    public FileNotFoundException (String message){
        super(message);
    }
}
