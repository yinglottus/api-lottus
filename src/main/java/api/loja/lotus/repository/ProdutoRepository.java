package api.loja.lotus.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import api.loja.lotus.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
 
    boolean existsByNomeIgnoreCase(String nome);
    
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    Page<Produto> findAllByAtivo(boolean ativo, Pageable pageable); 

}
