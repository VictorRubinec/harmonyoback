package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import school.sptech.harmonyospringapi.domain.Pedido;
import school.sptech.harmonyospringapi.service.aula.dto.AulaGraficoInformacoesDashboardDto;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Query("SELECT p FROM Pedido p WHERE p.aluno.id = :usuarioId OR p.professor.id = :usuarioId")
    List<Pedido> buscarPorUsuarioId(Integer usuarioId);

    @Query("SELECT p FROM Pedido p WHERE p.aluno.id = :usuarioId OR p.professor.id = :usuarioId AND YEAR(p.horaCriacao) = YEAR(CURDATE())")
    List<Pedido> buscarPorUsuarioIdAnual(Integer usuarioId);

    @Query("SELECT p FROM Pedido p WHERE p.aula.professor.id = :idProfessor AND (p.status.descricao = 'Pendente' OR p.status.descricao = 'Aguardando Pagamento' OR p.status.descricao = 'Confirmado')")
    List<Pedido> encontrarPedidosPendentesPorIdProfessor(int idProfessor);

    @Query("SELECT new school.sptech.harmonyospringapi.service.aula.dto.AulaGraficoInformacoesDashboardDto( " +
            "SUM(CASE WHEN p.status.descricao = 'Cancelado' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.descricao = 'Recusado' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN p.status.descricao = 'Concluído' THEN 1 ELSE 0 END))" +
            "FROM Pedido p " +
            "WHERE p.professor.id = :id AND p.horaCriacao BETWEEN :comeco AND :fim AND p.status.descricao IN ('Cancelado', 'Recusado', 'Concluído') ")
    AulaGraficoInformacoesDashboardDto getDadosAulasPeriodoPorIdProfessor(int id, LocalDateTime comeco, LocalDateTime fim);

    @Query(value = "SELECT p from Pedido p where (p.professor.id = :fkUsuario or p.aluno.id = :fkUsuario) and (p.status.descricao = 'Confirmado' or p.status.descricao = 'Concluído')  and CAST(p.dataAula AS DATE ) = CAST(:data AS DATE)")
    List<Pedido> findAllByUsuarioIdAndAulaDataConfirmado(int fkUsuario, LocalDateTime data);

    @Query(value = "SELECT p from Pedido p where (p.professor.id = :fkUsuario or p.aluno.id = :fkUsuario) and CAST(p.dataAula AS DATE ) = CAST(:data AS DATE)")
    List<Pedido> findAllByUsuarioIdAndAulaData(int fkUsuario, LocalDateTime data);

    @Query(value = "SELECT p from Pedido p where (p.professor.id = :fkUsuario or p.aluno.id = :fkUsuario) and CAST(p.dataAula AS DATE ) = CAST(:data AS DATE)")
    Pedido findByUsuarioIdAndAulaData(int fkUsuario, LocalDateTime data);

    @Query(value = "SELECT p FROM Pedido p WHERE (p.professor.id = :usuarioId OR p.aluno.id = :usuarioId) AND p.status.descricao = 'Confirmado'")
    List<Pedido> buscarPorUsuarioIdConfirmado(Integer usuarioId);

    @Query(value = "SELECT p from Pedido p where (p.professor.id = :fkUsuario or p.aluno.id = :fkUsuario) and (p.status.descricao = 'Confirmado' or p.status.descricao = 'Concluído') and  MONTH(CAST(p.dataAula as DATE)) = MONTH(CAST(:localDateTime AS DATE))")
    List<Pedido> findAllByUsuarioIdAndAulaDataMes(int fkUsuario, LocalDateTime localDateTime);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.dataAula BETWEEN :dataComeco AND :dataFim")
    Integer obterQuantidadePedidosDuranteDatas(LocalDateTime dataComeco, LocalDateTime dataFim);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.status.descricao = 'Concluído' AND p.dataAula BETWEEN :diaInicio AND :diaFim")
    Optional<Integer> obterQuantidadePedidosRealizadosDuranteDatas(LocalDateTime diaInicio, LocalDateTime diaFim);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE (p.status.descricao = 'Confirmado') AND p.dataAula BETWEEN :diaInicio AND :diaFim")
    Integer obterQuantidadePedidosPendentesDuranteDatas(LocalDateTime diaInicio, LocalDateTime diaFim);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.status.descricao = 'Cancelado' AND p.dataAula BETWEEN :diaInicio AND :diaFim")
    Integer obterQuantidadePedidosCanceladosDuranteDatas(LocalDateTime diaInicio, LocalDateTime diaFim);

    @Query("""
        SELECT SUM(p.valorAula) FROM Pedido p
        WHERE p.status.descricao = 'Concluído' AND p.dataAula BETWEEN :dataComeco AND :dataFim
""")
    Optional<Double> obterRendimentoPeriodo(LocalDateTime dataComeco, LocalDateTime dataFim);

    @Query("SELECT p FROM Pedido p WHERE p.aluno.id = :idUsuario OR p.professor.id = :idUsuario")
    Page<Pedido> obterTodosPedidosPorPaginaPeloIdUsuario(Integer idUsuario, Pageable pageable);

    @Query("SELECT count(*) FROM Pedido p WHERE (p.dataAula BETWEEN :diaInicio AND :diaFim) AND p.status.descricao LIKE :status")
    Optional<Integer> obterQuantidadeAulasHistorico(LocalDateTime diaInicio, LocalDateTime diaFim, String status);

    @Query("SELECT sum(p.valorAula) FROM Pedido p WHERE p.status.descricao = 'Concluído' AND p.dataAula >= :dataInicial AND p.dataAula <= :dataFinal")
    Optional<Double> obterRendimentoProfessores(LocalDateTime dataInicial, LocalDateTime dataFinal);

    @Query("""
        SELECT p.aula.instrumento.nome, COUNT(p.aula.instrumento.nome) AS qtd_pedidos
        FROM Pedido p
        WHERE p.status.descricao = 'Concluído' AND p.dataAula BETWEEN :dataInicial AND :dataFinal
        GROUP BY p.aula.instrumento.nome
        ORDER BY qtd_pedidos DESC
        """)
    List<Object> obterQuantidadePedidosInstrumentoPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal);

    @Query("""
        SELECT CONCAT(p.professor.endereco.estado, " - ", p.professor.endereco.cidade), COUNT(p.professor.endereco.cidade) AS qtd_pedidos
        FROM Pedido p
        WHERE p.status.descricao = 'Concluído' AND p.dataAula BETWEEN :dataInicial AND :dataFinal
        GROUP BY p.professor.endereco.estado, p.professor.endereco.cidade
        ORDER BY qtd_pedidos DESC
        """)
    List<Object> obterQuantidadePedidosRegiaoPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal);
}
