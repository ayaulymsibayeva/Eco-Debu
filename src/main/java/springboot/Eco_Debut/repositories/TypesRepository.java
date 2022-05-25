package springboot.Eco_Debut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.Eco_Debut.entities.Types;

@Repository
@Transactional
public interface TypesRepository extends JpaRepository<Types,Long> {
}
