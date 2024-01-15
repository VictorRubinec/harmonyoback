package school.sptech.harmonyospringapi.utils;

import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;

public class ArvoreBin {
    private Node<InstrumentoExibicaoDto> raiz;

    public ArvoreBin() {
        this.raiz = null;
    }

    public Node<InstrumentoExibicaoDto> getRaiz(){
        return raiz;
    }

    public void criaRaiz(InstrumentoExibicaoDto info) {
        Node<InstrumentoExibicaoDto> newObj = new Node<>(info);
        raiz = newObj;
    }

    public Node<InstrumentoExibicaoDto> insereDir(Node<InstrumentoExibicaoDto> pai, InstrumentoExibicaoDto info) {
        if (raiz != null && pai.getDir() == null) {
            Node<InstrumentoExibicaoDto> newObj = new Node<>(info);
            pai.setDir(newObj);
            return newObj;
        }
        return null;
    }

    public Node<InstrumentoExibicaoDto> insereEsq(Node<InstrumentoExibicaoDto> pai, InstrumentoExibicaoDto info) {
        if (raiz != null && pai.getEsq() == null) {
            Node<InstrumentoExibicaoDto> newObj = new Node<>(info);
            pai.setEsq(newObj);
            return newObj;
        }
        return null;
    }

    public void iniciarExibe() {
        exibeArvore(raiz);
    }

    public void exibeArvore(Node<InstrumentoExibicaoDto> noDaVez) {
        if (noDaVez != null) {
            System.out.println(noDaVez.getInfo());
            if (noDaVez.getEsq() != null)
                System.out.println(noDaVez.getEsq().getInfo());
            if (noDaVez.getDir() != null)
                System.out.println(noDaVez.getDir().getInfo());
            exibeArvore(noDaVez.getEsq());
            exibeArvore(noDaVez.getDir());
        }
    }

}
