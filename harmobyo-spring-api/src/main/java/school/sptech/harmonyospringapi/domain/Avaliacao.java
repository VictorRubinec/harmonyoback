package school.sptech.harmonyospringapi.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double valor;

    private String comentario;

    @ManyToOne
    @JoinColumn(name = "usuario_avaliado_fk")
    private Usuario usuarioAvaliado;

    private LocalDate dataAvaliacao;

    @ManyToOne
    @JoinColumn(name = "usuario_avaliador_fk")
    private Usuario usuarioAvaliador;

    @ManyToOne
    @JoinColumn(name = "pedido_fk")
    private Pedido pedido;

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

    public Usuario getUsuarioAvaliado() {
        return usuarioAvaliado;
    }

    public void setUsuarioAvaliado(Usuario usuarioAvaliado) {
        this.usuarioAvaliado = usuarioAvaliado;
    }

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDate dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public Usuario getUsuarioAvaliador() {
        return usuarioAvaliador;
    }

    public void setUsuarioAvaliador(Usuario usuarioAvaliador) {
        this.usuarioAvaliador = usuarioAvaliador;
    }

    public Pedido getPedidoAula() {
        return pedido;
    }

    public void setPedidoAula(Pedido pedido) {
        this.pedido = pedido;
    }
}
