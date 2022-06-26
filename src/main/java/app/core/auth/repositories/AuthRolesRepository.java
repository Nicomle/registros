package app.core.auth.repositories;

import app.core.auth.entities.AuthRoles;
import app.core.auth.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRolesRepository extends JpaRepository<AuthRoles, Long> {

    Optional<AuthRoles> findByRol(Roles rol);
}
