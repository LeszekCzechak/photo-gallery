package pl.czechak.leszek.photogalerybackend.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoggedUser {

    private String username;

    public String getUsername() {
        return username;
    }
}
