package api.loja.lotus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.loja.lotus.models.ItemCarrinho;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {
    
}
