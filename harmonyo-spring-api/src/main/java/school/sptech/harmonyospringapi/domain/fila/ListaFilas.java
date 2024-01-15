package school.sptech.harmonyospringapi.domain.fila;

import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;
import school.sptech.harmonyospringapi.utils.NodeObj;

import java.util.ArrayList;
import java.util.List;

public class ListaFilas {

    private NodeObj<FilaEspera> head;

    public ListaFilas() {
        this.head = new NodeObj<>(null);
    }

    public void novaFila(FilaEspera fila) {
        NodeObj<FilaEspera> novo = new NodeObj<>(fila);
        novo.setNext(this.head.getNext());
        this.head.setNext(novo);
    }

    public FilaEspera buscaFila(int idPai){
        List<FilaEspera> filas = this.getFilas();
        for (FilaEspera fila : filas){
            if (fila.getPai().getId() == idPai){
                return fila;
            }
        }
        return null;
    }

    public boolean removeFila(PedidoExibicaoDto pai){
        NodeObj<FilaEspera> ant = this.head;
        NodeObj<FilaEspera> atual = this.head.getNext();
        while (atual != null){
            if (atual.getInfo().getPai() == pai){
                ant.setNext(atual.getNext());
                return true;
            } else {
                ant = atual;
                atual = atual.getNext();
            }
        }
        return false;
    }

    public PedidoExibicaoDto buscarPai(int idPedido){
        List<FilaEspera> filas = this.getFilas();
        for (FilaEspera fila : filas){
            List<PedidoExibicaoDto> pedidos = fila.getPedidos();
            for (PedidoExibicaoDto pedido : pedidos){
                if (pedido.getId() == idPedido){
                    return fila.getPai();
                }
            }
        }
        return null;
    }

    public int getTamanho(){
        NodeObj<FilaEspera> atual = this.head.getNext();
        int tam = 0;
        while (atual != null){
            tam++;
            atual = atual.getNext();
        }
        return tam;
    }

    public List<FilaEspera> getFilas(){
        List<FilaEspera> filas = new ArrayList<>();
        NodeObj<FilaEspera> atual = this.head.getNext();
        while (atual != null){
            filas.add(atual.getInfo());
            atual = atual.getNext();
        }
        return filas;
    }

    public boolean isEmpty() {
        return this.head.getNext() == null;
    }

    public NodeObj<FilaEspera> getHead() {
        return this.head;
    }
}
