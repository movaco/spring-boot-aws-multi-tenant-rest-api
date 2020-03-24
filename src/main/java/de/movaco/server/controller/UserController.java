package de.movaco.server.controller;

import static de.movaco.server.controller.RestEndpoints.USERS;
import static de.movaco.server.dto.UserDTO.of;

import de.movaco.server.dto.UserDTO;
import de.movaco.server.exception.NoUserAccountException;
import de.movaco.server.exception.UserDetailsNotFoundException;
import de.movaco.server.security.AuthenticatedUserService;
import de.movaco.server.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = USERS)
public class UserController {

  private final UserService userService;
  private final AuthenticatedUserService userAuthenticationService;

  @Autowired
  public UserController(
      UserService userService, AuthenticatedUserService userAuthenticationService) {
    this.userService = userService;
    this.userAuthenticationService = userAuthenticationService;
  }

  @ApiOperation(value = "Get user details by username")
  @GetMapping(value = RestEndpoints.USERS_GET_BY_NAME)
  public UserDTO getUserByName(@RequestParam(value = "userName") String userName) {
    return of(this.userService.getUserByUsername(userName));
  }

  @ApiOperation(value = "Update user account")
  @PostMapping(value = RestEndpoints.USERS_UPDATE)
  public UserDTO updateUser(@RequestBody UserDTO userDTO) {
    return of(this.userService.updateUserDetails(userDTO));
  }

  @ApiOperation(value = "Create a new user account")
  @PostMapping(value = RestEndpoints.USERS_CREATE)
  public UserDTO createUser(@RequestBody UserDTO userDTO) {
    return of(this.userService.createAppUser(userDTO));
  }

  @ApiOperation(value = "Delete a user account by username")
  @GetMapping(value = RestEndpoints.USERS_DELETE)
  public void deleteUser(@RequestParam(value = "userName") String userName) {
    this.userService.deleteUser(userName);
  }

  @ApiOperation(value = "Get user details of current user")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Successfully retrieved user details"),
        @ApiResponse(code = 401, message = "You are not authorized to access user details"),
        @ApiResponse(
            code = 412,
            message = "You do not have an account yet. Please use /create to create an account")
      })
  @GetMapping(value = RestEndpoints.USERS_GET_CURRENT)
  public UserDTO getCurrentUser() {
    String userName = this.userAuthenticationService.getAuthenticatedUsername();
    try {
      return of(this.userService.getUserByUsername(userName));
    } catch (UserDetailsNotFoundException e) {
      throw new NoUserAccountException("No user account found for user " + userName, e);
    }
  }
}
