package pl.czechak.leszek.photogalerybackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.czechak.leszek.photogalerybackend.dto.LoggedUser;
import pl.czechak.leszek.photogalerybackend.service.UserService;

@Controller
public class SiteController {

    private final UserService userService;

    public SiteController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-user")
    public String login(Model model) {
        LoggedUser loggedUser = userService.checkLoginStatus();
        model.addAttribute("loggedUser", loggedUser);

        return "checkUser";
    }
}
