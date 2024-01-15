package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.sptech.harmonyospringapi.domain.Professor;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDashboardDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoHistoricoDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidosMes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer>, JpaSpecificationExecutor<Professor> {
    @Query("SELECT p FROM Professor p LEFT JOIN Avaliacao a ON a.usuarioAvaliado.id = p.id  GROUP BY p.id ORDER BY AVG(a.valor) DESC LIMIT 4")
    List<Professor> buscarProfessoresPopulares();

    @Query("SELECT p FROM Professor p INNER JOIN Aula a" +
            " ON a.professor.id = p.id  GROUP BY p.id ORDER BY a.valorAula DESC")
    List<Professor> findTop50MaisCarosValorAula();

    @Query("SELECT p FROM Professor p INNER JOIN Aula a" +
            " ON a.professor.id = p.id GROUP BY p.id ORDER BY a.valorAula ASC")
    List<Professor> findTop50MaisBaratosValorAula();


    @Query("SELECT p FROM Professor p INNER JOIN Aula a "  +
            "ON a.professor.id = p.id WHERE a.instrumento.id = 1")
    List<Professor> getProfessoresByInstrumento(int idInstrumento);


    @Query("SELECT pi.emprestaInstrumento FROM ProfessorInstrumento pi WHERE pi.professor.id = :idProfessor AND pi.emprestaInstrumento = TRUE ORDER BY pi.emprestaInstrumento LIMIT 1")
    Optional<Boolean> emprestaInstrumento(Integer idProfessor);

    @Query("SELECT SUM(a.valorAula) FROM Aula a INNER JOIN Pedido p ON p.aula.id = a.id WHERE p.professor.id = :idProfessor AND p.dataAula BETWEEN :comeco AND :fim AND p.status.descricao = 'Concluído'")
    Optional<Double> getRendimentoPorPeriodo(int idProfessor, LocalDateTime comeco, LocalDateTime fim);
    @Query("SELECT SUM(a.valorAula) FROM Aula a INNER JOIN Pedido p ON p.aula.id = a.id WHERE p.professor.id = :idProfessor AND p.status.descricao = 'Concluído'")
    Optional<Double> getRendimento(int idProfessor);

    @Query("SELECT COUNT(DISTINCT (p.aluno.id)) FROM Pedido p WHERE p.professor.id = :id AND p.status.descricao = 'Concluído' AND p.dataAula BETWEEN :comeco AND :fim")
    Optional<Integer> getQuantidadeAlunosPorPeriodo(int id, LocalDateTime comeco, LocalDateTime fim);

    @Query("SELECT COUNT(DISTINCT (p.aluno.id)) FROM Pedido p WHERE p.professor.id = :id AND p.status.descricao = 'Concluído'")
    Optional<Integer> getQuantidadeAlunos(int id);


    @Query("SELECT COUNT(p.id) FROM Pedido p WHERE p.professor.id = :id AND p.status.descricao = 'Concluído' AND p.dataAula BETWEEN :comeco AND :fim")
    Optional<Integer> getQuantidadeAulasPorPeriodo(int id, LocalDateTime comeco, LocalDateTime fim);

    @Query("SELECT COUNT(p.id) FROM Pedido p WHERE p.professor.id = :id AND p.status.descricao = 'Concluído'")
    Optional<Integer> getQuantidadeAulas(int id);


    @Query("SELECT AVG(EXTRACT(MINUTE FROM p.horaCriacao) - EXTRACT(MINUTE FROM p.horaResposta)) FROM Pedido p WHERE p.professor.id = :id")
    Optional<Integer> getMediaTempoResposta(int id);

    @Query("SELECT new school.sptech.harmonyospringapi.service.pedido.dto.PedidoHistoricoDto(FUNCTION('MONTHNAME', p.dataAula), (SELECT COUNT(p.id) FROM Pedido where p.professor.id = :id), COUNT(p.id)) FROM Pedido p WHERE p.professor.id = :id AND p.status.descricao = 'Concluído' GROUP BY FUNCTION('MONTHNAME', p.dataAula)")
    List<PedidoHistoricoDto> getHistoricoPedidos(int id);

    @Query("SELECT new school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDashboardDto(p.aula.instrumento.id, p.aula.instrumento.nome, COUNT(p.id), SUM(p.aula.valorAula)) FROM Pedido p WHERE p.professor.id = :id AND p.status.descricao = 'Concluído' AND p.dataAula BETWEEN :comeco AND :fim  GROUP BY p.aula.instrumento.id")
    List<PedidoExibicaoDashboardDto> getAulasRealizadasAgrupadasPorInstrumentoMesAtual(int id, LocalDateTime comeco, LocalDateTime fim);

    @Query("SELECT new school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDashboardDto(p.aula.instrumento.id, p.aula.instrumento.nome, COUNT(p.id), SUM(p.aula.valorAula)) FROM Pedido p WHERE p.professor.id = :id AND p.status.descricao = 'Concluído' GROUP BY p.aula.instrumento.id")
    List<PedidoExibicaoDashboardDto> getAulasRealizadasAgrupadasPorInstrumentoTotal(int id);



    @Query("SELECT new school.sptech.harmonyospringapi.service.pedido.dto.PedidosMes(" +
            "FUNCTION('MONTHNAME', p.dataAula), " +
            "SUM(CASE WHEN p.status.descricao = 'Cancelado' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.descricao = 'Recusado' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.descricao = 'Concluído' THEN 1 ELSE 0 END)) " +
            "FROM Pedido p " +
            "WHERE p.professor.id = :id AND p.status.descricao IN ('Cancelado', 'Recusado', 'Concluído') " +
            "AND p.dataAula BETWEEN :comeco AND :fim " +
            "GROUP BY FUNCTION('MONTHNAME', p.dataAula), MONTH(p.dataAula)"+
            "ORDER BY FUNCTION('MONTH', p.dataAula)")
    List<PedidosMes> getAulasAgrupadasPorMes(int id, LocalDateTime comeco, LocalDateTime fim);

    @Query("SELECT new school.sptech.harmonyospringapi.service.pedido.dto.PedidosMes(" +
            "CONCAT(FUNCTION('MONTHNAME', p.dataAula), '/', FUNCTION('YEAR', p.dataAula)), " +
            "SUM(CASE WHEN p.status.descricao = 'Cancelado' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.descricao = 'Recusado' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.descricao = 'Concluído' THEN 1 ELSE 0 END)) " +
            "FROM Pedido p " +
            "WHERE p.professor.id = :id AND p.status.descricao IN ('Cancelado', 'Recusado', 'Concluído') " +
            "GROUP BY CONCAT(FUNCTION('MONTHNAME', p.dataAula), '/', FUNCTION('YEAR', p.dataAula)), FUNCTION('YEAR', p.dataAula), MONTH(p.dataAula)"+
            "ORDER BY FUNCTION('YEAR', p.dataAula), FUNCTION('MONTH', p.dataAula)")
    List<PedidosMes> getAulasAgrupadasPorMes(int id);

    @Query("SELECT COUNT(p) FROM Professor p WHERE p.dataCriacao BETWEEN :dataInicial AND :dataFinal")
    Integer obterQuantidadeCadastradosEntre(LocalDateTime dataInicial, LocalDateTime dataFinal);

    @Query("SELECT SUM(p.valorAula) FROM Aula a INNER JOIN Pedido p ON p.aula.id = a.id WHERE p.dataAula BETWEEN :dataComeco AND :dataFim AND p.status.descricao = 'Concluído'")
    Optional<Double> getRendimentoTotalPeriodo(LocalDateTime dataComeco, LocalDateTime dataFim);
}

