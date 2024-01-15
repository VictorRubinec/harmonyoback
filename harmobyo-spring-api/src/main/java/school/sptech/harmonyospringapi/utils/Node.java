package school.sptech.harmonyospringapi.utils;

public class Node<T> {
    private T info;
    private Node esq;
    private Node  dir;

    public Node(T info) {
        this.info = info;
        this.esq = null;
        this.dir = null;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public Node<T> getEsq() {
        return esq;
    }

    public void setEsq(Node<T> esq) {
        this.esq = esq;
    }

    public Node<T> getDir() {
        return dir;
    }

    public void setDir(Node<T> dir) {
        this.dir = dir;
    }
}
