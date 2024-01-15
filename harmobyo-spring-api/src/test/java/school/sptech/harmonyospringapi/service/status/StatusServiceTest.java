package school.sptech.harmonyospringapi.service.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.harmonyospringapi.domain.Status;
import school.sptech.harmonyospringapi.repository.StatusRepository;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusService statusService;

    @Test
    void devolverExcecaoQuandoBuscarPorIdInvalido() {

        Mockito.when(statusRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(EntitadeNaoEncontradaException.class, () -> statusService.buscarPorId(0));

    }

    @Test
    void devolverStatusQuandoBuscarPorIdValido() {

        Mockito.when(statusRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new Status()));

        Status status = statusService.buscarPorId(1);

        assertNotNull(status);

    }

    @Test
    void devolverExcecaoQuandoBuscarPorDescricaoInvalida() {

        Mockito.when(statusRepository.findByDescricao(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(EntitadeNaoEncontradaException.class, () -> statusService.buscarPorDescricao(""));

    }

    @Test
    void devolverStatusQuandoBuscarPorDescricaoValida() {

        Mockito.when(statusRepository.findByDescricao(Mockito.anyString())).thenReturn(Optional.of(new Status()));

        Status status = statusService.buscarPorDescricao("Concluído");

        assertNotNull(status);

    }

}
