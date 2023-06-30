package app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import app.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    User findByEmail(String email);

    User findByConfirmationToken(String confirmationToken);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);
    User getUsernameByLogin(String login);

}