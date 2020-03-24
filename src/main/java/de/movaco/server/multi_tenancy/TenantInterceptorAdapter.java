package de.movaco.server.multi_tenancy;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class TenantInterceptorAdapter extends HandlerInterceptorAdapter {

  private final TenantResolver tenantDetector;

  @Autowired
  public TenantInterceptorAdapter(TenantResolver tenantDetector) {
    this.tenantDetector = tenantDetector;
  }

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    Optional<String> tenantSchemaName = tenantDetector.detectTenantSchemaName(request);
    if (tenantSchemaName.isPresent()) {
      TenantContext.setCurrentTenant(tenantSchemaName.get());
    } else {
      TenantContext.clear();
    }
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView) {
    TenantContext.clear();
  }
}
