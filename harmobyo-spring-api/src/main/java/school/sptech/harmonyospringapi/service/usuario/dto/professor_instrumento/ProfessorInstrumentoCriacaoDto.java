package school.sptech.harmonyospringapi.service.usuario.dto.professor_instrumento;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProfessorInstrumentoCriacaoDto {

    @Min(1)
    @NotNull
    private Integer instrumentoId;

    @NotBlank
    private String nivelConhecimento;

    @NotNull
    private boolean emprestaInstrumento;

    public Integer getInstrumentoId() {
        return instrumentoId;
    }

    public void setInstrumentoId(Integer instrumentoId) {
        this.instrumentoId = instrumentoId;
    }

    public String getNivelConhecimento() {
        return nivelConhecimento;
    }

    public void setNivelConhecimento(String nivelConhecimento) {
        this.nivelConhecimento = nivelConhecimento;
    }

    public boolean isEmprestaInstrumento() {
        return emprestaInstrumento;
    }

    public void setEmprestaInstrumento(boolean emprestaInstrumento) {
        this.emprestaInstrumento = emprestaInstrumento;
    }
}
