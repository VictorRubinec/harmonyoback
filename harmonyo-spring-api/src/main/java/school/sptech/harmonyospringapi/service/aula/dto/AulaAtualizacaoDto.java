package school.sptech.harmonyospringapi.service.aula.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AulaAtualizacaoDto {

    @DecimalMin("0.0")
    @NotNull
    private Double valorAula;

    public Double getValorAula() {
        return valorAula;
    }

    public void setValorAula(Double valorAula) {
        this.valorAula = valorAula;
    }
}
