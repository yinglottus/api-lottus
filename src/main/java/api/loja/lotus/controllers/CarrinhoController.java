package api.loja.lotus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.loja.lotus.dtos.ItemCarrinho.ItemCarrinhoRequestDTO;
import api.loja.lotus.dtos.carrinho.CarrinhoResponseDTO;
import api.loja.lotus.services.CarrinhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@RestController 
@RequestMapping("/carrinho")  
public class CarrinhoController {
    
    private final CarrinhoService carrinhoService;

    @PostMapping("/itens/adicionar")
    public ResponseEntity<CarrinhoResponseDTO> adicionarItemCarrinho(
        @RequestBody @Valid ItemCarrinhoRequestDTO dto,
        @RequestHeader(value = "X-cart-Token", required = false) String cartToken
    ) 
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(carrinhoService.adicionarItemCarrinho(
            dto, 
            cartToken)
        );
    }

    @PatchMapping("/{itemId}/aumentar")
    public ResponseEntity<CarrinhoResponseDTO> aumentarQuantidadeItemCarrinho(
        @PathVariable("itemId") Long itemCarrinhoId,
        @RequestHeader(value = "X-cart-Token", required = false) String cartToken
    ) 
    {
        return ResponseEntity.ok(carrinhoService.aumentarQuantidadeItemCarrinho(
            itemCarrinhoId, 
            cartToken)
        );
    }

    @PatchMapping("/{itemId}/diminuir")
    public ResponseEntity<CarrinhoResponseDTO> diminuirQuantidadeItemCarrinho(
        @PathVariable("itemId") Long itemCarrinhoId,
        @RequestHeader(value = "X-cart-Token", required = false) String cartToken
    )
    {
        return ResponseEntity.ok(carrinhoService.diminuirQuantidadeItemCarrinho(
            itemCarrinhoId, 
            cartToken)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<CarrinhoResponseDTO> buscarCarrinhoUsuario(
        @RequestHeader(value = "X-cart-Token", required = false) String cartToken
    )
    {
        return ResponseEntity.ok(carrinhoService.buscarCarrinhoDoUsuario(cartToken));
    }

    @DeleteMapping("/{itemId}/deletar")
    public ResponseEntity<Void> deletarItemCarrinho(
        @PathVariable("itemId") Long itemCarrinhoId,
        @RequestHeader(value = "X-cart-Token", required = false) String cartToken
    ) 
    {
        carrinhoService.deletarItemCarrinho(itemCarrinhoId, cartToken);

        return ResponseEntity.noContent().build();
    }

}
