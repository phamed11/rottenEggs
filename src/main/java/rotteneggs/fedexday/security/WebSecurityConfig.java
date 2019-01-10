package rotteneggs.fedexday.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static rotteneggs.fedexday.security.SecurityConstants.TOKEN_KEY;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  ClientDetailsService clientDetailsService;

  @Autowired
  JwtProvider jwtProvider;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder
        .userDetailsService(clientDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable().
        authorizeRequests()
        .antMatchers("/login").permitAll()
        .antMatchers("/registration/**").permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/**.ico", "/**.png", "/**.jpg", "/static/**", "/css/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.formLogin()
        .loginPage("/login")
        .and()
        .logout().deleteCookies(TOKEN_KEY);

    http.addFilter(new AuthenticationFilter(authenticationManager(), jwtProvider))
        .addFilter(new AuthorizationFilter(authenticationManager(), clientDetailsService, jwtProvider));
  }
}