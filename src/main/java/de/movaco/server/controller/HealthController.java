package de.movaco.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class HealthController {

  @ApiIgnore
  @GetMapping(value = RestEndpoints.HEALTH)
  public String checkHealth() {
    return "REST-Service is up and running!";
  }

  @ApiIgnore
  @GetMapping(value = RestEndpoints.API)
  public void root() {}
}
