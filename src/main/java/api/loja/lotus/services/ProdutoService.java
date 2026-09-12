package api.loja.lotus.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import api.loja.lotus.dtos.produto.ProdutoRequestDTO;
import api.loja.lotus.dtos.produto.ProdutoResponseDTO;
import api.loja.lotus.exceptions.BusinessException;
import api.loja.lotus.exceptions.ResourceNotFound;
import api.loja.lotus.mappers.ProdutoMapper;
import api.loja.lotus.models.Produto;
import api.loja.lotus.models.ProdutoImagem;
import api.loja.lotus.models.enums.RoleUser;
import api.loja.lotus.repository.ProdutoImagemRepository;
import api.loja.lotus.repository.ProdutoRepository;
import api.loja.lotus.services.auth.UsuarioAutenticadoService;
import api.loja.lotus.services.storage.SupabaseStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor 
@Slf4j 
@Service 
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;
    private final ProdutoImagemRepository produtoImagemRepository;
    private final SupabaseStorageService supabaseStorageService;
    private final UsuarioAutenticadoService usuarioLogado;

    @Transactional 
    public ProdutoResponseDTO criarProduto(
        ProdutoRequestDTO dto,
        List<MultipartFile> imagens
    ) 
    {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de criar produto!");
        }

        if (produtoRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new BusinessException("Produto existente já com esse nome!");
        }

        Produto produto = ProdutoMapper.toEntity(dto);

        produtoRepository.save(produto);
        
        if (imagens != null && !imagens.isEmpty()) {

            for (MultipartFile imagem : imagens) {
            
                String nomeArquivo = supabaseStorageService.upload(imagem);

                var produtoImagem = new ProdutoImagem();

                produtoImagem.setImagemUrl(nomeArquivo);
                produtoImagem.setProduto(produto);

                produtoImagemRepository.save(produtoImagem);

                log.info("Imagem enviada para o Supabase: {}", nomeArquivo);
            }

        } 

        return ProdutoMapper.toDTO(produto);
    }

    @Transactional 
    public ProdutoResponseDTO adicionarImagensProduto(
        List<MultipartFile> imagens,
        Long produtoId
    ) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de adicionar imagem em produtos!");
        }

        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFound("Produto não encontrado!"));

        for (MultipartFile imagem : imagens) {

            String imagemUrl = supabaseStorageService.upload(imagem);

            ProdutoImagem produtoImagem = new ProdutoImagem();
            produtoImagem.setImagemUrl(imagemUrl);
            produtoImagem.setProduto(produto);

            produtoImagemRepository.save(produtoImagem);

            log.info("Imagem enviada para o Supabase: {}", imagemUrl);
        }

        return ProdutoMapper.toDTO(produto);
    }

    @Transactional 
    public void removerImagemProduto(
        Long produtoId,
        Long imagemId
    ) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de remover imagens!");
        }

        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFound("Produto não encontrado!"));

        ProdutoImagem imagem = produtoImagemRepository.findById(imagemId)
            .orElseThrow(() -> new ResourceNotFound("Imagem não encontrada!"));

        if (!imagem.getProduto().getId().equals(produto.getId())) {
            throw new BusinessException("Essa imagem não pertence ao produto escolhido!");
        }

        supabaseStorageService.delete(imagem.getImagemUrl());

        produtoImagemRepository.delete(imagem);
    }

    @Transactional 
    public ProdutoResponseDTO atualizarProduto(ProdutoRequestDTO dto, Long id) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de atualizar produtos!");
        }

        if (produtoRepository.existsByNomeIgnoreCaseAndIdNot(dto.nome(), id)) {
            throw new BusinessException("Já existe um produto com esse nome!");
        }

        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFound("Produto não encontrado!"));

        verificarProdutoAtivo(produto);

        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());

        produtoRepository.save(produto);

        return ProdutoMapper.toDTO(produto);
    }

    @Transactional 
    public ProdutoResponseDTO ativarProduto(Long produtoId) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de ativar produtos!");
        }

        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFound("Produto não encontrado!"));

        if (produto.isAtivo()) {
            throw new BusinessException("Produto já está ativo!");
        }

        produto.setAtivo(true);

        produtoRepository.save(produto);

        return ProdutoMapper.toDTO(produto);
    }

    @Transactional 
    public ProdutoResponseDTO desativarProduto(Long produtoId) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de desativar produtos!");
        }

        Produto produto = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResourceNotFound("Produto não encontrado!"));

        if (!produto.isAtivo()) {
            throw new BusinessException("Produto já está desativado!");
        }

        produto.setAtivo(false);

        produtoRepository.save(produto);

        return ProdutoMapper.toDTO(produto);
    }

    @Transactional (readOnly = true)
    public Page<ProdutoResponseDTO> buscarTodosProdutosAtivos(Pageable pageable) {

        Page<Produto> produtos = produtoRepository.findAllByAtivo(true, pageable);

        return produtos
            .map(ProdutoMapper::toDTO);
    }

    @Transactional (readOnly = true)
    public Page<ProdutoResponseDTO> buscarTodosProdutosAdmin(Pageable pageable) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de visualizar esses produtos!");
        }

        Page<Produto> produtos = produtoRepository.findAll(pageable);

        return produtos
            .map(ProdutoMapper::toDTO);
    }

    @Transactional (readOnly = true)
    public ProdutoResponseDTO buscarProdutoPorId(Long id) {

        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFound("Produto não encontrado!"));

        return ProdutoMapper.toDTO(produto);
    }

    @Transactional 
    public void excluirProduto(Long id) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de excluir produtos!");
        }

        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFound("Produto não encontrado!"));

        for (ProdutoImagem imagem : produto.getImagens()) {
            
            supabaseStorageService.delete(imagem.getImagemUrl());

            log.info("Imagem de produto {} deletada do bucket s3!", produto.getId());

            produtoImagemRepository.delete(imagem);

            log.info("Imagem de produto {} deletada!", produto.getId());
        }

        produtoRepository.delete(produto);
    }

    private void verificarProdutoAtivo(Produto produto) {

        if (!produto.isAtivo()) {
            throw new BusinessException("Produto está desativado!");
        }
    }

}
