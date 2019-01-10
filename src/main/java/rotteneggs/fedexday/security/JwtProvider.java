package rotteneggs.fedexday.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static rotteneggs.fedexday.security.SecurityConstants.*;

@Component
public class JwtProvider {

  public String generateJwtToken(Authentication authentication) {
    return TOKEN_PREFIX + Jwts.builder()
        .setSubject(((User) authentication.getPrincipal()).getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS384, SECRET)
        .compact();
  }

  public String getJwtStringFromHeader(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, TOKEN_KEY);
    String authHeader = cookie.getValue();
    if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
      return authHeader.replace(TOKEN_PREFIX, "");
    }
    return null;
  }

  public String getEmailNameFromJwtToken(String token) {
    return Jwts.parser()
        .setSigningKey(SECRET)
        .parseClaimsJws(token)
        .getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
      return true;
    } catch (ExpiredJwtException | MalformedJwtException | SignatureException
        | UnsupportedJwtException | IllegalArgumentException e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  public String getEmailFromRequestJwt(HttpServletRequest request) {
    return getEmailNameFromJwtToken(getJwtStringFromHeader(request));
  }
}
