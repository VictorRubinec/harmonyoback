package school.sptech.harmonyospringapi.service.fila_de_espera;

/*import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.harmonyospringapi.service.exceptions.FilaVaziaException;
<<<<<<< HEAD
import school.sptech.harmonyospringapi.service.fila_de_espera.dto.AlunoFilaDeEsperaDTO;
import school.sptech.harmonyospringapi.utils.FilaObj;*/

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
=======
//import school.sptech.harmonyospringapi.service.fila_de_espera.dto.AlunoFilaDeEsperaDTO;
import school.sptech.harmonyospringapi.utils.FilaObj;
>>>>>>> 73b462e58ed86726b0248336acc8f0147c43d59a

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlunoFilaDeEsperaServiceTest {
<<<<<<< HEAD
/*

    @Mock
    AlunoFilaDeEsperaDTO alunoDTO;

    @Mock
    FilaObj<AlunoFilaDeEsperaDTO> fila;

    @Mock
    AlunoFilaDeEsperaService service;
    @Test
    public void quandoAcionadoOPrimeiroAlunoDaFilaDeveEstarInteressadoEmViolino() {


        Mockito.when(service.pollAluno(Mockito.anyInt())).thenReturn(
            new AlunoFilaDeEsperaDTO(1, "João", "violino"));




        assertEquals("violino", service.pollAluno(1).getAlunoInstrumento());
    }

    @Test
    public void quandoAcionadoDeveRetornarFilaVaziaException() {

        Mockito.when(service.pollAluno(Mockito.anyInt())).thenThrow(FilaVaziaException.class);

        assertThrows(FilaVaziaException.class, () -> service.pollAluno(1));
    }
*/
=======
//
//    @Mock
//    AlunoFilaDeEsperaDTO alunoDTO;
//
//    @Mock
//    FilaObj<AlunoFilaDeEsperaDTO> fila;
//
//    @Mock
//    AlunoFilaDeEsperaService service;
//    @Test
//    public void quandoAcionadoOPrimeiroAlunoDaFilaDeveEstarInteressadoEmViolino() {
//
//
//        Mockito.when(service.pollAluno(Mockito.anyInt())).thenReturn(
//            new AlunoFilaDeEsperaDTO(1, "João", "violino"));
//
//
//
//
//        assertEquals("violino", service.pollAluno(1).getAlunoInstrumento());
//    }
//
//    @Test
//    public void quandoAcionadoDeveRetornarFilaVaziaException() {
//
//        Mockito.when(service.pollAluno(Mockito.anyInt())).thenThrow(FilaVaziaException.class);
//
//        assertThrows(FilaVaziaException.class, () -> service.pollAluno(1));
//    }
>>>>>>> 73b462e58ed86726b0248336acc8f0147c43d59a

}
