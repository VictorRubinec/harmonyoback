package school.sptech.harmonyospringapi.service.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.Notificacao;
import school.sptech.harmonyospringapi.service.notificacao.NotificacaoService;
import school.sptech.harmonyospringapi.service.notificacao.dto.NotificacaoExibicaoDto;
import school.sptech.harmonyospringapi.service.pedido.PedidoService;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketService {

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private SimpMessagingTemplate message;

    @Autowired
    private PedidoService pedidoService;


    public void enviarNotificacoes(Integer idUsuario, int pagina) {
        Object mensagem = new Object() {
            public final String tipo = "INIT";

            public final int qtdNotificacoesNaoLidas = notificacaoService.obterQuantidadeNotificacoesNaoLidas(idUsuario);
            public final Page<NotificacaoExibicaoDto> notificacoes = notificacaoService.obterPorIdUsuario(idUsuario,
                    PageRequest.of(pagina, 5, Sort.by(Sort.Order.desc("data"))));
        };


        message.convertAndSendToUser(String.valueOf(idUsuario), "/notificacao", mensagem);
    }

    public void adicionarNovaNotificacao(Integer idUsuario, NotificacaoExibicaoDto notificacaoExibicaoDto) {
        Object mensagem = new Object() {
            public final String tipo = "ADD";
            public final int totalPaginas = notificacaoService.obterPorIdUsuario(idUsuario, PageRequest.of(0, 5)).getTotalPages();
            public final int qtdNotificacoesNaoLidas = notificacaoService.obterQuantidadeNotificacoesNaoLidas(idUsuario);
            public final NotificacaoExibicaoDto notificacao = notificacaoExibicaoDto;
        };

        message.convertAndSendToUser(String.valueOf(idUsuario), "/notificacao", mensagem);
    }

    public void removeNotificacao(Integer idUsuario, Integer idNotificacao) {
        Object mensagem = new Object() {
            public String tipo = "REMOVE";
            public Integer id = idNotificacao;
        };

        message.convertAndSendToUser(String.valueOf(idUsuario), "/notificacao", mensagem);
    }

    public void enviarPedidos(Integer idUsuario, int pagina) {
        Object mensagem = new Object() {
            public final String tipo = "INIT";

            public final Page<PedidoExibicaoDto> pedidos = pedidoService.obterTodosPedidosPorPaginaPeloIdUsuario(idUsuario,
                    PageRequest.of(pagina, 5, Sort.by(Sort.Order.desc("dataAula"))));
        };

        message.convertAndSendToUser(String.valueOf(idUsuario), "/pedido", mensagem);
    }

    public void adicionaNovoPedido(Integer idUsuario, PedidoExibicaoDto pedidoExibicaoDto) {
        Object mensagem = new Object() {
            public final String tipo = "ADD";
            public final PedidoExibicaoDto pedido = pedidoExibicaoDto;
        };

        message.convertAndSendToUser(String.valueOf(idUsuario), "/pedido", mensagem);
    }
}
