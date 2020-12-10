package pl.czechak.leszek.photogalerybackend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import pl.czechak.leszek.photogalerybackend.model.user.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(statements = "INSERT INTO user_entity(user_id,password,username) VALUES (99,'$2a$10$flwjx8Bqss5SNktHaTFyIuuSpvBrAj8sEDpURwdjq1UgUrzeQh/la','admin')")
    void shouldFindOptionalOfUserEntityByUsername(){

        Optional<UserEntity> optionalUserEntity = userRepository.findUserEntityByUsername("admin");

        assertEquals(99,optionalUserEntity.get().getId());
        assertEquals("admin",optionalUserEntity.get().getUsername());

        //AssertJ usage
        assertThat(optionalUserEntity.get().getId()).isEqualTo(99);

    }

}
