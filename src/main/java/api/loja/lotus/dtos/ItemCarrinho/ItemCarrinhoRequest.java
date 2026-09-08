package api.loja.lotus.dtos.ItemCarrinho;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemCarrinhoRequest(
    
    @NotNull
    Long produtoId,

    @NotNull(message = "Quantidade é obrigatória!")
    @Positive(message = "Quantidade deve ser positiva!")
    Integer quantidade
) {
    
}
