package api.loja.lotus.dtos.produto;

import java.math.BigDecimal;
import java.util.List;

import api.loja.lotus.dtos.imagem.ProdutoImagemResponseDTO;
import api.loja.lotus.models.enums.Categoria;

public record ProdutoResponseDTO(

    Long id,

    List<ProdutoImagemResponseDTO> imagens,

    String nome,

    Categoria categoria,

    String descricao,

    BigDecimal preco,

    boolean ativo
) {
    
}
