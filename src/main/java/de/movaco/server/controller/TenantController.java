package de.movaco.server.controller;

import static de.movaco.server.controller.RestEndpoints.TENANTS;

import de.movaco.server.service.TenantService;
import io.swagger.annotations.ApiOperation;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = TENANTS)
public class TenantController {

  private final TenantService tenantService;

  @Autowired
  public TenantController(TenantService tenantService) {
    this.tenantService = tenantService;
  }

  @GetMapping(value = RestEndpoints.TENANTS_CREATE)
  public UUID createTenant(
      @RequestParam(value = "tenantName") String tenantName,
      @RequestParam(value = "schemaName") String schemaName) {
    return this.tenantService.createTenant(tenantName, schemaName);
  }

  @ApiOperation(value = "Get name of tenant")
  @GetMapping(value = RestEndpoints.TENANTS_GET_NAME)
  public String getName() {
    return tenantService.getTenantName();
  }

  // We do not use @DeleteMapping to be able to enable CSRF protection
  @GetMapping(value = RestEndpoints.TENANTS_DELETE + "/{tenantId}")
  public void deleteTenant(@PathVariable(value = "tenantId") UUID tenantId) {
    this.tenantService.deleteTenant(tenantId);
  }
}
