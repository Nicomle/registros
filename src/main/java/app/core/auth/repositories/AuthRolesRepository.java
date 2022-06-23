package app.core.auth.repositories;

import app.core.auth.entities.AuthRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRolesRepository extends JpaRepository<AuthRoles, Long> {
}
