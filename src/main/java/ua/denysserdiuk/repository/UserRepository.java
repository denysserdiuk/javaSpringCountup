package ua.denysserdiuk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.denysserdiuk.model.Users;


public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}