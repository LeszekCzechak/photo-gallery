package pl.czechak.leszek.photogalerybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GalleryResponse {

    private Long galleryId;
    private String galleryName;
    private int gallerySize;
}
