package de.movaco.server.persistence.entities;

import de.movaco.server.model.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity(name = "user_details")
@Table(name = "user_details")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsEntity extends BaseEntity implements User {

  @NotNull
  @Length(min = 3, max = 50)
  @Column(name = "user_name")
  private String userName;

  @NotNull
  @Length(min = 1, max = 200)
  @Column(name = "first_name")
  private String firstName;

  @NotNull
  @Length(min = 1, max = 200)
  @Column(name = "last_name")
  private String lastName;

  @NotNull @Email private String email;
}
