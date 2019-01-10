package rotteneggs.fedexday.player;

public interface PlayerService {

  public Player findOneByEmail(String email);
  public void createPlayer(String firstName, String lastName, String email, String password, boolean activated);

}
