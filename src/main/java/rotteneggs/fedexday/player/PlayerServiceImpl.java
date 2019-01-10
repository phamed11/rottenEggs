package rotteneggs.fedexday.player;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rotteneggs.fedexday.player.exceptions.PlayerExceptions;

@Service
public class PlayerServiceImpl implements PlayerService {

  private PlayerRepository playerRepository;

  @Autowired
  public PlayerServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
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
}
