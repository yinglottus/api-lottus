package api.loja.lotus.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.loja.lotus.dtos.cupom.CupomAdicionarDTO;
import api.loja.lotus.dtos.cupom.CupomRequestDTO;
import api.loja.lotus.dtos.cupom.CupomResponseDTO;
import api.loja.lotus.exceptions.BusinessException;
import api.loja.lotus.exceptions.ResourceNotFound;
import api.loja.lotus.models.Carrinho;
import api.loja.lotus.models.Cupom;
import api.loja.lotus.models.enums.RoleUser;
import api.loja.lotus.repository.CarrinhoRepository;
import api.loja.lotus.repository.CupomRepository;
import api.loja.lotus.services.auth.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RequiredArgsConstructor 
@Service
public class CupomService {
    
    private final CupomRepository cupomRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoService carrinhoService;
    private final UsuarioAutenticadoService usuarioLogado;

    @Transactional 
    public CupomResponseDTO criarCupom(CupomRequestDTO dto) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não é admin!");
        }

        if (cupomRepository.existsByCodigoAndQuantidadeGreaterThan(dto.codigo(), 0)) {
            throw new BusinessException("Já existe um cupom com este código ativo!");
        }

        Cupom cupom = Cupom.criarCupom(dto);    
        cupomRepository.save(cupom);

        return CupomResponseDTO.from(cupom);
    }

    @Transactional
    public CupomResponseDTO aplicarDescontoCarrinho(
        String cartToken,
        CupomAdicionarDTO dto
    ) 
    {

        Carrinho carrinho = carrinhoService.obterCarrinhoExistente(cartToken);

        if (carrinho.getCupom() != null) {
            throw new BusinessException("Este carrinho já possui cupom!");
        }

        Cupom cupom = cupomRepository.findByCodigo(dto.codigo())
            .orElseThrow(() -> new ResourceNotFound("Cupom não encontrado!"));

        int linhasAfetadas = cupomRepository.decrementarQuantidadeSeDisponivel(cupom.getId());

        if (linhasAfetadas == 0) {
            throw new BusinessException("Cupom expirado!");
        }

        cupom.setQuantidade(cupom.getQuantidade() - 1);

        BigDecimal precoDescontado = aplicarDesconto(carrinho.getSubTotal(), cupom.getDesconto());

        carrinho.setSubTotalDescontado(precoDescontado);
        carrinho.setCupom(cupom);

        carrinhoRepository.save(carrinho);

        return CupomResponseDTO.from(cupom);
    }

    @Transactional
    public CupomResponseDTO atualizarCupom(CupomRequestDTO dto, Long cupomId) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de atualizar cupons!");
        }

        Cupom cupom = cupomRepository.findById(cupomId)
            .orElseThrow(() -> new ResourceNotFound("Cupom não encontrado!"));

        cupom.atualizarCupom(dto);

        cupomRepository.save(cupom);

        return CupomResponseDTO.from(cupom);
    }

    @Transactional(readOnly = true)
    public Page<CupomResponseDTO> buscarTodosCupons(Pageable pageable) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de visualizar cupons!");
        }

        Page<Cupom> cupons = cupomRepository.findAll(pageable);

        return cupons
            .map(CupomResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public CupomResponseDTO buscarCupomMeuCarrinho(String cartToken) 
    {

        Carrinho carrinho = carrinhoService.obterCarrinhoExistente(cartToken);

        if (carrinho.getCupom() == null) {
            throw new ResourceNotFound("Carrinho está sem cupom!");
        }

        Cupom cupom = carrinho.getCupom();

        return CupomResponseDTO.from(cupom);
    }

    @Transactional(readOnly = true)
    public CupomResponseDTO buscarCupomPorId(Long cupomId) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de ver esse cupom!");
        }

        Cupom cupom = cupomRepository.findById(cupomId)
            .orElseThrow(() -> new ResourceNotFound("Cupom não encontrado!"));

        return CupomResponseDTO.from(cupom);
    }

    @Transactional
    public void deletarCupom(Long cupomId) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de excluir esse cupom!");
        }

        Cupom cupom = cupomRepository.findById(cupomId)
            .orElseThrow(() -> new ResourceNotFound("Cupom não encontardo!"));

        carrinhoRepository.removerCupomDosCarrinhos(cupomId);
        
        cupomRepository.delete(cupom);
    }

    public BigDecimal aplicarDesconto(BigDecimal preco, BigDecimal percentual) {

        BigDecimal fator = percentual.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal desconto = preco.multiply(fator);
        return preco = preco.subtract(desconto).setScale(2, RoundingMode.HALF_UP);
    }

}
