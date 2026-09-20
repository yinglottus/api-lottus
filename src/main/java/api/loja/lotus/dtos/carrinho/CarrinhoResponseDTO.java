package api.loja.lotus.dtos.carrinho;

import java.math.BigDecimal;
import java.util.List;

import api.loja.lotus.dtos.ItemCarrinho.ItemCarrinhoResponseDTO;
import api.loja.lotus.dtos.cupom.CupomResponseDTO;

public record CarrinhoResponseDTO(
    
    Long id,
    
    BigDecimal subTotal,

    String cartToken,

    CupomResponseDTO cupom,

    List<ItemCarrinhoResponseDTO> itens
) {
    
}
