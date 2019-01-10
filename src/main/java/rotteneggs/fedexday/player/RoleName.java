package rotteneggs.fedexday.player;

public enum RoleName {
  ROLE_PLAYER("Player");

  private final String string;

  RoleName(String string) {
    this.string = string;
  }

  @Override
  public String toString() {
    return string;
  }
}