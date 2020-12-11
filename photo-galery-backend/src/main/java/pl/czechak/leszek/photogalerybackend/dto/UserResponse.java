package pl.czechak.leszek.photogalerybackend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {

    private Long userId;
    private String username;
    private List<GalleryResponse> galleries;
}
