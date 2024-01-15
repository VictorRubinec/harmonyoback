package school.sptech.harmonyospringapi.utils.FiltroAvancado;

import org.springframework.data.jpa.domain.Specification;
import school.sptech.harmonyospringapi.domain.Professor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpecificationManager {

    public Specification<Professor> obterProfessoresFiltrados(String listaDeParametros) {
        List<String> listaDeParametrosSeparados = new ArrayList<>();

        boolean possuiNomeEInstrumento = listaDeParametros.contains("nome") && listaDeParametros.contains("instrumentos");

        if (!listaDeParametros.isBlank() || !listaDeParametros.isBlank()) {
            listaDeParametrosSeparados = List.of(listaDeParametros.split(","));
        }


        ProfessorSpecificationBuilder builder = new ProfessorSpecificationBuilder();

        boolean compararDistancia = false;
        List<String> parametrosSeparados;
        String key, operation, value;
        boolean forcePredicate = false;

        for (String parametro : listaDeParametrosSeparados) {
            parametrosSeparados = filtrarParametrosDeBusca(parametro);

            key = parametrosSeparados.get(0);
            operation = parametrosSeparados.get(1);
            value = parametrosSeparados.get(2).toLowerCase();

            if (possuiNomeEInstrumento && (Objects.equals(parametrosSeparados.get(0), "nome") || Objects.equals(parametrosSeparados.get(0), "instrumentos"))) {
                if (Objects.equals(parametrosSeparados.get(0), "nome")) {
                    forcePredicate = true;

                    key = "nomeOr";
                } else if (Objects.equals(parametrosSeparados.get(0), "instrumentos")) {
                    key = "instrumentoOr";
                }
            } else {
                forcePredicate = false;
            }

            if (!Objects.equals(parametrosSeparados.get(0), "distancia")) {
                /*System.out.println("=====================================");
                System.out.println("Parametros: " + key + " " + operation + " " + value + " " + forcePredicate);
                System.out.println("=====================================");*/

                builder.adicionarParametro(key, operation, value, forcePredicate);
            }

        }

        return builder.build();
    }


    public List<String> filtrarParametrosDeBusca(String parametros) {
        String operacao = "";

        List<String> criteriosDePesquisa = List.of("><", ">:", "<:", ":", "<", ">", "~");

        for (String criterio: criteriosDePesquisa) {
            if (parametros.contains(criterio)) {
                operacao = criterio;
                break;
            }
        }

        List<String> criterios = new ArrayList<>(List.of(parametros.split(operacao)));
        criterios.add(1, operacao);

        return criterios;
    }

}
