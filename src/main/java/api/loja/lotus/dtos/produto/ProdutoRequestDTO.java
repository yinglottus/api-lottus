package api.loja.lotus.dtos.produto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdutoRequestDTO(

    @NotBlank (message = "Nome é obrigatório!")
    String nome,

    @NotBlank(message = "Descrição é obrigatório!")
    String descricao,

    @NotNull(message = "Preço é obrigatório!")
    @Positive(message = "Preço deve ser maior que 0!")
    BigDecimal preco
) {
    
}
