package school.sptech.harmonyospringapi.service.usuario.autenticacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.Usuario;
import school.sptech.harmonyospringapi.repository.UsuarioRepository;
import school.sptech.harmonyospringapi.service.usuario.autenticacao.dto.UsuarioDetalhesDto;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired(required = true)
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> usuarioOpt = this.usuarioRepository.findByEmail(username);

        if (usuarioOpt.isEmpty()){
            throw new UsernameNotFoundException(String.format("Usuário: %s não encontrado", username));
        }

        return new UsuarioDetalhesDto(usuarioOpt.get());

    }
}
