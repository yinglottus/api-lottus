package api.loja.lotus.services;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.loja.lotus.dtos.ItemCarrinho.ItemCarrinhoRequestDTO;
import api.loja.lotus.dtos.carrinho.CarrinhoResponseDTO;
import api.loja.lotus.exceptions.BusinessException;
import api.loja.lotus.exceptions.ResourceNotFound;
import api.loja.lotus.mappers.CarrinhoMapper;
import api.loja.lotus.models.Carrinho;
import api.loja.lotus.models.ItemCarrinho;
import api.loja.lotus.models.Produto;
import api.loja.lotus.models.Usuario;
import api.loja.lotus.repository.CarrinhoRepository;
import api.loja.lotus.repository.ItemCarrinhoRepository;
import api.loja.lotus.repository.ProdutoRepository;
import api.loja.lotus.services.auth.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor 
@Slf4j 
@Service 
public class CarrinhoService {
    
    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final UsuarioAutenticadoService usuarioLogado;

    @Transactional 
    public CarrinhoResponseDTO adicionarItemCarrinho(
        ItemCarrinhoRequestDTO dto, 
        String cartToken) 
    {

        Carrinho carrinho = obterOuCriarCarrinho(cartToken);

        Produto produto = produtoRepository.findById(dto.produtoId())
            .orElseThrow(() -> new ResourceNotFound("Produto não encontrado!"));

        validarAdicaoDeItens(carrinho, produto);
        BigDecimal subTotal = validarSubTotal(carrinho);

        carrinho.setSubTotal(subTotal);

        return CarrinhoMapper.toDTO(carrinho);
    }

    @Transactional 
    public CarrinhoResponseDTO aumentarQuantidadeItemCarrinho(
        Long itemCarrinhoId,
        String cartToken
    ) 
    {

        Carrinho carrinho = obterCarrinhoExistente(cartToken);

        ItemCarrinho itemCarrinho = itemCarrinhoRepository.findById(itemCarrinhoId)
            .orElseThrow(() -> new ResourceNotFound("Item carrinho não encontrado!"));
        
        if (!itemCarrinho.getCarrinho().getId().equals(carrinho.getId())) {
            throw new BusinessException("Este item não pertence a esse carrinho!");
        }

        itemCarrinho.setQuantidade(itemCarrinho.getQuantidade() + 1);

        BigDecimal subTotal = validarSubTotal(carrinho);

        carrinho.setSubTotal(subTotal);
        itemCarrinhoRepository.save(itemCarrinho);

        return CarrinhoMapper.toDTO(carrinho);
    }

    @Transactional 
    public CarrinhoResponseDTO diminuirQuantidadeItemCarrinho(
        Long itemCarrinhoId, 
        String cartToken) 
    {

        Carrinho carrinho = obterCarrinhoExistente(cartToken);

        ItemCarrinho itemCarrinho = itemCarrinhoRepository.findById(itemCarrinhoId)
            .orElseThrow(() -> new ResourceNotFound("Carrinho não encontrado!"));

        if (!itemCarrinho.getCarrinho().getId().equals(carrinho.getId())) {
            throw new BusinessException("Este item não pertence a esse carrinho!");
        }

        if (itemCarrinho.getQuantidade() == 1) {
            throw new BusinessException("Mínimo de 1 por quantidade!");
        }

        itemCarrinho.setQuantidade(itemCarrinho.getQuantidade() - 1);
        BigDecimal subTotal = validarSubTotal(carrinho);

        carrinho.setSubTotal(subTotal);

        itemCarrinhoRepository.save(itemCarrinho);

        return CarrinhoMapper.toDTO(carrinho);
    }

    @Transactional(readOnly = true)
    public CarrinhoResponseDTO buscarCarrinhoDoUsuario(
        String cartToken
    ) {

        Carrinho carrinho = obterCarrinhoExistente(cartToken);

        return CarrinhoMapper.toDTO(carrinho);
    }

    @Transactional 
    public void deletarItemCarrinho(
        Long itemCarrinhoId,
        String cartToken
    ) 
    {

        Carrinho carrinho = obterCarrinhoExistente(cartToken);

        ItemCarrinho itemCarrinho = itemCarrinhoRepository.findById(itemCarrinhoId)
            .orElseThrow(() -> new ResourceNotFound("Item carrinho não encontrado!"));

        if (!itemCarrinho.getCarrinho().getId().equals(carrinho.getId())) {
            throw new BusinessException("Este item não pertence a esse carrinho!");
        }

        long quantidadeItens = itemCarrinhoRepository.countByCarrinho(carrinho);

        if (quantidadeItens == 1) {
            carrinhoRepository.delete(carrinho);
        }

        itemCarrinhoRepository.delete(itemCarrinho);
    }

