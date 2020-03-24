package de.movaco.server.shared;

import de.movaco.server.security.Roles;
import de.movaco.server.shared.AdminUser.AdminMockSecurityContextFactory;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.StringUtils;

@Inherited
@Documented
@WithSecurityContext(factory = AdminMockSecurityContextFactory.class)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminUser {

  String username() default "admin";

  String[] roles() default {Roles.ADMIN};

  String value() default "admin";

  String[] authorities() default {};

  String password() default "password";

  class AdminMockSecurityContextFactory implements WithSecurityContextFactory<AdminUser> {
    AdminMockSecurityContextFactory() {}

    public SecurityContext createSecurityContext(AdminUser withUser) {
      String username =
          StringUtils.hasLength(withUser.username()) ? withUser.username() : withUser.value();
      if (username == null) {
        throw new IllegalArgumentException(
            withUser + " cannot have null username on both username and value properites");
      } else {
        List<GrantedAuthority> grantedAuthorities = new ArrayList();
        String[] var4 = withUser.authorities();
        int var5 = var4.length;

        int var6;
        String role;
        for (var6 = 0; var6 < var5; ++var6) {
          role = var4[var6];
          grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }

        if (grantedAuthorities.isEmpty()) {
          var4 = withUser.roles();
          var5 = var4.length;

          for (var6 = 0; var6 < var5; ++var6) {
            role = var4[var6];
            if (role.startsWith("ROLE_")) {
              throw new IllegalArgumentException("roles cannot start with ROLE_ Got " + role);
            }

            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role));
          }
        } else if (withUser.roles().length != 1 || !"USER".equals(withUser.roles()[0])) {
          throw new IllegalStateException(
              "You cannot define roles attribute "
                  + Arrays.asList(withUser.roles())
                  + " with authorities attribute "
                  + Arrays.asList(withUser.authorities()));
        }

        User principal =
            new User(username, withUser.password(), true, true, true, true, grantedAuthorities);
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
      }
    }
  }
}
