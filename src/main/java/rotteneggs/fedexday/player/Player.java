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
  private String profilePicUrl;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinTable(name = "rotteneggs_user_roles",
      joinColumns = @JoinColumn(name = "rotteneggs_user_id"),
      inverseJoinColumns = @JoinColumn(name = "rotteneggs_user_role_id"))
  private EggRole role;


  public Player() {
  }

  public Player(String firstName, String lastName, String email, String password, boolean activated) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.activated = activated;
  }

  public Player(String firstName, String lastName, String email, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }
}
