package school.sptech.harmonyospringapi.service.status.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import school.sptech.harmonyospringapi.domain.Pedido;

import java.util.List;

public class StatusCriacaoDto {

    @NotBlank
    @Size(min=3)
    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
