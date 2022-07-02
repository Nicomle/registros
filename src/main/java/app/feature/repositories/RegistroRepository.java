package app.feature.repositories;

import app.feature.entities.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {

    @Query(value = "SELECT * FROM REGISTROS as r WHERE r.id = ?1", nativeQuery = true)
    List<Registro> obtenerRegistrosUsuario(Long id);
}
