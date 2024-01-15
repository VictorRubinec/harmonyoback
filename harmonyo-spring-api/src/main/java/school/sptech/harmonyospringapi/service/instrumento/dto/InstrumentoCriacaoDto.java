package school.sptech.harmonyospringapi.service.instrumento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import school.sptech.harmonyospringapi.domain.Naipe;

public class InstrumentoCriacaoDto {

    @Size(min = 3)
    @NotBlank
    private String nome;

    @Min(1)
    @NotNull
    private Integer naipeId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getNaipeId() {
        return naipeId;
    }

    public void setNaipe(Integer naipeId) {
        this.naipeId = naipeId;
    }

    @Override
    public String toString() {
        return "InstrumentoCriacaoDto{" +
                "nome='" + nome + '\'' +
                ", naipeId=" + naipeId +
                '}';
    }
}
