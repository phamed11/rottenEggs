package rotteneggs.fedexday.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

  private ClientDetailsService clientDetailsService;
  private JwtProvider jwtProvider;

  public AuthorizationFilter(AuthenticationManager authenticationManager, ClientDetailsService clientDetailsService, JwtProvider jwtProvider) {
    super(authenticationManager);
    this.clientDetailsService = clientDetailsService;
    this.jwtProvider = jwtProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws IOException, ServletException {

    try {
      String jwt = jwtProvider.getJwtStringFromHeader(request);
      if (jwt != null && jwtProvider.validateJwtToken(jwt)) {
        String username = jwtProvider.getEmailNameFromJwtToken(jwt);
        UserDetails userDetails = clientDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication
            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {

    }
    chain.doFilter(request, response);
  }
}