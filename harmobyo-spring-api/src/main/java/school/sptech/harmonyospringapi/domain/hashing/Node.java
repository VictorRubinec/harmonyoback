package school.sptech.harmonyospringapi.domain.hashing;

import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;


public class Node {
    private PedidoExibicaoDto pedidoExibicaoDto;
    private Node next;

    public Node(PedidoExibicaoDto pedidoExibicaoDto) {
        this.pedidoExibicaoDto = pedidoExibicaoDto;
        this.next = null;
    }

    public PedidoExibicaoDto getPedido() {
        return pedidoExibicaoDto;
    }

    public void setPedido(PedidoExibicaoDto pedidoExibicaoDto) {
        this.pedidoExibicaoDto = pedidoExibicaoDto;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
