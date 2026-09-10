package api.loja.lotus.dtos.ItemCarrinho;

import java.math.BigDecimal;
import java.util.List;

import api.loja.lotus.dtos.imagem.ProdutoImagemResponseDTO;

public record ItemCarrinhoResponseDTO(

    Long id,

    Long produtoId,

    String nomeProduto,

    List<ProdutoImagemResponseDTO> imagens,

    BigDecimal precoUnitario,

    Integer quantidade
) {
    
}
