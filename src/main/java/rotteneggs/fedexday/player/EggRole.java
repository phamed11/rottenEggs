package rotteneggs.fedexday.player;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class EggRole {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private RoleName name;

  public EggRole() {
  }

  public EggRole(RoleName name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RoleName getName() {
    return name;
  }

  public void setName(RoleName name) {
    this.name = name;
  }
}
