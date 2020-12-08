package pl.czechak.leszek.photogalerybackend.dto;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String username;
    private String password;
    private Boolean admin;

}
