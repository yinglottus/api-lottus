package api.loja.lotus.dtos.produto;

import java.math.BigDecimal;

import api.loja.lotus.models.enums.Categoria;

public record ProdutoFilterDTO(

    String nome,

    Categoria categoria,

    BigDecimal precoMinimo,

    BigDecimal precoMaximo
) 
{
    
}
