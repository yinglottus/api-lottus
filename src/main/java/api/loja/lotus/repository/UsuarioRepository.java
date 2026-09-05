package api.loja.lotus.repository;

import api.loja.lotus.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmailAndNotId(String email, Long id);

    boolean existsByEmail(String email);
}
