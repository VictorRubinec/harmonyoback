package school.sptech.harmonyospringapi.service.endereco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.Endereco;
import school.sptech.harmonyospringapi.domain.Usuario;
import school.sptech.harmonyospringapi.repository.EnderecoRepository;
import school.sptech.harmonyospringapi.repository.UsuarioRepository;
import school.sptech.harmonyospringapi.service.endereco.dto.EnderecoMapper;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioMapper;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Endereco cadastrarEndereco(Endereco endereco){
        return  enderecoRepository.save(endereco);
    }

    public Endereco atualizarEndereco(Endereco endereco){
        return enderecoRepository.save(endereco);
    }

    public void deletarEndereco(Endereco endereco){
        enderecoRepository.delete(endereco);
    }

    public List<Endereco> listarEnderecos(){
        return this.enderecoRepository.findAll();
    }

    public Endereco buscarPorId(Integer id){
        if(id <= 0)
            throw new RuntimeException("Id inválido");

        return enderecoRepository.findById(id).orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
    }

    public List<String> listarCidades() {
        	return this.enderecoRepository.listarCidades();
    }

    public void atualizarDadosEnderecoDeUsuario(int idUsuario, Endereco novoEndereco){

        Optional<Usuario> usuario = this.usuarioRepository.findById(idUsuario);

        if (usuario.isPresent()){

            Usuario usuarioEncontrado = usuario.get();

            novoEndereco.setId(usuarioEncontrado.getEndereco().getId());
            Endereco enderecoSalvo = this.enderecoRepository.save(novoEndereco);

            usuarioEncontrado.setEndereco(enderecoSalvo);

            this.usuarioRepository.save(usuarioEncontrado);
        }
        else {
            throw new EntitadeNaoEncontradaException("ID de Usuário Inválido !");
        }
    }
}
