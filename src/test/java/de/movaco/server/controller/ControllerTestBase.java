package de.movaco.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.movaco.server.shared.TestTenantContextBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ControllerTestBase extends TestTenantContextBase {

  @Autowired protected ObjectMapper objectMapper;

  @Autowired protected MockMvc mockMvc;
}
