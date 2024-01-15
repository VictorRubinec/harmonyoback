package school.sptech.harmonyospringapi.utils.FiltroAvancado;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import school.sptech.harmonyospringapi.domain.*;

import java.util.Objects;

public class ProfessorSpecification implements Specification<Professor> {

    private CriteriosDePesquisa criterio;

    public ProfessorSpecification(CriteriosDePesquisa criterio) {
        this.criterio = criterio;
    }

    @Override
    public Predicate toPredicate
            (Root<Professor> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (Objects.equals(criterio.getKey(), "preco")) {
            return construirCriterioComJoinValorAula(root, builder);
        } else if (Objects.equals(criterio.getKey(), "avaliacao")) {
            return construirCriterioComJoinMediaAvaliacao(root, builder, query);
        } else if (Objects.equals(criterio.getKey(), "instrumento")) {
            return construirCriterioComJoinInstrumentos(root, builder, query);
        } else if (Objects.equals(criterio.getKey(), "aula")) {
            return construirCriterioComJoinAula(root, builder, query);
        } else if (Objects.equals(criterio.getKey(), "cidade")) {
            return construirCriterioComJoinEndereco(root, builder, query);
        } else if (Objects.equals(criterio.getKey(), "nomeOr")) {
            return construirCriterioComJoinNomeOr(root, builder);
        } else if (Objects.equals(criterio.getKey(), "instrumentoOr")) {
            return construirCriterioComJoinInstrumentoOr(root, builder);
        } else {
            return construirCriterio(root, builder);
        }
    }

    private Predicate construirCriterioComJoinValorAula(Root<Professor> root, CriteriaBuilder builder) {
        String key = "valorAula";
        Join<Professor, Aula> joinAula = root.join("aulas", JoinType.INNER);
        Path<String> valorAula = joinAula.get(key);

        return construirCriterio(root, builder, valorAula);
    }

    private Predicate construirCriterioComJoinMediaAvaliacao(Root<Professor> root, CriteriaBuilder builder, CriteriaQuery<?> query) {
        Subquery<Double> subquery = query.subquery(Double.class);
        Root<Professor> subRoot = subquery.from(Professor.class);
        Join<Professor, Avaliacao> joinAvaliacao2 = subRoot.join("avaliacoesRecebidas", JoinType.INNER);
        subquery.select(builder.avg(joinAvaliacao2.get("valor")));
        subquery.where(builder.equal(subRoot.get("id"), root.get("id")));

        return builder.greaterThanOrEqualTo(subquery, Double.parseDouble((String) criterio.getValue()));
    }

    private Predicate construirCriterioComJoinInstrumentos(Root<Professor> root, CriteriaBuilder builder, CriteriaQuery<?> query) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Professor> subRoot = subquery.from(Professor.class);
        Join<Professor, Aula> joinProfessorAulas = subRoot.join("aulas", JoinType.INNER);
        Predicate possuiOMesmoId = builder.equal(subRoot.get("id"), root.get("id"));
        Predicate possuiOMesmoInstrumento = null;
        Path<String> nomeInstrumento = joinProfessorAulas.get("instrumento").get("nome");

        possuiOMesmoInstrumento = construirCriterio(root, builder, nomeInstrumento);

        subquery.select(builder.count(joinProfessorAulas))
                .where(possuiOMesmoId, possuiOMesmoInstrumento);

