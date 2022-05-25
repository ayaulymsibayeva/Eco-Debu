package springboot.Eco_Debut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.Eco_Debut.entities.Roles;

@Repository
@Transactional
public interface RolesRepository extends JpaRepository<Roles,Long> {
}
