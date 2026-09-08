package api.loja.lotus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.loja.lotus.models.ProdutoImagem;

public interface ProdutoImagemRepository extends JpaRepository<ProdutoImagem, Long> {
    
}
