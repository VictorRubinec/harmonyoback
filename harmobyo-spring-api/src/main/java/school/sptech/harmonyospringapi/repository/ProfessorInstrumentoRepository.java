package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.domain.ProfessorInstrumento;

import java.util.List;

public interface ProfessorInstrumentoRepository extends JpaRepository<ProfessorInstrumento, Integer> {

    List<ProfessorInstrumento> findByProfessor_id(int id);

    Boolean existsByProfessor_idAndInstrumento_id(int idProfessor, int idInstrumento);

    @Query("SELECT i FROM Instrumento i INNER JOIN ProfessorInstrumento pi ON pi.instrumento.id = i.id WHERE pi.professor.id = :idProfessor")
    List<Instrumento> listarInstrumentosPeloIdDoProfessor(int idProfessor);
}
