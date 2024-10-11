package ua.denysserdiuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.denysserdiuk.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}