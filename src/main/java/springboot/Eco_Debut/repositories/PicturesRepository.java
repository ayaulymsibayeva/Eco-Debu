package springboot.Eco_Debut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.Eco_Debut.entities.Pictures;

import java.util.List;

@Repository
@Transactional
public interface PicturesRepository extends JpaRepository<Pictures,Long> {
    List<Pictures> findAllByItemId(Long Item_id);
    Pictures findByUrlEquals(String url);
}
