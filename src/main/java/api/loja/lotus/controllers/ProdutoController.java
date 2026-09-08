package api.loja.lotus.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import api.loja.lotus.dtos.produto.ProdutoRequestDTO;
import api.loja.lotus.dtos.produto.ProdutoResponseDTO;
import api.loja.lotus.services.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController 
@RequestMapping("/produto")
public class ProdutoController {
    
    private final ProdutoService produtoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponseDTO> criarProduto(
        @RequestPart("produto") ProdutoRequestDTO dto,
        @RequestPart(required = false, value = "imagens") List<MultipartFile> imagens
    )
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criarProduto(dto, imagens));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{produtoId}/adicionar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponseDTO> adicionarImagensProduto(
        @RequestPart("imagens") List<MultipartFile> imagens,
        @PathVariable("produtoId") Long produtoId
    ) 
    {
        return ResponseEntity.ok(produtoService.adicionarImagensProduto(imagens, produtoId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{produtoId}/atualizar")
    public ResponseEntity<ProdutoResponseDTO> editarCamposProduto(
        @RequestBody @Valid ProdutoRequestDTO dto,
        @PathVariable("produtoId") Long produtoId
    ) 
    {
        return ResponseEntity.ok(produtoService.atualizarProduto(dto, produtoId));
    }

    @GetMapping("/buscar/ativos")
    public ResponseEntity<Page<ProdutoResponseDTO>> buscarTodosProdutosAtivos(
        @PageableDefault(size = 12, sort = "nome") Pageable pageable
    ) 
    {
        return ResponseEntity.ok(produtoService.buscarTodosProdutosAtivos(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/buscar/admin")
    public ResponseEntity<Page<ProdutoResponseDTO>> buscarTodosProdutosAdmin(
        @PageableDefault(size = 12, sort = "nome") Pageable pageable
    ) 
    {   
        return ResponseEntity.ok(produtoService.buscarTodosProdutosAdmin(pageable));
    }

    @GetMapping("/{produtoId}/buscar")
    public ResponseEntity<ProdutoResponseDTO> buscarProdutoPorId(@PathVariable("produtoId") Long id) {
        return ResponseEntity.ok(produtoService.buscarProdutoPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{produtoId}/imagens/{imagemId}")
    public ResponseEntity<Void> excluirImagemProduto(
        @PathVariable("produtoId") Long produtoId,
        @PathVariable("imagemId") Long imagemId
    ) 
    {
        produtoService.removerImagemProduto(produtoId, imagemId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{produtoId}/deletar")
    public ResponseEntity<Void> excluirProduto(@PathVariable("produtoId") Long id) {
        
        produtoService.excluirProduto(id);

        return ResponseEntity.noContent().build();
    }

}
