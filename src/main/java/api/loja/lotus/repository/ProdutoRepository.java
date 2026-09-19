package api.loja.lotus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import api.loja.lotus.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>,
JpaSpecificationExecutor<Produto> 
{
 
    boolean existsByNomeIgnoreCase(String nome);
    
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    @EntityGraph(attributePaths = "imagens")
    Optional<Produto> findById(Long id);

}
