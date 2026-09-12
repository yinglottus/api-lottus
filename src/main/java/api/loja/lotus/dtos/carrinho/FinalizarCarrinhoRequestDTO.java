package api.loja.lotus.dtos.carrinho;

import jakarta.validation.constraints.NotBlank;

public record FinalizarCarrinhoRequestDTO(

    @NotBlank 
    String rua,

    @NotBlank 
    String numero
) {
    
}
