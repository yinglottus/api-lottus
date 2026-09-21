package api.loja.lotus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import api.loja.lotus.models.Carrinho;
import api.loja.lotus.models.Usuario;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    
    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Optional<Carrinho> findByUsuario(Usuario usuario);

    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Optional<Carrinho> findByCartToken(String cartToken);

    @EntityGraph(attributePaths = {"itens", "itens.produto"})
    Optional<Carrinho> findByCartTokenAndUsuarioIsNull(String carToken);

    @Modifying 
    @Query("""
            UPDATE Carrinho c
            SET c.cupom = null,
                c.subTotalDescontado = null
            WHERE c.cupom.id = :cupomId
            """)
    int removerCupomDosCarrinhos(@Param("cupomId") Long cupomId);

}
