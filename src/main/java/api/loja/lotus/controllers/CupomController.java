package api.loja.lotus.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.loja.lotus.dtos.cupom.CupomAdicionarDTO;
import api.loja.lotus.dtos.cupom.CupomRequestDTO;
import api.loja.lotus.dtos.cupom.CupomResponseDTO;
import api.loja.lotus.services.CupomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController 
@RequestMapping("/cupom")
public class CupomController {
    
    private final CupomService cupomService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/criar")
    public ResponseEntity<CupomResponseDTO> criarCupom(@RequestBody @Valid CupomRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cupomService.criarCupom(dto));
    }   

    @PostMapping("/adicionar")
    public ResponseEntity<CupomResponseDTO> adicionarCupomCarrinho(
        @RequestHeader(value = "X-cart-Token", required = false) String cartToken,
        @RequestBody @Valid CupomAdicionarDTO dto
    ) 
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(cupomService.aplicarDescontoCarrinho(cartToken, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{cupomId}/atualizar")
    public ResponseEntity<CupomResponseDTO> atualizarCupom(
        @PathVariable("cupomId") Long cupomId,
        @RequestBody @Valid CupomRequestDTO dto
    ) 
    {
        return ResponseEntity.ok(cupomService.atualizarCupom(dto, cupomId));
    }

    @GetMapping("/buscar/todos")
    public ResponseEntity<Page<CupomResponseDTO>> buscarTodosCupons(
        @PageableDefault(size = 12, sort = "quantidade") Pageable pageable
    ) 
    {
        return ResponseEntity.ok(cupomService.buscarTodosCupons(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<CupomResponseDTO> buscarCupomMeuCarrinho(
        @RequestHeader(value = "X-cart-Token", required = false) String cartToken
    ) 
    {
        return ResponseEntity.ok(cupomService.buscarCupomMeuCarrinho(cartToken));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{cupomId}/buscar")
    public ResponseEntity<CupomResponseDTO> buscarCupomPorId(
        @PathVariable("cupomId") Long cupomId
    ) 
    {
        return ResponseEntity.ok(cupomService.buscarCupomPorId(cupomId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{cupomId}/deletar")
    public ResponseEntity<Void> deletarCupom(
        @PathVariable("cupomId") Long cupomId
    ) 
    {
        cupomService.deletarCupom(cupomId);

        return ResponseEntity.noContent().build();
    }

}
