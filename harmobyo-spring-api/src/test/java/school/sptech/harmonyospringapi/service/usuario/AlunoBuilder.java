package school.sptech.harmonyospringapi.service.usuario;

import school.sptech.harmonyospringapi.domain.Aluno;

public class AlunoBuilder {

    public static Aluno criarAluno() {
        Aluno aluno = new Aluno();
        aluno.setId(1);
        aluno.setNome("Aluno teste");
        aluno.setCpf("12345678901");
        aluno.setTelefone("12345678901");
        aluno.setEmail("teste@gmail.com");
        aluno.setSenha("123456");
        aluno.setEndereco(null);

        return aluno;
    }
}
