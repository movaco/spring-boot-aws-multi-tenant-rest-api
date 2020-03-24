package de.movaco.server.controller;

public final class RestEndpoints {

  public static final String API = "";

  public static final String HEALTH = API + "/health";

  public static final String TENANTS = API + "/tenants";
  public static final String TENANTS_CREATE = "/create";
  public static final String TENANTS_DELETE = "/delete";
  public static final String TENANTS_GET_NAME = "/name";

  public static final String USERS = API + "/users";
  public static final String USERS_GET_BY_NAME = "/getByName";
  public static final String USERS_GET_CURRENT = "/get";
  public static final String USERS_CREATE = "/create";
  public static final String USERS_UPDATE = "/update";
  public static final String USERS_DELETE = "/delete";

  private RestEndpoints() {
    // constants only
  }
}
