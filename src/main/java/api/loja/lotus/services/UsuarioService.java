package api.loja.lotus.services;

import api.loja.lotus.dtos.usuario.UsuarioRequestDTO;
import api.loja.lotus.dtos.usuario.UsuarioResponseDTO;
import api.loja.lotus.exceptions.UsuarioExistenteException;
import api.loja.lotus.mappers.UsuarioMapper;
import api.loja.lotus.models.Usuario;
import api.loja.lotus.repository.UsuarioRepository;
import api.loja.lotus.services.auth.UsuarioAutenticadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public UsuarioResponseDTO atualizarUsuario() {

        var usuario = usuarioLogado.usuarioLogado();


    }

}
