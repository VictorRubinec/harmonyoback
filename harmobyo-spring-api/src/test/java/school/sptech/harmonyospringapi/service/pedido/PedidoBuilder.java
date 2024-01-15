package school.sptech.harmonyospringapi.service.pedido;

import school.sptech.harmonyospringapi.domain.*;
import school.sptech.harmonyospringapi.service.aula.AulaBuilder;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.AlunoBuilder;
import school.sptech.harmonyospringapi.service.usuario.ProfessorBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PedidoBuilder {

    public static PedidoCriacaoDto criarPedidoCriacaoDto() {
        PedidoCriacaoDto pedidoCriacaoDto = new PedidoCriacaoDto();
        pedidoCriacaoDto.setAlunoId(AlunoBuilder.criarAluno().getId());
        pedidoCriacaoDto.setProfessorId(ProfessorBuilder.criarProfessor().getId());
        pedidoCriacaoDto.setAulaId(AulaBuilder.getId());
        pedidoCriacaoDto.setDataAula(PedidoBuilder.getData());
        return pedidoCriacaoDto;
    }

    public static Pedido criarPedido(Status status) {
        Pedido pedido = new Pedido();
        pedido.setId(1);

        Aluno aluno = AlunoBuilder.criarAluno();
        pedido.setAluno(aluno);

        Professor professor = ProfessorBuilder.criarProfessor();
        pedido.setProfessor(professor);

        pedido.setStatus(status);

        Aula aula = AulaBuilder.criarAula();
        pedido.setAula(aula);

        return pedido;
    }

    public static Pedido criarPedido(int id, Aluno aluno, Professor professor, Status status, Aula aula){
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setAluno(aluno);
        pedido.setProfessor(professor);
        pedido.setStatus(status);
        pedido.setAula(aula);

        return pedido;
    }

    public static LocalDateTime getData() {
        return LocalDateTime.parse("2018-07-22 10:35:10",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
