package school.sptech.harmonyospringapi.service.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioBibliografiaDto {


    @NotBlank
    @Size(min = 20, max = 500)
    private String bibliografia;

    public String getBibliografia() {
        return bibliografia;
    }

    public void setBibliografia(String bibliografia) {
        this.bibliografia = bibliografia;
    }
}
