package api.loja.lotus.dtos.ItemCarrinho;

import java.math.BigDecimal;

public record ItemCarrinhoResponseDTO(

    Long id,

    Long produtoId,

    String nomeProduto,

    BigDecimal precoUnitario,

    Integer quantidade,
    
    BigDecimal subTotal
) {
    
}
