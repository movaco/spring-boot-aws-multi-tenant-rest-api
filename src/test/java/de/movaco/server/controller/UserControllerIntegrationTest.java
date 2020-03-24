package de.movaco.server.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.movaco.server.dto.UserDTO;
import de.movaco.server.multi_tenancy.TenantContext;
import de.movaco.server.persistence.entities.UserDetailsEntity;
import de.movaco.server.persistence.repository.UserDetailsRepository;
import de.movaco.server.security.AuthenticatedUserService;
import de.movaco.server.service.UserService;
import de.movaco.server.shared.TestUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class UserControllerIntegrationTest extends ControllerTestBase {

  private static final String TEST_USER_NAME = "tester";
  private static final UserDetailsEntity TEST_USER =
      new UserDetailsEntity(TEST_USER_NAME, "Helge", "Schneider", "testuser@company.com");

  @Autowired private UserDetailsRepository userDetailsRepository;

  @Autowired private UserService userService;

  @MockBean AuthenticatedUserService authenticatedUserService;

  @Test
  @TestUser
  public void shouldReturnTestUser() throws Exception {
    deleteAndCreateTestUser();
    this.mockMvc
        .perform(
            get(
                RestEndpoints.USERS
                    + RestEndpoints.USERS_GET_BY_NAME
                    + "?userName="
                    + TEST_USER_NAME))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(TEST_USER.getEmail()));
  }

  @Test
  @TestUser
  public void shouldReturnCurrentUser() throws Exception {
    deleteAndCreateTestUser();
    when(authenticatedUserService.getAuthenticatedUsername()).thenReturn(TEST_USER_NAME);
    this.mockMvc
        .perform(get(RestEndpoints.USERS + RestEndpoints.USERS_GET_CURRENT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value(TEST_USER.getEmail()))
        .andExpect(jsonPath("$.userName").value(TEST_USER_NAME));
  }

  @Test
  @TestUser
  public void shouldReturnPreconditionFailedStatus() throws Exception {
    when(authenticatedUserService.getAuthenticatedUsername()).thenReturn(TEST_USER_NAME);
    this.mockMvc
        .perform(get(RestEndpoints.USERS + RestEndpoints.USERS_GET_CURRENT))
        .andExpect(status().is(HttpStatus.PRECONDITION_FAILED.value()));
  }

  @Test
  @TestUser
  public void createUser() throws Exception {
    this.mockMvc
        .perform(
            post(RestEndpoints.USERS + RestEndpoints.USERS_CREATE)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_USER)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("testuser@company.com"));
    TenantContext.setCurrentTenant(TEST_TENANT_SCHEMA_NAME);
    assertThat(this.userDetailsRepository.findByUserName(TEST_USER_NAME)).isNotEmpty();
  }

  @Test
  @TestUser
  public void createAndDeleteUser() throws Exception {
    deleteAndCreateTestUser();
    assertThat(this.userDetailsRepository.findByUserName(TEST_USER_NAME)).isNotEmpty();
    this.mockMvc
        .perform(
            get(RestEndpoints.USERS + RestEndpoints.USERS_DELETE + "?userName=" + TEST_USER_NAME))
        .andExpect(status().isOk());
  }

  @Test
  @TestUser
  public void updateUser() throws Exception {
    deleteAndCreateTestUser();
    UserDTO updatedUser = UserDTO.of(TEST_USER);
    updatedUser.setEmail("new@mail.com");
    this.mockMvc
        .perform(
            post(RestEndpoints.USERS + RestEndpoints.USERS_UPDATE)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("new@mail.com"));
  }

  private void deleteAndCreateTestUser() {
    this.userService.createAppUser(TEST_USER);
  }
}
