package api.loja.lotus.mappers;

import api.loja.lotus.dtos.carrinho.CarrinhoResponseDTO;
import api.loja.lotus.models.Carrinho;

public class CarrinhoMapper {
    
    public static CarrinhoResponseDTO toDTO(Carrinho carrinho) {

        return new CarrinhoResponseDTO(
            carrinho.getId(),
            carrinho.getSubTotal(),
            carrinho.getCartToken(),
            carrinho.getItens().stream().map(ItemCarrinhoMapper::toDTO).toList()
        );
    }

}
