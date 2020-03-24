package de.movaco.server;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class MainTest {

  @Test
  public void contextLoads() {
    // if no exception is thrown, the context is assumed to be loaded correctly
  }
}
