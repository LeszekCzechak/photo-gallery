package pl.czechak.leszek.photogalerybackend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class GalleryRepositoryTest {

    @Autowired
    GalleryRepository galleryRepository;

    @Test
    @Sql(statements = {
            "INSERT INTO user_entity(user_id,password,username) VALUES (99,'$2a$10$flwjx8Bqss5SNktHaTFyIuuSpvBrAj8sEDpURwdjq1UgUrzeQh/la','TESTadmin')",
            "INSERT INTO gallery_entity(id,gallery_name,user_user_id) values (615,'galleryName',99)"
    })
    void shouldFindGalleryEntitiesByUserId() {

        //given
        //when
        List<GalleryEntity> galleryEntityList = galleryRepository.findAllByUser_Id(99L);

        //then
        assertEquals("galleryName",galleryEntityList.get(0).getGalleryName());
        assertEquals("TESTadmin",galleryEntityList.get(0).getUser().getUsername());
    }
}
