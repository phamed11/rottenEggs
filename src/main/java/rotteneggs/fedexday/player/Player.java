package rotteneggs.fedexday.player;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private boolean activated;
  private long countResult;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinTable(name = "player_roles",
      joinColumns = @JoinColumn(name = "player_id"),
      inverseJoinColumns = @JoinColumn(name = "player_role_id"))
  private EggRole role;


  public Player() {
  }

  public Player(String firstName, String lastName, String email, String password, boolean activated) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.activated = activated;
    this.countResult = 0;
  }


  public EggRole getRole() {
    return role;
  }

  public void setRole(EggRole role) {
    this.role = role;
  }
}
