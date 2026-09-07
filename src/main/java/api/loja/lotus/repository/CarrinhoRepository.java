package api.loja.lotus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.loja.lotus.models.Carrinho;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    
}
