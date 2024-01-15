package school.sptech.harmonyospringapi.domain.fila;

import org.springframework.security.core.parameters.P;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;
import school.sptech.harmonyospringapi.utils.NodeObj;

import java.util.ArrayList;
import java.util.List;

public class FilaEspera {
    private NodeObj<PedidoExibicaoDto> inicio;
    private NodeObj<PedidoExibicaoDto> fim;
    private PedidoExibicaoDto pai;

    public FilaEspera(PedidoExibicaoDto pai) {
        this.inicio = null;
        this.fim = new NodeObj<>(null);
        this.pai = pai;
    }

    public boolean isEmpty() {
        return this.inicio == null;
    }

    public void insert(PedidoExibicaoDto info) {
        NodeObj<PedidoExibicaoDto> novo = new NodeObj<>(info);
        if (isEmpty()) {
            this.inicio = novo;
            this.inicio.setNext(this.fim);
        } else {
            NodeObj<PedidoExibicaoDto> atual = this.inicio;
            while (atual.getNext() != this.fim) {
                atual = atual.getNext();
            }
            atual.setNext(novo);
            novo.setNext(this.fim);
        }
    }

    public List<PedidoExibicaoDto> getPedidos() {
        List<PedidoExibicaoDto> pedidos = new ArrayList<>();
        NodeObj<PedidoExibicaoDto> atual = this.inicio;
        while (atual != this.fim) {
            pedidos.add(atual.getInfo());
            atual = atual.getNext();
        }
        return pedidos;
    }

    public int posicaoNaFila(PedidoExibicaoDto info){
        int posicao = 1;
        if (!isEmpty()) {
            List<PedidoExibicaoDto> pedidos = this.getPedidos();
            for (PedidoExibicaoDto pedido : pedidos){
                if (pedido.getId() == info.getId()){
                    return posicao;
                }
                posicao++;
            }
        }
        return posicao;
    }

    public PedidoExibicaoDto getPai() {
        return this.pai;
    }
    public void setPai(PedidoExibicaoDto pai) {
        this.pai = pai;
    }

    public PedidoExibicaoDto peek() {
        return this.inicio.getInfo();
    }

    public PedidoExibicaoDto poll() {
        if (!isEmpty()) {
            this.pai = this.inicio.getInfo();
            PedidoExibicaoDto aux = this.inicio.getInfo();
            this.inicio = this.inicio.getNext();
            return aux;
        }
        return null;
    }

    public int getTamanho() {
        int tam = 0;
        NodeObj<PedidoExibicaoDto> atual = this.inicio;
        while (atual != this.fim) {
            atual = atual.getNext();
            tam++;
        }
        return tam;
    }
}
