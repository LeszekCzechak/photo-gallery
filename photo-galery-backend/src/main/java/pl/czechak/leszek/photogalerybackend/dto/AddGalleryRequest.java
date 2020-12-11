package pl.czechak.leszek.photogalerybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddGalleryRequest {

    private String galleryName;
    private Long userId;

}
