package school.sptech.harmonyospringapi.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PilhaObj<T> {

    // Atributos
    private T[] pilha;
    protected int topo;

    // Construtor
    public PilhaObj(int capacidade) {
        pilha = (T[]) new Object[capacidade];
        topo = -1;
    }

    // Métodos

    // Retorna true se a pilha estiver vazia e false caso contrário
    @JsonIgnore
    public Boolean isEmpty() {
        return topo == -1;
    }

    // 04) MÃ©todo isFull
    protected Boolean isFull() {
        return topo == pilha.length - 1;
    }
    public T[] getPilha() {
        return pilha;
    }
    // Se a pilha estiver cheia, deve lançar IllegalStateException
    // Se a pilha não estiver cheia, empilha info
    public void push(T info) {
        if (isFull()) {
            throw new IllegalStateException("Pilha cheia!");
        }else {
            pilha[++topo] = info;
        }
    }

    // Desempilha e retorna o elemento do topo da pilha
    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("A pilha está vazia!");
        }
        T valor = pilha[topo];
        topo--;
        return valor;
    }

    // Retorna o elemento do topo da pilha, sem desempilhar
    public T peek() {
        return pilha[topo];
    }

    // Exibe o conteúdo da pilha
    public void exibe() {
        if (isEmpty()) {
            System.out.println("A pilha está vazia!");
        } else {
            System.out.print("Pilha: ");
            for (int i = topo; i >= 0; i--) {
                System.out.print(pilha[i] + " ");
            }
            System.out.println();
        }
    }

    public int getTopo(){
        return topo;
    }

}
