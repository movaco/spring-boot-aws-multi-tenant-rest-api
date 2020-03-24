package de.movaco.server.multi_tenancy;

import java.util.Optional;

public interface SecurityContextTenantResolver {

  Optional<String> resolveTenant();
}
