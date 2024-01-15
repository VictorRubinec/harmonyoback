package school.sptech.harmonyospringapi.service.usuario.dto.avaliacao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AvaliacaoCardDto {
    private Integer idAvaliador;

    private String nomeAvaliador;

    private String comentario;

    private Double valorAvaliacao;
    private String dataAvaliacaoFormatada;

    public Integer getIdAvaliador() {
        return idAvaliador;
    }

    public void setIdAvaliador(Integer idAvaliador) {
        this.idAvaliador = idAvaliador;
    }

    public String getNomeAvaliador() {
        return nomeAvaliador;
    }

    public void setNomeAvaliador(String nomeAvaliador) {
        this.nomeAvaliador = nomeAvaliador;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Double getValorAvaliacao() {
        return valorAvaliacao;
    }

    public void setValorAvaliacao(Double valorAvaliacao) {
        this.valorAvaliacao = valorAvaliacao;
    }
    public String getDataAvaliacaoFormatada() {
        return dataAvaliacaoFormatada;
    }

    public void setDataAvaliacaoFormatada(String dataAvaliacaoFormatada) {
        this.dataAvaliacaoFormatada = dataAvaliacaoFormatada;
    }


}
