package pl.czechak.leszek.photogalerybackend.model.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] bytes;
    private String contentType;


    public FileEntity(byte[] bytes, String contentType) {
        this.bytes = bytes;
        this.contentType = contentType;
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public byte[] getBytes() {
        return bytes;
    }

    public String getContentType() {
        return contentType;
    }
}
