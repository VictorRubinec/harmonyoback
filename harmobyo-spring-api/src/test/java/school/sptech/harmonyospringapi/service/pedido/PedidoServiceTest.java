package school.sptech.harmonyospringapi.service.pedido;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.harmonyospringapi.domain.*;
import school.sptech.harmonyospringapi.repository.*;
import school.sptech.harmonyospringapi.service.aula.AulaService;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoAlteracaoStatus;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoCriacaoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoMapper;
import school.sptech.harmonyospringapi.service.pedido.hashing.HashTableService;
import school.sptech.harmonyospringapi.service.status.StatusBuilder;
import school.sptech.harmonyospringapi.service.status.StatusService;
import school.sptech.harmonyospringapi.service.usuario.AlunoService;
import school.sptech.harmonyospringapi.service.usuario.ProfessorBuilder;
import school.sptech.harmonyospringapi.service.usuario.ProfessorService;
import school.sptech.harmonyospringapi.service.usuario.UsuarioService;
import school.sptech.harmonyospringapi.utils.PilhaObj;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private AulaRepository aulaRepository;
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AlunoService alunoService;
    @Mock
    private ProfessorService professorService;
    @Mock
    private StatusService statusService;
    @Mock
    private AulaService aulaService;
    @Mock
    private PedidoService pedidoService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private HashTableService hashTableService;

    @InjectMocks
    private PedidoService service;


    @DisplayName("Deve retornar 0 quando não houver pedidos")
    @Test
    void retornaZeroQuandoNaoHouverPedido(){

        Mockito.when(repository.findAll())
                .thenReturn(List.of());

        List<PedidoExibicaoDto> pedidos = service.obterTodos();

        assertTrue(pedidos.isEmpty());
    }

    @DisplayName("Deve retornar 3 pedidos quando acionado obterTodos")
    @Test
    void retornarTresPedidosQuandoAcionarObterTodos(){
        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());

        List<Pedido> pedidos = new ArrayList<>();

        pedidos.add(pedido);
        pedidos.add(pedido);
        pedidos.add(pedido);

        Mockito.when(repository.findAll())
                .thenReturn(pedidos);

        List<PedidoExibicaoDto> resultado = service.obterTodos();

        assertEquals(pedidos.size(), resultado.size());

    }

    @DisplayName("Retornar pilha vazia quando acionado obterPedidosPendentes e não há pedidos pendentes")
    @Test
    void retonarPilhaVaziaQuandoAcionadoObterPedidosPendentesSemPedidosPendentes(){

        Professor professor = ProfessorBuilder.criarProfessor(1);

        Mockito.when(repository.encontrarPedidosPendentesPorIdProfessor(Mockito.anyInt()))
                .thenReturn(List.of());

        PilhaObj<PedidoExibicaoDto> resultado = service.obterPedidosPendentes(1);

        assertTrue(resultado.isEmpty());
    }

    @DisplayName("Retornar pilha com três pedidos quando acionado obterPedidos pendentes")
    @Test
    void retornarPilhaComTresPedidosQuandoAcionadoObterPedidosPendentes(){
        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());

        List<Pedido> pedidos = new ArrayList<>();

        pedidos.add(pedido);
        pedidos.add(pedido);
        pedidos.add(pedido);

        Mockito.when(repository.encontrarPedidosPendentesPorIdProfessor(pedido.getProfessor().getId()))
                .thenReturn(pedidos);

        PilhaObj<PedidoExibicaoDto> resultado = service.obterPedidosPendentes(pedido.getProfessor().getId());

        assertEquals(pedidos.size(), resultado.getTopo()+1);
    }

    /*@DisplayName("Criar pedido quando acionado criar quando PedidoCriacaoDto estiver válido")
    @Test
    void criarPedidoQuandoAcionadoCriarQuandoPedidoCriacaoDtoEstiverValido(){


        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());
        pedido.getStatus().setDescricao("Pendente");

        PedidoCriacaoDto dto = PedidoBuilder.criarPedidoCriacaoDto();


        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(PedidoMapper.of(dto, pedido.getAluno(), pedido.getProfessor(), pedido.getStatus(), pedido.getAula()));

        PedidoExibicaoDto resultado = service.criar(dto);

        assertNotNull(resultado);
        assertEquals(PedidoBuilder.getData(), resultado.getDataAula());
        assertEquals(1, resultado.getAluno().getId());
        assertEquals(2, resultado.getProfessor().getId());
        assertEquals(1, resultado.getAula().getId());
    }*/

    @DisplayName("Retornar pedido quando buscarPorId com Id válido")
    @Test
    void retornarPedidoQuandoBuscarPorIdComIdValido(){
        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());

        Mockito.when(repository.findById(pedido.getId()))
                .thenReturn(Optional.of(pedido));

        Pedido resultado = service.buscarPorId(pedido.getId());

        assertNotNull(resultado);
        assertEquals(pedido.getAluno(), resultado.getAluno());
        assertEquals(pedido.getProfessor(), resultado.getProfessor());
        assertEquals(pedido.getAula(), resultado.getAula());
    }

    @DisplayName("Lançar exceção quando buscarPorId com Id inválido")
    @Test
    void lancarExcecaoQuandoBuscarPorIdComIdInvalido(){
        EntitadeNaoEncontradaException exception = assertThrows(EntitadeNaoEncontradaException.class, ()
                -> service.buscarPorId(999));

        assertEquals("Pedido não encontrado", exception.getMessage());
    }

