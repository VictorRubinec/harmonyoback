package school.sptech.harmonyospringapi.utils.FiltroAvancado;

public class CriteriosDePesquisa {

    private boolean orPredicate;

    private String key;
    private OperacoesDePesquisa operation;
    private Object value;

    private Object value2;


    public CriteriosDePesquisa(boolean orPredicate, String key, OperacoesDePesquisa operation, Object value) {
        this.orPredicate = orPredicate;
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public CriteriosDePesquisa(boolean orPredicate, String key, OperacoesDePesquisa operation, Object value, Object value2) {
        this.orPredicate = orPredicate;
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.value2 = value2;
    }

    public boolean isOrPredicate() {
        return orPredicate;
    }

    public void setOrPredicate(boolean orPredicate) {
        this.orPredicate = orPredicate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public OperacoesDePesquisa getOperation() {
        return operation;
    }

    public void setOperation(OperacoesDePesquisa operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }
}
