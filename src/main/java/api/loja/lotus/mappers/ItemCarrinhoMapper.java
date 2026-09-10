package api.loja.lotus.mappers;

import api.loja.lotus.dtos.ItemCarrinho.ItemCarrinhoResponseDTO;
import api.loja.lotus.models.ItemCarrinho;

public class ItemCarrinhoMapper {
    
    public static ItemCarrinhoResponseDTO toDTO(ItemCarrinho itemCarrinho) {

        return new ItemCarrinhoResponseDTO(
            itemCarrinho.getId(),
            itemCarrinho.getProduto().getId(),
            itemCarrinho.getProduto().getNome(),
            itemCarrinho.getProduto().getImagens().stream().map(ProdutoImagemMapper::toDTO).toList(),
            itemCarrinho.getProduto().getPreco(),
            itemCarrinho.getQuantidade()
        );
    }

}
