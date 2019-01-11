package rotteneggs.fedexday.player;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EggRoleRepository extends CrudRepository<EggRole, Long> {

  List<EggRole> findAll();
  EggRole findByName(RoleName name);
}
