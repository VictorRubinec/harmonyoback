package school.sptech.harmonyospringapi.service.experiencia.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExperienciaCriacaoDto {

    @Size(min = 3)
    @NotBlank
    private String titulo;

    @Size(min = 3)
    @NotBlank
    private String descricao;

    @Min(1)
    @NotNull
    private Integer idProfessor;

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

    public Integer getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(Integer idProfessor) {
        this.idProfessor = idProfessor;
    }
}
