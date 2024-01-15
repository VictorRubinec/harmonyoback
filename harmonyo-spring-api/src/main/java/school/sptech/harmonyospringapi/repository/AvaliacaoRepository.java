package school.sptech.harmonyospringapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import school.sptech.harmonyospringapi.domain.Avaliacao;
import school.sptech.harmonyospringapi.domain.Usuario;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    @Query("SELECT AVG(a.valor) AS Avaliacao_media FROM Avaliacao AS a WHERE a.usuarioAvaliado.id = :idUsuario")
    Optional<Double> getMediaAvaliacaoUsuario(Integer idUsuario);


    @Query("SELECT AVG(a.valor) AS Avaliacao_media FROM Avaliacao AS a WHERE a.usuarioAvaliado.id = :idProfessor")
    Optional<Double> getMediaAvaliacaoProfessor(Integer idProfessor);

    List<Avaliacao> findByUsuarioAvaliadoId(Integer id);

    @Query("SELECT COUNT(a.valor) FROM Avaliacao AS a WHERE a.usuarioAvaliado.id = :id")
    Optional<Integer> getQuantidadeAvaliacoes(Integer id);

    boolean existsAvaliacaoByPedidoIdAndUsuarioAvaliador(Integer idPedido, Usuario usuarioAvaliado);

}
