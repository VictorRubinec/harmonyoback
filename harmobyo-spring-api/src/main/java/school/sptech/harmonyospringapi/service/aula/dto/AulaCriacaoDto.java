package school.sptech.harmonyospringapi.service.aula.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AulaCriacaoDto {

    @DecimalMin("0.0")
    @NotNull
    private Double valorAula;

    @Min(1)
    @NotNull
    private Integer professorId;

    @Min(1)
    @NotNull
    private Integer instrumentoId;

    public Double getValorAula() {
        return valorAula;
    }

    public void setValorAula(Double valorAula) {
        this.valorAula = valorAula;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public Integer getInstrumentoId() {
        return instrumentoId;
    }

    public void setInstrumentoId(Integer instrumentoId) {
        this.instrumentoId = instrumentoId;
    }
}
