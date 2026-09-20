package api.loja.lotus.mappers;

import api.loja.lotus.dtos.carrinho.CarrinhoResponseDTO;
import api.loja.lotus.dtos.cupom.CupomResponseDTO;
import api.loja.lotus.models.Carrinho;

public class CarrinhoMapper {
    
    public static CarrinhoResponseDTO toDTO(Carrinho carrinho) {
        
        CupomResponseDTO dto = carrinho.getCupom() != null
            ? CupomResponseDTO.from(carrinho.getCupom())
            : null;

        return new CarrinhoResponseDTO(
            carrinho.getId(),
            carrinho.getSubTotal(),
            carrinho.getCartToken(),
            dto,
            carrinho.getItens().stream().map(ItemCarrinhoMapper::toDTO).toList()
        );
    }

}
