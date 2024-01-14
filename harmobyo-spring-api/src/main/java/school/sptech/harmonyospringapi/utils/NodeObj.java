package school.sptech.harmonyospringapi.utils;

public class NodeObj <T> {

    private T info;
    private NodeObj next;

    public NodeObj(T info) {
        this.info = info;
        this.next = null;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public NodeObj getNext() {
        return next;
    }

    public void setNext(NodeObj next) {
        this.next = next;
    }

}


