package pl.czechak.leszek.photogalerybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserComplexInfo {
    private Long userId;
    private String username;
    private Long galleryId;
    private String galleryName;
    private Long gallerySize;
}
