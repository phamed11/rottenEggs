package rotteneggs.fedexday.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EggRoleServiceImpl implements EggRoleService {

  private EggRoleRepository eggRoleRepository;

  @Autowired
  public EggRoleServiceImpl(EggRoleRepository eggRoleRepository) {
    this.eggRoleRepository = eggRoleRepository;
  }

  @Override
  public EggRole findByName(String name) {
    return eggRoleRepository.findByName(RoleName.valueOf(name));
  }
}
