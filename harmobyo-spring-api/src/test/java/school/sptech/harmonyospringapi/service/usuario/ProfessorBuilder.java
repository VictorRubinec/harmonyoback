package school.sptech.harmonyospringapi.service.usuario;

import school.sptech.harmonyospringapi.domain.Professor;

public class ProfessorBuilder {

    private static final Integer ID = 2;
    private static final String NOME = "Professor";

    public static Professor criarProfessor() {
        Professor professor = new Professor();
        professor.setId(ID);
        professor.setNome(NOME);
        return professor;
    }

    public static Professor criarProfessor(Integer id) {
        Professor professor = new Professor();
        professor.setId(id);
        professor.setNome(NOME);
        return professor;
    }

    public static Integer getId() {
        return ID;
    }

    public static String getNome() {
        return NOME;
    }
}
