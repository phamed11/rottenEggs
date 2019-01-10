package rotteneggs.fedexday.player;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import rotteneggs.fedexday.player.exceptions.PlayerExceptions;
import rotteneggs.fedexday.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

  private PlayerRepository playerRepository;
  private JwtProvider jwtProvider;

  @Autowired
  public PlayerServiceImpl(PlayerRepository playerRepository, JwtProvider jwtProvider) {
    this.playerRepository = playerRepository;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Player findOneByEmail(String email) {
    if (email == null || email.isEmpty()) {
      throw new PlayerExceptions("Email is null or empty");
    } else {
      return playerRepository.findPlayerByEmail(email);
    }
  }

  public void createPlayer(String firstName, String lastName, String email, String password, boolean activated) {
    playerRepository.save(new Player(firstName, lastName, email, password, true));
  }

  @Override
  public Player getCurrentUser(HttpServletRequest request) {
    String email = jwtProvider.getEmailFromRequestJwt(request);
    return findOneByEmail(email);
  }

  @Override
  public List<String> validateSignUp(Player player, String confirm) {
    List<String> errors = new ArrayList<>();
    if (confirm == null || isInputFieldEmpty(player, confirm)) {
      errors.add("Please fill all fields!");
    } else {
      if (!isValidEmail(player.getEmail())) {
        errors.add("The email address format you entered is invalid.");
      }
      if (!isValidPassword(player.getPassword())) {
        errors.add("Your password is too short, it should be at least 8 characters.");
      }
      if (!isConfirmationMatching(player.getPassword(), confirm)) {
        errors.add("The passwords you entered do not match.");
      }
      if (!isExistingEmail(player.getEmail())) {
        errors.add("This email address already exists.");
      }
    }
    return errors;
  }

  private boolean isValidEmail(String email) {
    return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\."
        + "[a-zA-Z0-9_+&*-]+)*@"
        + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
        + "A-Z]{2,7}$");
  }

  private boolean isInputFieldEmpty(Player player, String confirm) {
    return player.getFirstName().equals("")
        || player.getLastName().equals("")
        || player.getEmail().equals("")
        || player.getPassword().equals("")
        || confirm.equals("");
  }

  private boolean isExistingEmail(String email) {
    return findOneByEmail(email) == null;
  }

  private boolean isPasswordValid(String password, String hashedPassword) {
    System.out.println("checking password");
    return BCrypt.checkpw(password, hashedPassword);
  }

  private String passwordEncoder(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(12));
  }

  private boolean isValidPassword(String password) {
    return password.length() >= 8;
  }

  private boolean isConfirmationMatching(String password, String confirm) {
    return password.equals(confirm);
  }

  @Override
  public String validateLogin(String email, String password) {
    if (findOneByEmail(email) == null || !isPasswordValid(password, findOneByEmail(email).getPassword())) {
      return "Wrong email or password.";
    }
    return null;
  }

  @Override
  public Player signUp(Player player) {
    player.setPassword(passwordEncoder(player.getPassword()));
      player.setActivated(true);
    return playerRepository.save(player);
  }

}
