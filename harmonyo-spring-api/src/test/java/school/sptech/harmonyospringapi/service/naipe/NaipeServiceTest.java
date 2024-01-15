package school.sptech.harmonyospringapi.service.naipe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.domain.Naipe;
import school.sptech.harmonyospringapi.repository.NaipeRepository;
import school.sptech.harmonyospringapi.service.exceptions.EntidadeConflitanteException;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoCriacaoDto;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeCriacaoDto;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeExibicaoDto;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class NaipeServiceTest {
    @Mock
    private NaipeRepository naipeRepository;

    @InjectMocks
    private NaipeService naipeService;

    @Test
    public void quandoAcionadoComIdInvalidoDeveRetornarEntidadeNaoEncontradaException() {
        // Arrange
        Mockito.when(naipeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntitadeNaoEncontradaException.class, ()
                -> naipeService.buscarPorId(999));
    }

    @Test
    public void quandoCadastradoCorretamenteDeveSalvarNaRepository() {
        NaipeCriacaoDto naipeCriacaoDto = NaipeBuilder.criarNaipeCriacaoDto("Naipe teste");
        Naipe naipe = NaipeBuilder.criarNaipe(1, "Naipe teste");

        Mockito.when(naipeRepository.save(Mockito.any(Naipe.class))).thenReturn(naipe);

        NaipeExibicaoDto naipeCriado = naipeService.cadastrar(naipeCriacaoDto);

        Mockito.verify(naipeRepository, times(1)).save(Mockito.any(Naipe.class));
        assertEquals(naipeCriado.getId(), naipe.getId());
        assertEquals(naipeCriado.getDescricaoNaipe(), naipe.getDescricao());

    }



}