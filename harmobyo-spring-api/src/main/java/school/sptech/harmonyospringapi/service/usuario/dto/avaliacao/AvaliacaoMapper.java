package school.sptech.harmonyospringapi.service.usuario.dto.avaliacao;

import school.sptech.harmonyospringapi.domain.Avaliacao;
import school.sptech.harmonyospringapi.domain.Pedido;
import school.sptech.harmonyospringapi.domain.Usuario;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoMapper;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AvaliacaoMapper {

    public static Avaliacao of(AvaliacaoCriacaoDto avaliacaoCriacaoDto, Usuario usuarioAvaliado,
                               Usuario usuarioAvaliador, Pedido pedido) {

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setValor(avaliacaoCriacaoDto.getValor());
        avaliacao.setComentario(avaliacaoCriacaoDto.getComentario());
        avaliacao.setUsuarioAvaliado(usuarioAvaliado);
        avaliacao.setDataAvaliacao(LocalDate.now());
        avaliacao.setUsuarioAvaliador(usuarioAvaliador);
        avaliacao.setPedidoAula(pedido);

        return avaliacao;
    }

    public static AvaliacaoExibicaoDto ofAvaliacaoExibicao(Avaliacao avaliacao) {
        AvaliacaoExibicaoDto avaliacaoExibicaoDto = new AvaliacaoExibicaoDto();

        avaliacaoExibicaoDto.setId(avaliacao.getId());
        avaliacaoExibicaoDto.setValor(avaliacao.getValor());
        avaliacaoExibicaoDto.setComentario(avaliacao.getComentario());
        avaliacaoExibicaoDto.setDataAvaliacao(avaliacao.getDataAvaliacao());
        avaliacaoExibicaoDto.setUsuarioAvaliador(UsuarioMapper.ofUsuarioExibicao(avaliacao.getUsuarioAvaliador()));
        avaliacaoExibicaoDto.setPedidoAula(PedidoMapper.ofPedidoExibicaoDto(avaliacao.getPedidoAula()));

        return avaliacaoExibicaoDto;
    }

    public static AvaliacaoCardDto ofAvaliacaoCard(Avaliacao avaliacao){
        AvaliacaoCardDto avaliacaoCardDto = new AvaliacaoCardDto();

        avaliacaoCardDto.setIdAvaliador(avaliacao.getUsuarioAvaliador().getId());
        avaliacaoCardDto.setNomeAvaliador(avaliacao.getUsuarioAvaliador().getNome());
        avaliacaoCardDto.setComentario(avaliacao.getComentario());
        avaliacaoCardDto.setValorAvaliacao(avaliacao.getValor());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = avaliacao.getDataAvaliacao().format(formatter);
        avaliacaoCardDto.setDataAvaliacaoFormatada(dataFormatada);

        return avaliacaoCardDto;
    }
}
