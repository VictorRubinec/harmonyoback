package school.sptech.harmonyospringapi.utils.FiltroAvancado;

public enum OperacoesDePesquisa {
    IGUALDADE,
    MAIOR_QUE,
    MENOR_QUE,
    MAIOR_OU_IGUAL_A,
    MENOR_OU_IGUAL_A,
    CONTEM,
    INICIA_COM,
    TERMINA_COM,
    ENTRE;

    public static OperacoesDePesquisa getOperacaoSimples(String in) {
        return switch (in) {
            case ":" -> IGUALDADE;
            case ">" -> MAIOR_QUE;
            case "<" -> MENOR_QUE;
            case ">:" -> MAIOR_OU_IGUAL_A;
            case "<:" -> MENOR_OU_IGUAL_A;
            case "~" -> CONTEM;
            case "><" -> ENTRE;
            default -> null;
        };
    }
}
