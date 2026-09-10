package api.loja.lotus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.loja.lotus.models.Carrinho;
import api.loja.lotus.models.Usuario;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    
    Optional<Carrinho> findByUsuario(Usuario usuario);

    Optional<Carrinho> findByCartToken(String cartToken);

}
