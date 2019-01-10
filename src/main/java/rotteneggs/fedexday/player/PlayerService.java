package rotteneggs.fedexday.player;

import javax.servlet.http.HttpServletRequest;

public interface PlayerService {

  Player findOneByEmail(String email);
  void createPlayer(String firstName, String lastName, String email, String password, boolean activated);
  Player getCurrentUser(HttpServletRequest request);
}
