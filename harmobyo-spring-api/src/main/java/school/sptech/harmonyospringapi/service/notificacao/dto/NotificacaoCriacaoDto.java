package school.sptech.harmonyospringapi.service.notificacao.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NotificacaoCriacaoDto {

    @Size(min = 3, max = 50)
    @NotBlank
    private String titulo;

    @Size(min = 3, max = 255)
    @NotBlank
    private String descricao;

    @Min(1)
    @NotNull
    private Integer idUsuario;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}