//    @DisplayName("Atualizar status do pedido para confirmado quando acionado aceitarPedido com dados válidos")
//    @Test
//    void atualizarStatusDoPedidoQuandoAcionadoAceitarPedidoComDadosValidos(){
//        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus(1, "Pendente"));
//
//        Status status = StatusBuilder.criarStatus(5, "Confirmado");
//
//        Mockito.when(repository.findById(Mockito.anyInt()))
//                .thenReturn(Optional.of(pedido));
//        Mockito.when(statusService.buscarPorDescricao("Confirmado"))
//                .thenReturn(status);
//        Mockito.when(repository.save(Mockito.any()))
//                .thenReturn(pedido);
//
//        PedidoExibicaoDto resultado = service.aceitarPropostaDoAluno(pedido.getId());
//
//        assertNotNull(resultado);
//        assertEquals(pedido.getId(), resultado.getId());
//        assertEquals("Confirmado", resultado.getStatus().getDescricao());
//    }

    @DisplayName("Lançar exceção quando acionado aceitarPedido com pedido Id inválido")
    @Test
    void lancarExcecaoQuandoAcionadoAceitarPedidoComPedidoIdInvalido(){
        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());
        PedidoAlteracaoStatus pedidoAlteracaoStatus = new PedidoAlteracaoStatus();
        pedidoAlteracaoStatus.setIdPedido(pedido.getId());
        pedidoAlteracaoStatus.setIdUsuario(pedido.getAluno().getId());
        pedidoAlteracaoStatus.setMotivo("");


        EntitadeNaoEncontradaException exception = assertThrows(EntitadeNaoEncontradaException.class, ()
                -> service.aceitarPropostaDoAluno(pedidoAlteracaoStatus));

        assertEquals("Pedido não encontrado", exception.getMessage());
    }

