package dev.franke.felipe.transaction_manager.api.repository;

import dev.franke.felipe.transaction_manager.api.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserDetails findByEmail(String email);

    UserDetails findByName(String name);
}
