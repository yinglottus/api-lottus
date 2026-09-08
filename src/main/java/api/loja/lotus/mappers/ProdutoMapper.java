package api.loja.lotus.mappers;

import api.loja.lotus.dtos.produto.ProdutoRequestDTO;
import api.loja.lotus.dtos.produto.ProdutoResponseDTO;
import api.loja.lotus.models.Produto;

public class ProdutoMapper {
    
    public static Produto toEntity(ProdutoRequestDTO dto) {
        
        return Produto.builder()
            .nome(dto.nome())
            .descricao(dto.descricao())
            .preco(dto.preco())
        .build();
    }

    public static ProdutoResponseDTO toDTO(Produto produto) {

        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getImagens().stream().map(ProdutoImagemMapper::toDTO).toList(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.isAtivo()
        );
    }

}
