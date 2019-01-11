package rotteneggs.fedexday.player;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import rotteneggs.fedexday.player.exceptions.PlayerExceptions;
import rotteneggs.fedexday.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

  private PlayerRepository playerRepository;
  private JwtProvider jwtProvider;
  private EggRoleService eggRoleService;

  @Autowired
  public PlayerServiceImpl(PlayerRepository playerRepository, JwtProvider jwtProvider, EggRoleService eggRoleService) {
    this.playerRepository = playerRepository;
    this.jwtProvider = jwtProvider;
    this.eggRoleService = eggRoleService;
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
    Player player = new Player(firstName, lastName, email, password, true);
    playerRepository.save(player);
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
      player.setRole(eggRoleService.findByName("ROLE_PLAYER"));
    return playerRepository.save(player);
  }

  @Override
  public void changeCountForPlayer(String email, long count) {
    Player player = playerRepository.findPlayerByEmail(email);
    if (player.getCountResult() > count) {
      player.setCountResult(count);
      playerRepository.save(player);
    }
  }

  @Override
  public List<Player> getTop5() {
    List<Player> allPlayers = playerRepository.findAll();
    Collections.sort(allPlayers, new Sortbyroll());
    List<Player> top5Players = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      top5Players.add(allPlayers.get(i));
      allPlayers.get(i).setPassword("not telling you");
    }
    return top5Players;
  }

  private class Sortbyroll implements Comparator<Player> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(Player a, Player b)
    {
      return (int) (a.getCountResult() - b.getCountResult());
    }
  }




}
