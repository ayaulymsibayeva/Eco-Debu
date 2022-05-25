package springboot.Eco_Debut.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import springboot.Eco_Debut.entities.Users;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService extends UserDetailsService {
    Users getUserByEmail(String email);

    Users saveUser(Users user);

    Users addUser(Users user);

    Users getUser(Long id);

    void deleteUser(Users user);

    List<Users> getAllUsers();

    void sendVerificationEmail(Users user, String siteURL) throws MessagingException, UnsupportedEncodingException;
}