//    @DisplayName("Atualizar status para recusado do pedido quando acionado recusarPedido com dados válidos")
//    @Test
//    void atualizarStatusDoPedidoQuandoAcionadoRecusarPedidoComDadosValidos(){
//        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());
//        pedido.getStatus().setDescricao("Pendente");
//
//        Status status = new Status();
//        status.setId(7);
//        status.setDescricao("Recusado");
//
//        Mockito.when(repository.findById(pedido.getId()))
//                .thenReturn(Optional.of(pedido));
//        Mockito.when(statusService.buscarPorDescricao("Recusado"))
//                .thenReturn(status);
//        Mockito.when(repository.save(Mockito.any()))
//                .thenReturn(pedido);
//
//        PedidoExibicaoDto resultado = service.recusarPropostaDoAluno(pedido.getId());
//
//        assertNotNull(resultado);
//        assertEquals(pedido.getId(), resultado.getId());
//        assertEquals("Recusado", resultado.getStatus().getDescricao());
//    }

    @DisplayName("Lançar exceção quando acionado recusarPedido com pedido Id inválido")
    @Test
    void lancarExcecaoQuandoAcionadoRecusarPedidoComPedidoIdInvalido(){
        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());
        PedidoAlteracaoStatus pedidoAlteracaoStatus = new PedidoAlteracaoStatus();
        pedidoAlteracaoStatus.setIdPedido(pedido.getId());
        pedidoAlteracaoStatus.setIdUsuario(pedido.getAluno().getId());
        pedidoAlteracaoStatus.setMotivo("");

        EntitadeNaoEncontradaException exception = assertThrows(EntitadeNaoEncontradaException.class, ()
                -> service.recusarPropostaDoAluno(pedidoAlteracaoStatus));

        assertEquals("Pedido não encontrado", exception.getMessage());
    }

    @DisplayName("Retonar lista vazia quando acionado buscarPorUsuarioId quando usuário for válido e não tem pedidos")
    @Test
    void retonarListaVaziaQuandoAcionadoBuscarPorUsuarioIdQuandoUsuarioForValidoENaoTemPedidos(){
        Usuario usuario = new Aluno();
        usuario.setId(1);

        Mockito.when(usuarioService.buscarPorId(Mockito.anyInt())).
                thenReturn(usuario);
        Mockito.when(repository.buscarPorUsuarioId(usuario.getId()))
                        .thenReturn(List.of());

        List<PedidoExibicaoDto> resultado = service.buscarPorUsuarioId(usuario.getId());

        assertTrue(resultado.isEmpty());
    }

    @DisplayName("Retonar lista com 3 pedidos quando acionado buscarPorUsuarioId quando usuário for válido")
    @Test
    void retonarListaComTresPedidosQuandoAcionadoBuscarPorUsuarioIdQuandoUsuarioForValido(){
        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());
        List<Pedido> pedidos = new ArrayList<>();

        pedidos.add(pedido);
        pedidos.add(pedido);
        pedidos.add(pedido);

        Mockito.when(usuarioService.buscarPorId(Mockito.anyInt())).
                thenReturn(pedido.getAluno());
        Mockito.when(repository.buscarPorUsuarioId(pedido.getAluno().getId()))
                .thenReturn(pedidos);

        List<PedidoExibicaoDto> resultado = service.buscarPorUsuarioId(pedido.getAluno().getId());

        assertNotNull(resultado);
        assertEquals(pedidos.size(), resultado.size());
    }

    @DisplayName("Retornar PedidoExibicaoDto quando acionado buscarPorIdParaExibicao com Id válido")
    @Test
    void retornarPedidoExibicaoDtoQuandoAcionadoBuscarPorIdParaExibicaoComIdValido(){
        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());

        Mockito.when(repository.findById(Mockito.anyInt()))
                        .thenReturn(Optional.of(pedido));

        PedidoExibicaoDto resultado = service.buscarPorIdParaExibicao(pedido.getId());

        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
    }

//    @DisplayName("Retonar pedido atualizado quando acionado aceitarPropostaDoAluno")
//    @Test
//    void retornarPedidoAtualizadoQuandoAcionadoAtualizarStatus(){
//        Pedido pedido = PedidoBuilder.criarPedido(StatusBuilder.criarStatus());
//
//        String status = "Aguardando Pagamento";
//
//        Mockito.when(repository.save(pedido))
//                .thenReturn(pedido);
//
//        Pedido resultado = service.atualizarStatus(pedido, status);
//
//        assertNotNull(resultado);
//        assertEquals("Aguardando Pagamento", resultado.getStatus().getDescricao());
//    }


}