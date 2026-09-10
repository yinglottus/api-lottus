package api.loja.lotus.dtos.ItemCarrinho;

import jakarta.validation.constraints.NotNull;

public record ItemCarrinhoRequestDTO(
    
    @NotNull
    Long produtoId
    
) {
    
}
