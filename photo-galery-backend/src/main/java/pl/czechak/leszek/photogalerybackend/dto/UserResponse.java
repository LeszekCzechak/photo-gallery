package pl.czechak.leszek.photogalerybackend.dto;


import lombok.Data;

import java.util.List;


@Data
public class UserResponse {

    private Long userId;
    private String username;
    private List<GalleryResponse> galleries;
}
