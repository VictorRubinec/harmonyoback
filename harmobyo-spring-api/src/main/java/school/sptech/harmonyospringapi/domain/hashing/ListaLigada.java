package school.sptech.harmonyospringapi.domain.hashing;

import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;

public class ListaLigada {

    private Node head;

    public ListaLigada() {
        this.head = new Node(null);
    }

    public void insereNode(PedidoExibicaoDto pedidoExibicaoDto){
        Node novo = new Node(pedidoExibicaoDto);
        novo.setNext(head.getNext());
        head.setNext(novo);
    }

    public Node buscaNode(PedidoExibicaoDto pedidoExibicaoDto){
        Node atual = head.getNext();
        while (atual != null){
            if (atual.getPedido() == pedidoExibicaoDto){
                return atual;
            } else {
                atual = atual.getNext();
            }
        }
        return null;
    }

    public boolean removeNode(PedidoExibicaoDto pedidoExibicaoDto){
        Node ant = head;
        Node atual = head.getNext();
        while (atual != null){
            if (atual.getPedido() == pedidoExibicaoDto){
                ant.setNext(atual.getNext());
                return true;
            } else {
                ant = atual;
                atual = atual.getNext();
            }
        }
        return false;
    }

    public int getTamanho(){
        Node atual = head.getNext();
        int tam = 0;
        while (atual != null){
            tam++;
            atual = atual.getNext();
        }
        return tam;
    }

    public Node getHead() {
        return head;
    }

    public Node buscaNodeRecursivo(Node atual, String status){
        if (atual == null){
            return null;
        }
        if (atual.getPedido().getStatus().getDescricao() == status){
            return atual;
        }
        return buscaNodeRecursivo(atual.getNext(), status);
    }

    public int getTamanhoRecursivo(int tam, Node atual){
        if (atual != null){
            tam++;
            atual = atual.getNext();

            getTamanhoRecursivo(tam, atual);
        }
        return tam;
    }

    public Node getElemento(int indice){
        Node atual = getHead().getNext();

        for (int i = 0; i <= indice; i++){
            if (indice == i){
                return atual;
            }
            atual = atual.getNext();
        }
        return null;
    }

    public Node getElementoRecursivo(int indice){
        Node atual = getHead().getNext();

        Node elemento = execucaoGetElementoRecursivo(indice, 0, atual);

        if (elemento == null){
            throw new EntitadeNaoEncontradaException("Pedido inexistente na HashingTable");
        }

        return elemento;
    }

    private Node execucaoGetElementoRecursivo(int indice, int posicao, Node atual){
        if (atual == null) {
            return null;
        } else if (indice == posicao){
            return atual;
        }
        atual = atual.getNext();
        posicao++;
        return execucaoGetElementoRecursivo(indice, posicao, atual);
    }
}
