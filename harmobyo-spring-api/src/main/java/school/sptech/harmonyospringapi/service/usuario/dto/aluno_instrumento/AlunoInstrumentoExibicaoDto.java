package school.sptech.harmonyospringapi.service.usuario.dto.aluno_instrumento;

import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioExibicaoDto;

public class AlunoInstrumentoExibicaoDto {

    private Integer id;

    private UsuarioExibicaoDto aluno;

    private InstrumentoExibicaoDto instrumento;

    private String nivelConhecimento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsuarioExibicaoDto getAluno() {
        return aluno;
    }

    public void setAluno(UsuarioExibicaoDto aluno) {
        this.aluno = aluno;
    }

    public InstrumentoExibicaoDto getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(InstrumentoExibicaoDto instrumento) {
        this.instrumento = instrumento;
    }

    public String getNivelConhecimento() {
        return nivelConhecimento;
    }

    public void setNivelConhecimento(String nivelConhecimento) {
        this.nivelConhecimento = nivelConhecimento;
    }
}
