package rotteneggs.fedexday.player;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PlayerService {

  Player findOneByEmail(String email);
  void createPlayer(String firstName, String lastName, String email, String password, boolean activated);
  Player getCurrentUser(HttpServletRequest request);
  List<String> validateSignUp(Player player, String confirm);
  String validateLogin(String email, String password);
  Player signUp(Player player);
  void changeCountForPlayer(String email, long count);
  List<Player> getTop5();
}
