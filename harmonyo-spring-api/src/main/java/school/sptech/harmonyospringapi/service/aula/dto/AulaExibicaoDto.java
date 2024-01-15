package school.sptech.harmonyospringapi.service.aula.dto;

import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioExibicaoDto;

public class AulaExibicaoDto {

    private Integer id;

    private Double valorAula;

    private InstrumentoExibicaoDto instrumento;

    private boolean ativa;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValorAula() {
        return valorAula;
    }

    public void setValorAula(Double valorAula) {
        this.valorAula = valorAula;
    }

    public InstrumentoExibicaoDto getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(InstrumentoExibicaoDto instrumento) {
        this.instrumento = instrumento;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
}
