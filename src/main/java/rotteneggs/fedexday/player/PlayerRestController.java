package rotteneggs.fedexday.player;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerRestController {

  private PlayerService playerService;

  @Autowired
  public PlayerRestController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GetMapping("/api/game")
  public ResponseEntity<?> top5 () {
    return ResponseEntity.ok(playerService.getTop5());
  }

  @PostMapping("/api/game")
  public ResponseEntity<?> counter(@RequestParam(value = "email") String email, @RequestParam(value = "counter") long counter) {
    playerService.changeCountForPlayer(email, counter);
    return ResponseEntity.status(418).body("Successfully submitted");
  }
}
