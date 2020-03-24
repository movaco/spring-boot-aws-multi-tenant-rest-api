package de.movaco.server.multi_tenancy;

public final class TenantContext {

  private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

  private TenantContext() {
    // static class
  }

  public static String getCurrentTenant() {
    return currentTenant.get();
  }

  public static void setCurrentTenant(String tenant) {
    currentTenant.set(tenant);
  }

  public static void clear() {
    currentTenant.remove();
  }
}
