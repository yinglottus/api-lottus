package api.loja.lotus.dtos.cupom;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CupomRequestDTO(

    @NotBlank
    @Size(min = 3, max = 8, message = "Código é obrigatório!")
    String codigo,

    @Min(1)
    @Max(100)
    @NotNull
    BigDecimal desconto,

    @NotNull
    Integer quantidade
) {
    
}
