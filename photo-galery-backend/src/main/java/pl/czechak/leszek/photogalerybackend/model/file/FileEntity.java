package pl.czechak.leszek.photogalerybackend.model.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;

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

    @ManyToOne
    @JsonIgnore
    private GalleryEntity gallery;

    public FileEntity(byte[] bytes, String contentType, GalleryEntity gallery) {
        this.bytes = bytes;
        this.contentType = contentType;
        this.gallery = gallery;
    }
}
