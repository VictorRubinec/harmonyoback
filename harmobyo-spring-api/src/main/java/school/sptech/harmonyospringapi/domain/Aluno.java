package school.sptech.harmonyospringapi.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import school.sptech.harmonyospringapi.service.pedido.PedidoService;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoPilhaDto;
import school.sptech.harmonyospringapi.service.usuario.AlunoService;
import school.sptech.harmonyospringapi.service.usuario.ProfessorService;
import school.sptech.harmonyospringapi.utils.PilhaObj;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Aluno")
public class Aluno extends Usuario{

    public PilhaObj<PedidoExibicaoPilhaDto> getHistorico() {
        PilhaObj<PedidoExibicaoPilhaDto> historico = new PilhaObj<>(AlunoService.MAX_AULAS);
        List<Pedido> listinha = new ArrayList<>();

        //criacao de objetos para teste
        Pedido pedido = new Pedido();
        Aula aula = new Aula();
        Instrumento instrumento = new Instrumento();
        Naipe naipe = new Naipe();
        Professor professor = new Professor();

        //setando valores para teste
        instrumento.setNome("Violão");
        naipe.setDescricao("corda");
        professor.setNome("o tal do brabo");

        aula.setProfessor(professor);
        instrumento.setNaipe(naipe);
        aula.setInstrumento(instrumento);


        pedido.setDataAula(LocalDateTime.now());
        pedido.setAula(aula);
       /*
        pedidos.sort((p1, p2) -> p1.getDataAula().compareTo(p2.getDataAula()));
        for(int i = 0; i < AlunoService.MAX_AULAS; i++){

            historico.push(new PedidoExibicaoPilhaDto(pedidos.get(0)));

        }*/
        historico.push(new PedidoExibicaoPilhaDto(pedido));

        return historico;


    }


}
