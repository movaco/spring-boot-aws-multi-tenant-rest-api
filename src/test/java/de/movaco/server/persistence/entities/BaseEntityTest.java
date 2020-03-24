package de.movaco.server.persistence.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BaseEntityTest {

  private static class TestEntity extends BaseEntity {

    private final String field;

    private TestEntity(String field) {
      this.field = field;
    }
  }

  @Test
  public void equals_works() {
    TestEntity a = new TestEntity("a");
    TestEntity b = new TestEntity("b");
    assertThat(a).isEqualTo(a);
    assertThat(a).isNotEqualTo(b);
    assertThat(a).isNotEqualTo(null);
  }

  @Test
  public void hashCode_works() {
    TestEntity a = new TestEntity("a");
    TestEntity b = new TestEntity("b");
    assertThat(a.hashCode()).isNotNull();
    // hashcode is based on the uuid and should not be equal
    assertThat(a.hashCode()).isNotEqualTo(new TestEntity("a").hashCode());
    assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
  }

  @Test
  public void shouldBeTransientAfterConstruction() {
    assertThat(new TestEntity("a").isTransient()).isTrue();
  }

  @Test
  public void version_shouldBeNull() {
    assertThat(new TestEntity("a").getVersion()).isNull();
  }
}
