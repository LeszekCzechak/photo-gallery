package pl.czechak.leszek.photogalerybackend.dto;

import lombok.Data;

@Data
public class AddGalleryRequest {

    private String galleryName;
    private Long userId;

}
