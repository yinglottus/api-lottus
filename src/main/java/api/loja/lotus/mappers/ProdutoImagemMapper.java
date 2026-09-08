package api.loja.lotus.mappers;

import api.loja.lotus.dtos.imagem.ProdutoImagemResponseDTO;
import api.loja.lotus.models.ProdutoImagem;

public class ProdutoImagemMapper {
    
    public static ProdutoImagemResponseDTO toDTO(ProdutoImagem produtoImagem) {

        return new ProdutoImagemResponseDTO(
            produtoImagem.getId(),
            produtoImagem.getImagemUrl()
        );
    }

}
