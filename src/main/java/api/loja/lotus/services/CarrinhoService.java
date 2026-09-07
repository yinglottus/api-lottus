package api.loja.lotus.services;

import org.springframework.stereotype.Service;

import api.loja.lotus.repository.CarrinhoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor 
@Slf4j 
@Service 
public class CarrinhoService {
    
    private final CarrinhoRepository carrinhoRepository;

    

}
