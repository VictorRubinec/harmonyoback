package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.sptech.harmonyospringapi.domain.Aula;
import school.sptech.harmonyospringapi.service.aula.dto.AulaGraficoInformacoesDashboardDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AulaRepository extends JpaRepository<Aula, Integer> {


    Optional<Aula> findFirstByProfessorIdOrderByValorAulaAsc(Integer professorId);

//    List<Aula> findAllByIdAluno(int id);

    List<Aula> findAllByProfessorId(int fkProfessor);

    List<Aula> findAllByProfessorIdAndAtivaTrue(int fkProfessor);

    boolean existsByProfessorIdAndInstrumentoId(int idProfessor, int idInstrumento);

    @Query(value = "SELECT MIN(a.valorAula) FROM Aula a WHERE a.professor.id = :idProfessor")
    Optional<Double> obterValorMinimoAula(int idProfessor);

    @Query(value = "SELECT MAX(a.valorAula) FROM Aula a WHERE a.professor.id = :idProfessor")
    Optional<Double> obterValorMaximoAula(int idProfessor);


    @Query("""
        SELECT COUNT(*) FROM Aula a
        JOIN Pedido p ON a.id = p.aula.id
        WHERE p.dataAula BETWEEN :dataInicial AND :dataFinal AND p.status.descricao = "Concluído"
""")
    Integer countByDataAulaBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);
}
