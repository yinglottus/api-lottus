package api.loja.lotus.dtos.cupom;

import java.math.BigDecimal;

import api.loja.lotus.models.Cupom;

public record CupomResponseDTO(

    Long id,

    String codigo,

    BigDecimal desconto
) 
{
    
    public static CupomResponseDTO from(Cupom cupom) {

        return new CupomResponseDTO(
            cupom.getId(), 
            cupom.getCodigo(), 
            cupom.getDesconto()
        );
    }
}
