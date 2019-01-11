package rotteneggs.fedexday.player;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

 Player findPlayerByEmail(String email);
 List<Player> findAll();
}
