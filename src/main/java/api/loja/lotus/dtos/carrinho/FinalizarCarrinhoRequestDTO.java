package api.loja.lotus.dtos.carrinho;

import jakarta.validation.constraints.NotBlank;

public record FinalizarCarrinhoRequestDTO(

    @NotBlank 
    String cidade,

    @NotBlank 
    String rua,

    @NotBlank 
    String numero,

    @NotBlank 
    String nome,

    @NotBlank 
    String mensagem
) {
    
}
