package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.sptech.harmonyospringapi.domain.Usuario;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioAtulizarDadosPessoaisDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    @Query("SELECT min(a.valorAula) FROM Professor p JOIN Aula a ON p.id = a.professor.id")
    Double obterPrecoMinimo();

    @Query("SELECT max(a.valorAula) FROM Professor p JOIN Aula a ON p.id = a.professor.id")
    Double obterPrecoMaximo();


    @Modifying
    @Query("UPDATE Usuario u SET u.nome = :nome, " +
                                "u.email = :email, " +
                                "u.dataNasc = :dataNasc, " +
                                "u.sexo = :sexo WHERE u.id = :id")
    void atualizarDadosPessoais(int id, String nome, String email, LocalDate dataNasc, String sexo);

    @Modifying
    @Query("UPDATE Usuario u SET u.bibliografia = :bibliografia WHERE u.id = :id")
    void atualizarBibliografia(int id, String bibliografia);

    @Query("SELECT count(*) FROM Usuario u WHERE u.categoria != 'Administrador' AND u.dataCriacao BETWEEN :dataComeco AND :dataFim")
    Integer obterQuantidadeUsuario(LocalDateTime dataComeco, LocalDateTime dataFim);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.dataCriacao BETWEEN :dataInicial AND :dataFinal")
    Optional<Integer> obterQuantidadeUsuariosCadastradosEntre(LocalDateTime dataInicial, LocalDateTime dataFinal);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.dataCriacao BETWEEN :dataInicial AND :dataFinal AND u.categoria = :tipo")
    Optional<Integer> obterQuantidadeUsuariosCadastradosEntre(LocalDateTime dataInicial, LocalDateTime dataFinal, String tipo);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.dataCriacao BETWEEN :dataInicial AND :dataFinal AND u.categoria = 'Aluno'")
    Optional<Integer> obterQuantidadeAlunosCadastradosEntre(LocalDateTime dataInicial, LocalDateTime dataFinal);

    @Query("SELECT COUNT(u) FROM Usuario u JOIN Pedido p ON p.aluno.id = u.id WHERE count(p) > 0 AND p.horaResposta BETWEEN :dataInicio AND :dataFim")
    Integer obterQuantidadeUsuariosRetidosEntre(LocalDateTime dataInicio, LocalDateTime dataFim);


}
