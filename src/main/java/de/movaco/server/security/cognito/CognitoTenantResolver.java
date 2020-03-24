package de.movaco.server.security.cognito;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import de.movaco.server.exception.TenantNotDefinedForUserException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CognitoTenantResolver {

  @Value("${cognito.poolId}")
  private String poolId;

  private final AWSCredentialsProviderChain awsCredentialsProviderChain;

  public CognitoTenantResolver() {
    this.awsCredentialsProviderChain = new DefaultAWSCredentialsProviderChain();
  }

  String getTenant(String userName) {
    AWSCognitoIdentityProvider provider =
        AWSCognitoIdentityProviderClient.builder()
            .withCredentials(this.awsCredentialsProviderChain)
            .build();
    AdminGetUserRequest adminGetUserRequest =
        new AdminGetUserRequest().withUserPoolId(this.poolId).withUsername(userName);
    AdminGetUserResult adminGetUserResult = provider.adminGetUser(adminGetUserRequest);
    Optional<AttributeType> first =
        adminGetUserResult.getUserAttributes().stream()
            .filter(a -> a.getName().equals("custom:tenant"))
            .findFirst();
    if (first.isPresent()) {
      return first.get().getValue();
    } else {
      throw new TenantNotDefinedForUserException();
    }
  }
}
