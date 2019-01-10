package rotteneggs.fedexday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rotteneggs.fedexday.player.Player;
import rotteneggs.fedexday.player.PlayerService;

import java.util.List;

@Controller
public class LoginRegistrationController {
  private PlayerService playerService;

  @Autowired
  public LoginRegistrationController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GetMapping("/registration")
  public String renderRegistrationPage(@ModelAttribute Player user, Model model) {
    model.addAttribute("user", new Player());
    return "registration";
  }

  @PostMapping("/registration")
  public String signUp(@ModelAttribute Player player, @RequestParam(name = "confirm") String confirm, Model model) {
    List<String> errorMessage = playerService.validateSignUp(player, confirm);
    model.addAttribute("errors", errorMessage);
    if (errorMessage != null && !errorMessage.isEmpty()) {
      System.out.println("loginError");
      return "registration";
    }
    playerService.signUp(player);
    System.out.println(player.getFirstName() + " " + player.getLastName() + " is registered to Player");
    return "redirect:/login";
  }

  @GetMapping("/login")
  public String renderLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
    model.addAttribute("error", error);
    return "login";
  }

  @PostMapping("/login")
  public String login(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password, Model model) {
    String errorMessage = playerService.validateLogin(email, password);
    model.addAttribute("error", errorMessage);
    if (errorMessage != null) {
      System.out.println("failed login with email: " + email);
      return "login";
    }
    System.out.println("successful login with email: " + email);
    System.out.println(playerService.findOneByEmail(email).getRole());
    return "redirect:/game";
  }

  @GetMapping("/game")
  public String mainPage() {
    return "mainpage";
  }

  @GetMapping("/")
  public String getMainPage() {
    return "redirect:/game";
  }
}
