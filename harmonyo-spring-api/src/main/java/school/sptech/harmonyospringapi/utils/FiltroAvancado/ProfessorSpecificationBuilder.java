package school.sptech.harmonyospringapi.utils.FiltroAvancado;

import org.springframework.data.jpa.domain.Specification;
import school.sptech.harmonyospringapi.domain.Professor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfessorSpecificationBuilder {

    private final List<CriteriosDePesquisa> parametros;

    public ProfessorSpecificationBuilder() {
        this.parametros = new ArrayList<>();
    }


    public final void adicionarParametro(String key, String operation, Object value, boolean forceOrPredicate) {
        OperacoesDePesquisa op = OperacoesDePesquisa.getOperacaoSimples(operation);

        if (Objects.nonNull(op)) {
            List<String> values = new ArrayList<>();
            boolean isOrPredicate = forceOrPredicate;

            //          - Para E e ; para OU
            if (value.toString().contains("-")) {
                values = new ArrayList<>(List.of(((String) value).split("-")));
            } else if (value.toString().contains(";")) {
                values = new ArrayList<>(List.of(((String) value).split(";")));
                isOrPredicate = true;
            }

            if (values.isEmpty()) values.add((String) value);

            /*System.out.println("=====================================");
            System.out.println("Filtro: " + key);
            System.out.println("Operação: " + op + " Operador: " + operation);
            System.out.println("Valores: " + values);
            System.out.println("=====================================");*/

            for (int i = 0; i < values.size(); i++) {
                String valueSeparated = values.get(i);
                boolean startWithAsterisk = (valueSeparated).startsWith("*");
                boolean endWithAsterisk = (valueSeparated).endsWith("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = OperacoesDePesquisa.CONTEM;
                    valueSeparated = (valueSeparated).substring(1, (valueSeparated).length() - 1);
                } else if (startWithAsterisk) {
                    op = OperacoesDePesquisa.TERMINA_COM;
                    valueSeparated = (valueSeparated).substring(1);
                } else if (endWithAsterisk) {
                    op = OperacoesDePesquisa.INICIA_COM;
                    valueSeparated = (valueSeparated).substring(0, (valueSeparated).length() - 1);
                }
                values.set(i, valueSeparated);
            }

            if (values.size() == 1) {
                this.parametros.add(new CriteriosDePesquisa(isOrPredicate, key, op, values.get(0)));
            } else {
                this.parametros.add(new CriteriosDePesquisa(isOrPredicate, key, op, values.get(0), values.get(1)));
            }
        }
        return;
    }

    public Specification<Professor> build() {
        if (this.parametros.size() == 0) return null;

        this.parametros.forEach(
                parametro -> System.out.println("Parametro: " + parametro.getKey() + " " + parametro.getOperation() + " " + parametro.getValue())
        );

        Specification<Professor> resultado = new ProfessorSpecification(this.parametros.get(0));

        for (int i = 1; i < this.parametros.size(); i++) {
            resultado = parametros.get(i).isOrPredicate()
                    ? resultado.or(new ProfessorSpecification(parametros.get(i)))
                    : resultado.and(new ProfessorSpecification(parametros.get(i)));
        }

        return resultado;
    }

    public List<CriteriosDePesquisa> getParametros() {
        return parametros;
    }
}
