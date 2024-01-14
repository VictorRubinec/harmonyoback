package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.sptech.harmonyospringapi.domain.AlunoInstrumento;
import school.sptech.harmonyospringapi.domain.Instrumento;

import java.util.List;

public interface AlunoInstrumentoRepository extends JpaRepository<AlunoInstrumento, Integer> {

    List<AlunoInstrumento> findByAluno_id(int id);

    Boolean existsAlunoInstrumentoByAluno_idAndInstrumento_id(int idAluno, int idInstrumento);

    @Query("SELECT i FROM Instrumento i INNER JOIN AlunoInstrumento ai ON ai.instrumento.id = i.id WHERE ai.aluno.id = :idAluno")
    List<Instrumento> listarInstrumentosPeloIdDoAluno(int idAluno);
}
