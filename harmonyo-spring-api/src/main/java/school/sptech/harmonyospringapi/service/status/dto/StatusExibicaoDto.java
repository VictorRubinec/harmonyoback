package school.sptech.harmonyospringapi.service.status.dto;

import school.sptech.harmonyospringapi.domain.Pedido;

import java.util.List;

public class StatusExibicaoDto {

    private Integer id;

    private String descricao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
