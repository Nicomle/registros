package app.feature.repositories;

import app.feature.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByDni(Long dni);

    Optional<Usuario> findByUserNameAndPassword(String userName, String password);

    Optional<Usuario> findByUserName(String userName);
}
