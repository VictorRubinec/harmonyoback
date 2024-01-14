package school.sptech.harmonyospringapi.utils;

public class ListaGenericaObj<T>{

    private T[] vetor;

    private int nroElem;

    public ListaGenericaObj(int tamanhoLista) {
        this.vetor = (T[]) new Object[tamanhoLista];;
        this.nroElem = 0;
    }

    public void adiciona(T elemento){

        if (nroElem == vetor.length){
            System.out.println("Lista Cheia !");
        }
        else {
            vetor[nroElem++] = elemento;
        }
    }

    public int size(){
        return vetor.length;
    }

    public T getElemento(int indice){

        if (validarIndice(indice)){
            return vetor[indice];
        }
        return null;
    }

    public T setElemento(int indice, T elemento){

        if (validarIndice(indice)){
            return vetor[indice] = elemento;
        }
        return null;
    }

    public void exibe(){
        if (nroElem == 0) {
            System.out.println("A Lista Está Vazia !");
        } else {
            for (int i = 0; i < nroElem; i++) {

                System.out.print("vetor[" + i + "] = " + vetor[i] + "\n");

            }
            System.out.println();
        }
    }

    private boolean validarIndice(int indice) {
        return indice >= 0 && indice < nroElem;
    }

}
