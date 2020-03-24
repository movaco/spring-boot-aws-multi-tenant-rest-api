package de.movaco.server.security.cognito;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.movaco.server.exception.JwtParsingException;
import de.movaco.server.security.Roles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CognitoAccessTokenReader {

  private final CognitoAccessTokenVerifier tokenVerifier;

  @Autowired
  public CognitoAccessTokenReader(CognitoAccessTokenVerifier tokenVerifier) {
    this.tokenVerifier = tokenVerifier;
  }

  public List<String> getRoles(String accessToken) {
    this.tokenVerifier.verify(accessToken);
    JsonObject payload = getPayload(accessToken);
    List<String> groups = new ArrayList<>();
    JsonElement groupsElement = payload.get("cognito:groups");
    if (groupsElement != null) {
      groupsElement.getAsJsonArray().forEach(group -> groups.add(group.getAsString()));
    }
    List<String> roles = new ArrayList<>();
    roles.add(Roles.USER);
    if (groups.contains(CognitoGroups.COGNITO_ADMIN_GROUP_NAME)) {
      roles.add(Roles.ADMIN);
    }
    if (groups.contains(CognitoGroups.COGNITO_SUPER_ADMIN_GROUP_NAME)) {
      roles.add(Roles.SUPER_ADMIN);
    }
    return roles;
  }

  public String getUserName(String accessToken) {
    this.tokenVerifier.verify(accessToken);
    JsonObject payload = getPayload(accessToken);
    return payload.get("username").getAsString();
  }

  private static JsonObject getPayload(String jwt) {
    JsonParser jsonParser = new JsonParser();
    final String[] jwtParts = jwt.split("\\.");
    if (jwtParts.length != 3) {
      throw new JwtParsingException("Not a valid json web token: " + jwt);
    }
    final byte[] payloadBytes = Base64.getUrlDecoder().decode(jwt.split("\\.")[1]);
    final String decodedString;
    decodedString = new String(payloadBytes, StandardCharsets.UTF_8);
    return (JsonObject) jsonParser.parse(decodedString);
  }
}
