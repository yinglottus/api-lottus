package api.loja.lotus.dtos.carrinho;

import java.math.BigDecimal;
import java.util.List;

import api.loja.lotus.dtos.ItemCarrinho.ItemCarrinhoResponseDTO;

public record CarrinhoResponseDTO(
    
    Long id,
    
    BigDecimal subTotal,

    List<ItemCarrinhoResponseDTO> itens
) {
    
}
