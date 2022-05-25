package springboot.Eco_Debut.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springboot.Eco_Debut.entities.Users;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users,Long> {
    Users findByEmail(String email);
    Users findByAddress(String address);

    @Query("SELECT u FROM Users u WHERE u.verificationCode = :code")
    public Users findByVerificationCode(@Param("code") String code);
}
