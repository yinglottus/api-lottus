package api.loja.lotus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/{carrinhoId}/adicionar")
    public ResponseEntity<CupomResponseDTO> adicionarCupomCarrinho(
        @PathVariable("carrinhoId") Long carrinhoId,
        @RequestBody @Valid CupomAdicionarDTO dto
    ) 
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(cupomService.aplicarDescontoCarrinho(carrinhoId, dto));
    }

}