    @Transactional 
    public void associarOuMergearCarrinho(String cartToken, Usuario usuario) {

        if (cartToken == null || cartToken.isBlank()) {
            return;
        }

        Optional<Carrinho> carrinhoAnonimoOptional = carrinhoRepository.findByCartTokenAndUsuarioIsNull(cartToken);
        
        if (carrinhoAnonimoOptional.isEmpty())  {
            return;
        }

        Carrinho carrinhoAnonimo = carrinhoAnonimoOptional.get();

        Optional<Carrinho> carrinhoUsuarioOptional = carrinhoRepository.findByUsuario(usuario);

        if (carrinhoUsuarioOptional.isEmpty()) {

            carrinhoAnonimo.setUsuario(usuario);

            carrinhoRepository.save(carrinhoAnonimo);

            return;
        }

        Carrinho carrinhoUsuario = carrinhoUsuarioOptional.get();

        for (ItemCarrinho itemAnonimo : carrinhoAnonimo.getItens()) {
            
            Optional<ItemCarrinho> itemExistente = 
                    carrinhoUsuario.getItens().stream()
                        .filter(item -> item.getProduto().getId()
                        .equals(itemAnonimo.getProduto().getId()))  
                    .findFirst();
            
            if (itemExistente.isPresent()) {

                ItemCarrinho item = itemExistente.get();

                item.setQuantidade(item.getQuantidade() + itemAnonimo.getQuantidade());
            } else {

                itemAnonimo.setCarrinho(carrinhoUsuario);
                carrinhoUsuario.getItens().add(itemAnonimo);
            }
        }

        carrinhoUsuario.setSubTotal(validarSubTotal(carrinhoUsuario));

        carrinhoRepository.save(carrinhoUsuario);

        carrinhoRepository.delete(carrinhoAnonimo);
    }

    private void validarAdicaoDeItens(Carrinho carrinho, Produto produto) {

        Optional<ItemCarrinho> itemExistente = carrinho.getItens().stream()
            .filter(item -> item.getProduto().getId().equals(produto.getId()))
            .findFirst();

        itemExistente.ifPresent(item -> item.setQuantidade(item.getQuantidade() + 1));

        if (itemExistente.isEmpty()) {

            ItemCarrinho itemCarrinho = new ItemCarrinho();

            itemCarrinho.setProduto(produto);
            itemCarrinho.setCarrinho(carrinho);
            itemCarrinho.setQuantidade(1);
            carrinho.getItens().add(itemCarrinho);

            itemCarrinhoRepository.save(itemCarrinho);
        }
    }

    private BigDecimal validarSubTotal(Carrinho carrinho) {

        BigDecimal subTotal = BigDecimal.ZERO;

        for (ItemCarrinho item : carrinho.getItens()) {
            
            subTotal = subTotal.add(item.getProduto().getPreco()
                .multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        return subTotal;
    }
    
    private Carrinho obterCarrinhoExistente(String cartToken) {

        Optional<Usuario> usuario = usuarioLogado.usuarioAtual();

        if (usuario.isPresent()) {

            return carrinhoRepository.findByUsuario(usuario.get())
                .orElseGet(() -> {

                    Carrinho novoCarrinho = new Carrinho();
                    novoCarrinho.setUsuario(usuario.get());
                    
                    return carrinhoRepository.save(novoCarrinho);
                });
        }

        if (cartToken == null || cartToken.isBlank()) {
            return criarCarrinhoAnonimo();
        }

        return carrinhoRepository.findByCartToken(cartToken)
            .orElseGet(this::criarCarrinhoAnonimo);
    }

    private Carrinho obterOuCriarCarrinho(String cartToken) {

        Optional<Usuario> usuario = usuarioLogado.usuarioAtual();

        if (usuario.isPresent()) {

            return carrinhoRepository.findByUsuario(usuario.get())
                .orElseGet(() -> {

                    Carrinho novoCarrinho = new Carrinho();
                    novoCarrinho.setUsuario(usuario.get());
                    
                    return carrinhoRepository.save(novoCarrinho);
                });
        }

        if (cartToken == null || cartToken.isBlank()) {
            return criarCarrinhoAnonimo();
        }

        return carrinhoRepository.findByCartToken(cartToken)
            .orElseThrow(() -> new ResourceNotFound("Carrinho não encontrado!"));
    }

    private Carrinho criarCarrinhoAnonimo() {

        Carrinho novoCarrinho = new Carrinho();

        novoCarrinho.setCartToken(UUID.randomUUID().toString());

        return carrinhoRepository.save(novoCarrinho);
    }

}
