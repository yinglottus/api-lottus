package api.loja.lotus.services;

import api.loja.lotus.dtos.usuario.UsuarioAtualizarDTO;
import api.loja.lotus.dtos.usuario.UsuarioDeletarDTO;
import api.loja.lotus.dtos.usuario.UsuarioRequestDTO;
import api.loja.lotus.dtos.usuario.UsuarioResponseDTO;
import api.loja.lotus.exceptions.BusinessException;
import api.loja.lotus.exceptions.ResourceNotFound;
import api.loja.lotus.exceptions.SenhaIncorretaException;
import api.loja.lotus.exceptions.UsuarioExistenteException;
import api.loja.lotus.mappers.UsuarioMapper;
import api.loja.lotus.models.Usuario;
import api.loja.lotus.models.enums.RoleUser;
import api.loja.lotus.repository.UsuarioRepository;
import api.loja.lotus.services.auth.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioAutenticadoService usuarioLogado;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new UsuarioExistenteException("Usuário já existente com esse email!");
        }

        var usuario = UsuarioMapper.toEntity(dto);

        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        usuarioRepository.save(usuario);
        log.info("Usuário {} criado com sucesso!", usuario.getEmail());

        return UsuarioMapper.toDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(UsuarioAtualizarDTO dto) {

        var usuario = usuarioLogado.usuarioLogado();

        if (dto.nome() != null && !dto.nome().isBlank()) {
            usuario.setNome(dto.nome());
        }

        if (dto.senhaNova() == null || dto.senhaNova().isBlank()) {
            return UsuarioMapper.toDTO(usuario);
        }

        validarSenha(dto.senhaAtual(), usuario);

        usuario.setSenha(passwordEncoder.encode(dto.senhaNova()));

        usuarioRepository.save(usuario);

        return UsuarioMapper.toDTO(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> buscarTodosUsuarios(
            Pageable pageable)
    {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem permissão de visualizar usuarios!");
        }

        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);

        return usuarios
                .map(UsuarioMapper::toDTO);
    }

    public UsuarioResponseDTO buscarMeusDados() {

        var usuario = usuarioLogado.usuarioLogado();

        return UsuarioMapper.toDTO(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarUsuarioPorId(Long id) {

        var usuario = usuarioLogado.usuarioLogado();

        if (usuario.getRole() != RoleUser.ROLE_ADMIN) {
            throw new BusinessException("Você não tem acesso para visualizar usuários!");
        }

        Usuario usuarioBuscado = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Usuário não encontrado!"));

        return UsuarioMapper.toDTO(usuarioBuscado);
    }

    @Transactional
    public void excluirUsuario(UsuarioDeletarDTO dto) {

        var usuario = usuarioLogado.usuarioLogado();

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new SenhaIncorretaException("Senha incorreta!");
        }

        usuarioRepository.delete(usuario);
    }

    private void validarSenha(String senha, Usuario usuario) {

        if (senha == null || senha.isBlank()) {
            throw new BusinessException("Informe sua senha atual!");
        }

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new SenhaIncorretaException("Senha incorreta!");
        }

    }

}
