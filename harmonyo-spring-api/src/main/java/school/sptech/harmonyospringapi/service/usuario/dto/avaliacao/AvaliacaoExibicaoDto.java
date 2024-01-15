package school.sptech.harmonyospringapi.service.usuario.dto.avaliacao;

import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioExibicaoDto;

import java.time.LocalDate;

public class AvaliacaoExibicaoDto {
    private Integer id;

    private Double valor;

    private String comentario;

    private LocalDate dataAvaliacao;

    private UsuarioExibicaoDto usuarioAvaliador;

    private PedidoExibicaoDto pedidoAula;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDate dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public UsuarioExibicaoDto getUsuarioAvaliador() {
        return usuarioAvaliador;
    }

    public void setUsuarioAvaliador(UsuarioExibicaoDto usuarioAvaliador) {
        this.usuarioAvaliador = usuarioAvaliador;
    }

    public PedidoExibicaoDto getPedidoAula() {
        return pedidoAula;
    }

    public void setPedidoAula(PedidoExibicaoDto pedidoAula) {
        this.pedidoAula = pedidoAula;
    }
}
