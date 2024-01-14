package school.sptech.harmonyospringapi.service.usuario;

import school.sptech.harmonyospringapi.domain.Usuario;
import school.sptech.harmonyospringapi.utils.ListaGenericaObj;

public class UsuarioComparador {

    private ListaGenericaObj<Usuario> ltUsuarios;

    public UsuarioComparador(ListaGenericaObj<Usuario> ltUsuarios) {
        this.ltUsuarios = ltUsuarios;
    }

    public ListaGenericaObj<Usuario> ordenacaoAlfabetica(){

        int contadorTrocas = 0;
        int qtdComparacoes = 0;

        for (int i = 0; i < ltUsuarios.size(); i++) {


            for (int j = ltUsuarios.size() - 1; j > i; j--) {

                if (ltUsuarios.getElemento(i).getNome().compareToIgnoreCase(ltUsuarios.getElemento(j).getNome()) > 0) {

                    Usuario usuarioAux = ltUsuarios.getElemento(i);

                    ltUsuarios.setElemento(i, ltUsuarios.getElemento(j));

                    ltUsuarios.setElemento(j, usuarioAux);

                    contadorTrocas++;

                }

                qtdComparacoes++;
            }
        }

        return ltUsuarios;
    }



    public int pesquisaBinariaPorNome(String nome){

        int inicio = 0;
        int fim = ltUsuarios.size() - 1;

        while (inicio <= fim){

            int meio = (inicio + fim)/2;

            if (nome.equalsIgnoreCase(ltUsuarios.getElemento(meio).getNome())){

                return meio;

            } else if (nome.compareToIgnoreCase(ltUsuarios.getElemento(meio).getNome()) > 0) {
                inicio = meio + 1;
            }
            else {
                fim = meio - 1;
            }
        }
        return -1;
    }
}
