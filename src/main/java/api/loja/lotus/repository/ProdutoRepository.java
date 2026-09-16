package api.loja.lotus.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import api.loja.lotus.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
 
    boolean existsByNomeIgnoreCase(String nome);
    
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    @EntityGraph(attributePaths = "imagens")
    Page<Produto> findAllByAtivo(boolean ativo, Pageable pageable); 

    @EntityGraph(attributePaths = "imagens")
    Optional<Produto> findById(Long id);

}
