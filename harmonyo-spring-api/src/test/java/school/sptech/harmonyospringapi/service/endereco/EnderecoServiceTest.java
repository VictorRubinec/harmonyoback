package school.sptech.harmonyospringapi.service.endereco;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.harmonyospringapi.domain.Endereco;
import school.sptech.harmonyospringapi.repository.EnderecoRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {

    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Test
    void salvarEnderecoValido(){
        Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(EnderecoBuilder.criarEndereco());
        assertNotNull(enderecoService.cadastrarEndereco(new Endereco()));
    }

    @Test
    void salvarEnderecoInvalido(){
        Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(null);
        assertNull(enderecoService.cadastrarEndereco(EnderecoBuilder.criarEndereco()));

    }

    @Test
    void atualizarEnderecoValido(){
        Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(new Endereco());
        assertNotNull(enderecoService.atualizarEndereco(EnderecoBuilder.criarEndereco()));

    }

    @Test
    void atualizarEnderecoInvalido(){
        Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(null);
        assertNull(enderecoService.atualizarEndereco(EnderecoBuilder.criarEndereco()));

    }

    @Test
    void buscarEnderecoPorIdValido(){

        Mockito.when(enderecoRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(EnderecoBuilder.criarEndereco()));

        assertNotNull(enderecoService.buscarPorId(1));

    }

    @Test
    void devolverExcecaoQuandoBuscarEnderecoIdInvalidoOuNegativo(){
        assertThrows(RuntimeException.class, () -> enderecoService.buscarPorId(-1));
        assertThrows(RuntimeException.class, () -> enderecoService.buscarPorId(0));

    }

    @Test
    void devolver3EnderecosQuandoListarEnderecos(){
        Mockito.when(enderecoRepository.findAll()).thenReturn(java.util.List.of(EnderecoBuilder.criarEndereco(), EnderecoBuilder.criarEndereco(), EnderecoBuilder.criarEndereco()));
        assertEquals(3, enderecoService.listarEnderecos().size());
    }
}
