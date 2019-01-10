package rotteneggs.fedexday.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserBuilder {
  private String username;
  private String password;
  private boolean enabled;
  private boolean accountNonExpired;
  private boolean credentialsNonExpired;
  private boolean accountNonLocked;
  private Collection<? extends GrantedAuthority> authorities;

  public UserBuilder() {
  }

  public UserBuilder username(String username) {
    this.username=username;
    return this;
  }

  public UserBuilder password(String password) {
    this.password=password;
    return this;
  }

  public UserBuilder enabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public UserBuilder accountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
    return this;
  }

  public UserBuilder credentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
    return this;
  }

  public UserBuilder accountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
    return this;
  }

  public UserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
    this.authorities = authorities;
    return this;
  }

  public User build() {
    return new User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
  }
}

