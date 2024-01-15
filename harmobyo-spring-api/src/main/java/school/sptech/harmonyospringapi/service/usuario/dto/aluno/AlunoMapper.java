package school.sptech.harmonyospringapi.service.usuario.dto.aluno;

import school.sptech.harmonyospringapi.domain.Aluno;

public class AlunoMapper {
    public static Aluno ofAlunoAtualizacaoEmAlunoDomain(AlunoAtualizacaoDto aluno){
        Aluno alunoDomain = new Aluno();
        alunoDomain.setNome(aluno.getNome());
        alunoDomain.setEmail(aluno.getEmail());
        alunoDomain.setSexo(aluno.getSexo());
        alunoDomain.setSenha(aluno.getSenha());
        alunoDomain.setEndereco(aluno.getEndereco());
        alunoDomain.setDataNasc(aluno.getDataNasc());
        alunoDomain.setTelefone(aluno.getTelefone());
        return alunoDomain;
    }
}