        return builder.equal(subquery, 1L);
    }

    private Predicate construirCriterioComJoinAula(Root<Professor> root, CriteriaBuilder builder, CriteriaQuery<?> query) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Professor> subRoot = subquery.from(Professor.class);
        Join<Professor, Aula> joinAula = subRoot.join("aulas", JoinType.INNER);
        Predicate possuiOMesmoId = builder.equal(subRoot.get("id"), root.get("id"));
        Predicate ensinaOInstrumento = null;
        Path<String> nomeInstrumento = joinAula.get("instrumento").get("nome");

        ensinaOInstrumento = construirCriterio(root, builder, nomeInstrumento);

        subquery.select(builder.count(joinAula))
                .where(possuiOMesmoId, ensinaOInstrumento);

        return builder.greaterThanOrEqualTo(subquery, 1L);
    }

    private Predicate construirCriterioComJoinEndereco(Root<Professor> root, CriteriaBuilder builder, CriteriaQuery<?> query) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Professor> subRoot = subquery.from(Professor.class);
        Join<Professor, Endereco> joinEndereco = subRoot.join("endereco", JoinType.INNER);
        Predicate possuiOMesmoId = builder.equal(subRoot.get("id"), root.get("id"));
        Predicate possuiOMesmaCidade = builder.equal(builder.lower(joinEndereco.get("cidade")), criterio.getValue());

        subquery.select(builder.count(joinEndereco))
                .where(possuiOMesmoId, possuiOMesmaCidade);

        return builder.greaterThanOrEqualTo(subquery, 1L);
    }

    private Predicate construirCriterioComJoinNomeOr(Root<Professor> root, CriteriaBuilder builder) {
        return construirCriterio(root, builder, root.get("nome"));
    }

    private Predicate construirCriterioComJoinInstrumentoOr(Root<Professor> root, CriteriaBuilder builder) {
        Join<Professor, Aula> joinAula = root.join("aulas", JoinType.INNER);
        return construirCriterio(root, builder, joinAula.get("instrumento").get("nome"));
    }

    private Predicate construirCriterio(Root<Professor> root, CriteriaBuilder builder) {
        Path<String> path = root.get(criterio.getKey());
        Expression<String> lowerPath = builder.lower(path);
        Object valor = criterio.getValue();
        String valorString = (String) valor;



        if (criterio.getOperation().equals(OperacoesDePesquisa.IGUALDADE) && criterio.getValue() instanceof String) return builder.equal(lowerPath, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.IGUALDADE)) return builder.equal(path, valor);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MAIOR_QUE)) return builder.greaterThan(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MENOR_QUE)) return builder.lessThan(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MAIOR_OU_IGUAL_A)) return builder.greaterThanOrEqualTo(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MENOR_OU_IGUAL_A)) return builder.lessThanOrEqualTo(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.CONTEM)) return builder.like(lowerPath, "%" + valor + "%");
        else if (criterio.getOperation().equals(OperacoesDePesquisa.INICIA_COM)) return builder.like(lowerPath, valor + "%");
        else if (criterio.getOperation().equals(OperacoesDePesquisa.TERMINA_COM)) return builder.like(lowerPath, "%" + valor);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.ENTRE) && Objects.nonNull(criterio.getValue2())) return builder.between(path, valorString, criterio.getValue2().toString());

        return null;
    }

    private Predicate construirCriterio(Join<?, ?> root, CriteriaBuilder builder) {
        Expression<String> lowerPath;
        Path<String> path = root.get(criterio.getKey());

        if (path.getJavaType() == String.class) {
            lowerPath = builder.lower(path);
        } else {
            lowerPath = path;
        }
        Object valor = criterio.getValue();
        String valorString = (String) valor;

        if (criterio.getOperation().equals(OperacoesDePesquisa.IGUALDADE) && criterio.getValue() instanceof String) return builder.equal(lowerPath, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.IGUALDADE)) return builder.equal(path, valor);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MAIOR_QUE)) return builder.greaterThan(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MENOR_QUE)) return builder.lessThan(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MAIOR_OU_IGUAL_A)) return builder.greaterThanOrEqualTo(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MENOR_OU_IGUAL_A)) return builder.lessThanOrEqualTo(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.CONTEM)) return builder.like(lowerPath, "%" + valor + "%");
        else if (criterio.getOperation().equals(OperacoesDePesquisa.INICIA_COM)) return builder.like(lowerPath, valor + "%");
        else if (criterio.getOperation().equals(OperacoesDePesquisa.TERMINA_COM)) return builder.like(lowerPath, "%" + valor);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.ENTRE) && Objects.nonNull(criterio.getValue2())) return builder.between(path, valorString, criterio.getValue2().toString());

        return null;
    }

    private Predicate construirCriterio(Root<Professor> root, CriteriaBuilder builder, Path<String> path) {
        Expression<String> lowerPath;
        if (path.getJavaType() == String.class) {
            lowerPath = builder.lower(path);
        } else {
            lowerPath = path;
        }
        Object valor = criterio.getValue();
        String valorString = valor.toString();


        if (criterio.getOperation().equals(OperacoesDePesquisa.IGUALDADE) && path.getJavaType() == String.class) return builder.equal(lowerPath, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.IGUALDADE)) return builder.equal(path, valor);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MAIOR_QUE)) return builder.greaterThan(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MENOR_QUE)) return builder.lessThan(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MAIOR_OU_IGUAL_A)) return builder.greaterThanOrEqualTo(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MENOR_OU_IGUAL_A)) return builder.lessThanOrEqualTo(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.CONTEM)) return builder.like(lowerPath, "%" + valor + "%");
        else if (criterio.getOperation().equals(OperacoesDePesquisa.INICIA_COM)) return builder.like(lowerPath, valor + "%");
        else if (criterio.getOperation().equals(OperacoesDePesquisa.TERMINA_COM)) return builder.like(lowerPath, "%" + valor);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.ENTRE) && Objects.nonNull(criterio.getValue2())) return builder.between(path, valorString, criterio.getValue2().toString());

        return null;
    }

    private Predicate construirCriterio(Join<?, ?> root, CriteriaBuilder builder, CriteriosDePesquisa criterio) {
        Path<String> path = root.get(criterio.getKey());
        Expression<String> lowerPath = builder.lower(path);
        Object valor = criterio.getValue();
        String valorString = (String) valor;


        if (criterio.getOperation().equals(OperacoesDePesquisa.IGUALDADE) && criterio.getValue() instanceof String) return builder.equal(lowerPath, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.IGUALDADE)) return builder.equal(path, valor);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MAIOR_QUE)) return builder.greaterThan(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MENOR_QUE)) return builder.lessThan(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MAIOR_OU_IGUAL_A)) return builder.greaterThanOrEqualTo(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.MENOR_OU_IGUAL_A)) return builder.lessThanOrEqualTo(path, valorString);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.CONTEM)) return builder.like(lowerPath, "%" + valor + "%");
        else if (criterio.getOperation().equals(OperacoesDePesquisa.INICIA_COM)) return builder.like(lowerPath, valor + "%");
        else if (criterio.getOperation().equals(OperacoesDePesquisa.TERMINA_COM)) return builder.like(lowerPath, "%" + valor);
        else if (criterio.getOperation().equals(OperacoesDePesquisa.ENTRE) && Objects.nonNull(criterio.getValue2())) return builder.between(path, valorString, criterio.getValue2().toString());

        return null;
    }
}
