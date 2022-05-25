package springboot.Eco_Debut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.Eco_Debut.entities.Comments;

import java.util.List;

@Repository
@Transactional
public interface CommentsRepository extends JpaRepository<Comments,Long> {
    List<Comments> findAllByItemIdOrderByDateDesc(Long id);
}
