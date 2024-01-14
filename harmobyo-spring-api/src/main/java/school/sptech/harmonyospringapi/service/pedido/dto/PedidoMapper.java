package school.sptech.harmonyospringapi.service.pedido.dto;

import school.sptech.harmonyospringapi.domain.*;
import school.sptech.harmonyospringapi.service.aula.dto.AulaMapper;
import school.sptech.harmonyospringapi.service.status.dto.StatusMapper;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioMapper;

import java.time.LocalDateTime;

public class PedidoMapper {

    public static Pedido of(PedidoCriacaoDto pedidoCriacaoDto, Aluno aluno, Professor professor, Status status, Aula aula) {

        Pedido pedido = new Pedido();
        pedido.setAluno(aluno);
        pedido.setProfessor(professor);
        pedido.setStatus(status);
        pedido.setAula(aula);
        pedido.setDataAula(pedidoCriacaoDto.getDataAula());
        pedido.setHoraCriacao(LocalDateTime.now());
        pedido.setValorAula(aula.getValorAula());
        return pedido;
    }

    public static PedidoExibicaoDto ofPedidoExibicaoDto(Pedido pedido) {
        PedidoExibicaoDto pedidoExibicaoDto = new PedidoExibicaoDto();
        pedidoExibicaoDto.setId(pedido.getId());
        pedidoExibicaoDto.setAluno(UsuarioMapper.ofUsuarioExibicao(pedido.getAluno()));
        pedidoExibicaoDto.setProfessor(UsuarioMapper.ofUsuarioExibicao(pedido.getProfessor()));
        pedidoExibicaoDto.setStatus(StatusMapper.ofStatusExibicaoDto(pedido.getStatus()));
        pedidoExibicaoDto.setAula(AulaMapper.ofAulaExibicaoDto(pedido.getAula()));
        pedidoExibicaoDto.setDataAula(pedido.getDataAula());
        pedidoExibicaoDto.setHoraCriacao(pedido.getHoraCriacao());
        pedidoExibicaoDto.setHoraResposta(pedido.getHoraResposta());
        pedidoExibicaoDto.setValorAula(pedido.getValorAula());

        return pedidoExibicaoDto;
    }
}
