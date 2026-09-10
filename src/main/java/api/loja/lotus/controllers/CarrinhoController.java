package api.loja.lotus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
