package de.movaco.server.config;

import de.movaco.server.multi_tenancy.TenantInterceptorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAutoConfiguration
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final TenantInterceptorAdapter tenantInterceptorAdapter;

  @Autowired
  public WebMvcConfig(TenantInterceptorAdapter tenantInterceptorAdapter) {
    this.tenantInterceptorAdapter = tenantInterceptorAdapter;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedMethods("GET", "POST");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(this.tenantInterceptorAdapter);
  }
}
