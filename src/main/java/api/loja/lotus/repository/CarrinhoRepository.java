package api.loja.lotus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import api.loja.lotus.models.Carrinho;
import api.loja.lotus.models.Usuario;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    
    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Optional<Carrinho> findByUsuario(Usuario usuario);

    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Optional<Carrinho> findByCartToken(String cartToken);

    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Optional<Carrinho> findByCartTokenAndUsuarioIsNull(String carToken);

}
