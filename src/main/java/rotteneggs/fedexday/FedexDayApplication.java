package rotteneggs.fedexday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rotteneggs.fedexday.player.PlayerService;

@SpringBootApplication
public class FedexDayApplication implements CommandLineRunner {

  private PlayerService playerService;


  @Autowired
  public FedexDayApplication(PlayerService playerService) {
    this.playerService = playerService;
  }

  public static void main(String[] args) {
    SpringApplication.run(FedexDayApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

//    playerService.createPlayer("Peter", "Antal", "wrkpeter@gmail.com", "adminadmin", true);

  }
}

