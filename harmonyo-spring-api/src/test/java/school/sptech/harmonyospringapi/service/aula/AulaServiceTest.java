package school.sptech.harmonyospringapi.service.aula;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.harmonyospringapi.domain.*;
import school.sptech.harmonyospringapi.repository.AulaRepository;
import school.sptech.harmonyospringapi.repository.InstrumentoRepository;
import school.sptech.harmonyospringapi.repository.ProfessorRepository;
import school.sptech.harmonyospringapi.service.aula.dto.AulaAtualizacaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaCriacaoDto;
import school.sptech.harmonyospringapi.service.aula.dto.AulaExibicaoDto;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.instrumento.InstrumentoService;
import school.sptech.harmonyospringapi.service.usuario.ProfessorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AulaServiceTest {

    @InjectMocks
    private static AulaService service;

    @Mock
    private static ProfessorService professorService;

    @Mock
    private static InstrumentoService instrumentoService;

    @Mock
    private static AulaRepository repository;





    @DisplayName("Deve retornar uma lista vazia quando não existir aulas")
    @Test
    void devolverListaVaziaQuandoNaoExistirAulas() {
        //when
        when(repository.findAll())
                .thenReturn(List.of());

        //then
        List<AulaExibicaoDto> aulas = service.obterTodos();
        assertTrue(aulas.isEmpty());
    }

    @DisplayName("Deve retornar uma lista com 3 aulas quando existirem 3 aulas")
    @Test
    void devolver3AulasQuandoExistirem3Aulas() {
        //given
        List<Aula> aulas = new ArrayList<>();
        Aula aula = AulaBuilder.criarAula();

        aulas.add(aula);
        aulas.add(aula);
        aulas.add(aula);

        //when
        when(repository.findAll())
                .thenReturn(aulas);

        //then
        List<AulaExibicaoDto> aulasExibicao = service.obterTodos();
        assertEquals(3, aulasExibicao.size());
    }


    @DisplayName("Deve criar uma aula quando AulaCriacaoDto for válido")
    @Test
    void criarUmaAulaQuandoAulaCriacaoDtoForValido(){
        //given
        Aula aula = AulaBuilder.criarAula();
        AulaCriacaoDto dto = AulaBuilder.criarAulaCriacao();

        Mockito.when(repository.save(Mockito.any(Aula.class)))
                .thenReturn(aula);
        Mockito.when(repository.existsByProfessorIdAndInstrumentoId(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(false);

        AulaExibicaoDto resultado = service.cadastrarAula(dto);

        assertNotNull(resultado);
        assertEquals(aula.getId(), resultado.getId());
        assertEquals(aula.getValorAula(), resultado.getValorAula());
        assertEquals(aula.getInstrumento().getId(), resultado.getInstrumento().getId());
    }

    @DisplayName("Deve lançar uma exceção quando AulaId for inválido")
    @Test
    void lancarExcecaoQuandoAulaIdForInvalido(){
        Aula aula = AulaBuilder.criarAula();

        Mockito.when(repository.findById(aula.getId())).thenReturn(Optional.empty());

        assertThrows(EntitadeNaoEncontradaException.class, () -> service.buscarPorId(aula.getId()));
    }

    @DisplayName("Retornar Aula quando AulaId for válido")
    @Test
    void retornarAulaQuandoAulaIdForValido(){
        Aula aula = AulaBuilder.criarAula();

        Mockito.when(repository.findById(aula.getId())).thenReturn(Optional.of(aula));

        Aula aulaRetornada = service.buscarPorId(aula.getId());
        assertEquals(aula, aulaRetornada);
    }

    @DisplayName("Retornar uma lista vazia quando não existe aulas para determinado professor")
    @Test
    void devolverListaVaziaQuandoNaoExistirAulasParaDeterminadoProfessor(){
        List<Aula> aulas = new ArrayList<>();
        Aula aula = AulaBuilder.criarAula();

        aulas.add(aula);
        aulas.add(aula);
        aulas.add(aula);

        Professor professor = new Professor();
        professor.setId(2);

        when(repository.findAll())
                .thenReturn(List.of());

        when(repository.findAll())
                .thenReturn(aulas);

        List<AulaExibicaoDto> aulasExibicaoGeral = service.obterTodos();
        List<AulaExibicaoDto> aulasExibicaoFiltro = service.buscarAulasPorIdProfessor(professor.getId());

        assertTrue(aulasExibicaoFiltro.isEmpty());
        assertEquals(3, aulasExibicaoGeral.size());
    }

    @DisplayName("Retornar uma lista com 2 aulas para determinado professor")
    @Test
    void devolverListaCom2AulasParaDeterminadoProfessor(){
        List<Aula> aulas = new ArrayList<>();
        Aula aula = AulaBuilder.criarAula();

        aulas.add(aula);
        aulas.add(aula);
        aulas.add(aula);

        Professor professor = new Professor();
        professor.setId(2);

        List<Aula> aulasFilto = new ArrayList<>();
        Aula aulaFiltro = AulaBuilder.criarAula(2);

        aulas.add(aulaFiltro);
        aulas.add(aulaFiltro);

        aulasFilto.add(aulaFiltro);
        aulasFilto.add(aulaFiltro);

        when(repository.findAll())
                .thenReturn(aulas);

        when(repository.findAllByProfessorId(professor.getId()))
                .thenReturn(aulasFilto);

        List<AulaExibicaoDto> aulasExibicaoGeral = service.obterTodos();
        List<AulaExibicaoDto> aulasExibicaoFiltro = service.buscarAulasPorIdProfessor(professor.getId());

        assertEquals(5, aulasExibicaoGeral.size());
        assertEquals(2, aulasExibicaoFiltro.size());
    }

    @DisplayName("Deve atualizar a aula quando o id for válido")
    @Test
    void atualizarAulaQuandoIdForVaildo(){
        double valor = 100.0;

        AulaAtualizacaoDto dto = AulaBuilder.criarAulaAtualizacao(valor);

        Aula aula = AulaBuilder.criarAula(1, 50.0);

        Mockito.when(repository.findById(aula.getId()))
                .thenReturn(Optional.of(aula));
        Mockito.when(repository.save(Mockito.any(Aula.class)))
                .thenReturn(aula);

        service.atualizarAulaPorId(aula.getId(), dto);

        double resultado = service.buscarPorId(aula.getId()).getValorAula();

        assertEquals(valor, resultado);
    }

    @DisplayName("Devolver exceção quando atualizar uma aula quando id for inválido")
    @Test
    void devolverExcecaoQuandoAtualizarAulaQuandoIdForInvalido(){
        Double valor = 100.0;

        AulaAtualizacaoDto dto = AulaBuilder.criarAulaAtualizacao(valor);

        EntitadeNaoEncontradaException exception = assertThrows(EntitadeNaoEncontradaException.class,
                () -> service.atualizarAulaPorId(Mockito.anyInt(), dto));

        assertEquals("Aula não encontrada!", exception.getMessage());
    }

    @DisplayName("deletar aula quando o Id for válido")
    @Test
    void deletarAulaQuandoAulaIdForValido(){
        Aula aula1 = AulaBuilder.criarAula(1, 50.0);

        Mockito.when(repository.existsById(aula1.getId()))
                .thenReturn(true);

        service.deletarAulaPorId(aula1.getId());

        Mockito.verify(repository, Mockito.times(1)).deleteById(Mockito.anyInt());
    }

    @DisplayName("Lançar exceção quando deletar aula e o Id for inválido")
    @Test
    void lancarExcecaoQuandoDeletarAulaComIdInvalido(){
        EntitadeNaoEncontradaException exception = assertThrows(EntitadeNaoEncontradaException.class,
                () -> service.deletarAulaPorId(Mockito.anyInt()));

        assertEquals("ID de Aula Inválido. Aula não encontrada !", exception.getMessage());
    }
}

