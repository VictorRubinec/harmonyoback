package school.sptech.harmonyospringapi.domain.hashing;

import school.sptech.harmonyospringapi.domain.Pedido;

public class HashTable {

    private ListaLigada tab[];

    public HashTable(int listas) {
        this.tab = new ListaLigada[listas];
        for (int i = 0; i < listas; i++){
            tab[i] = new ListaLigada();
        }
    }

    public ListaLigada[] getTab() {
        return tab;
    }
}
