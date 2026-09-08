package api.loja.lotus.dtos.produto;

import java.math.BigDecimal;
import java.util.List;

import api.loja.lotus.dtos.imagem.ProdutoImagemResponseDTO;

public record ProdutoResponseDTO(

    Long id,

    List<ProdutoImagemResponseDTO> imagens,

    String nome,

    String descricao,

    BigDecimal preco,

    boolean ativo
) {
    
}
