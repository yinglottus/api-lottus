package api.loja.lotus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import api.loja.lotus.models.Cupom;

public interface CupomRepository extends JpaRepository<Cupom, Long> {

    Optional<Cupom> findByCodigo(String codigo);

    boolean existsByCodigoAndQuantidadeGreaterThan(String codigo, Integer quantidade);

    @Modifying 
    @Query("""
            UPDATE Cupom c
            SET c.quantidade = c.quantidade - 1
            WHERE c.id = :id
            AND c.quantidade > 0
            """)
    int decrementarQuantidadeSeDisponivel(@Param("id") Long id);
}
