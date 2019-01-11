package rotteneggs.fedexday.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rotteneggs.fedexday.player.Player;
import rotteneggs.fedexday.player.PlayerService;

import java.util.Arrays;
import java.util.List;

@Service
public class ClientDetailsService implements UserDetailsService {

  @Autowired
  PlayerService playerService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Player player = playerService.findOneByEmail(email);
    if (player == null) {
      throw new AuthenticationCredentialsNotFoundException("Email address is not found");
    }
    return createSecurityUserFromClient(player);
  }

  public User createSecurityUserFromClient(Player player) {
    List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(player.getRole().getName().name()));
    User securityUser = new UserBuilder()
        .username(player.getEmail())
        .password(player.getPassword())
        .enabled(true)
        .accountNonExpired(true)
        .credentialsNonExpired(true)
        .accountNonLocked(true)
        .authorities(authorities)
        .build();

    return securityUser;
    }
}

