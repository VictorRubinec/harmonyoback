package school.sptech.harmonyospringapi.service.naipe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NaipeCriacaoDto {

    @NotBlank
    @Size(min = 3)
    private String descricaoNaipe;

    public String getDescricaoNaipe() {
        return descricaoNaipe;
    }

    public void setDescricaoNaipe(String descricaoNaipe) {
        this.descricaoNaipe = descricaoNaipe;
    }
}
