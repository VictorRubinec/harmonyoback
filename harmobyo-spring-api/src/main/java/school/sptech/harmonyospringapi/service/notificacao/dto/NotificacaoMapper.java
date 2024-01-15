package school.sptech.harmonyospringapi.service.notificacao.dto;

import school.sptech.harmonyospringapi.domain.Notificacao;
import school.sptech.harmonyospringapi.domain.Usuario;

import java.time.LocalDateTime;

public class NotificacaoMapper {

    public static Notificacao ofNotificacaoCriacao(NotificacaoCriacaoDto notificacaoCriacaoDto, Usuario usuario) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTitulo(notificacaoCriacaoDto.getTitulo());
        notificacao.setDescricao(notificacaoCriacaoDto.getDescricao());
        notificacao.setUsuario(usuario);
        notificacao.setData(LocalDateTime.now());
        return notificacao;
    }

    public static NotificacaoExibicaoDto ofNotificacao(Notificacao notificacao) {
        NotificacaoExibicaoDto notificacaoExibicaoDto = new NotificacaoExibicaoDto();

        notificacaoExibicaoDto.setId(notificacao.getId());

        notificacaoExibicaoDto.setTitulo(notificacao.getTitulo());

        notificacaoExibicaoDto.setDescricao(notificacao.getDescricao());

        notificacaoExibicaoDto.setData(notificacao.getData());

        notificacaoExibicaoDto.setLida(notificacao.isLida());

        return notificacaoExibicaoDto;
    }
}
