package de.movaco.server.config;

import de.movaco.server.controller.RestEndpoints;
import de.movaco.server.security.Roles;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {

  private final AuthenticationManager authenticationProvider;

  @Autowired
  public SecurityConfig(AuthenticationManager authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.authenticationManager(authenticationProvider);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.requestMatcher(new OAuthRequestedMatcher())
        .authorizeRequests()
        .antMatchers("/")
        .authenticated()
        .antMatchers(RestEndpoints.USERS + "/*")
        .hasAnyRole(Roles.ADMIN, Roles.USER)
        .antMatchers(RestEndpoints.TENANTS + RestEndpoints.TENANTS_GET_NAME)
        .hasAnyRole(Roles.ADMIN, Roles.USER)
        .antMatchers(RestEndpoints.TENANTS + "/*")
        .hasRole(Roles.ADMIN);
  }

  private static class OAuthRequestedMatcher implements RequestMatcher {

    public boolean matches(HttpServletRequest request) {
      String auth = request.getHeader("Authorization");
      boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
      boolean haveAccessToken = request.getParameter("access_token") != null;
      return haveOauth2Token || haveAccessToken;
    }
  }
}
