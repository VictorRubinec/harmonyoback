package school.sptech.harmonyospringapi.service.usuario.dto;

import school.sptech.harmonyospringapi.service.usuario.dto.avaliacao.AvaliacaoCardDto;

import java.util.List;

public class UsuarioTelaFeedback {

    private Integer id;

    private String nome;

    private Double avaliacaoMedia;

    private List<AvaliacaoCardDto> avaliacoes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(Double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public List<AvaliacaoCardDto> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoCardDto> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}
