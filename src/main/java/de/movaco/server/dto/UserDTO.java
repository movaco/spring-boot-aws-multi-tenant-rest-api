package de.movaco.server.dto;

import de.movaco.server.model.User;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "User", description = "Details a user. ")
public class UserDTO implements User {

  private String userName;
  private String firstName;
  private String lastName;
  private String email;

  public static UserDTO of(User user) {
    return new UserDTO(
        user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail());
  }
}
