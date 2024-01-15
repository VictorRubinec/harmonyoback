package school.sptech.harmonyospringapi.service.pedido.dto;

import school.sptech.harmonyospringapi.service.aula.dto.AulaExibicaoDto;
import school.sptech.harmonyospringapi.service.status.dto.StatusExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.UsuarioExibicaoDto;

import java.time.LocalDateTime;

public class PedidoExibicaoDto {

    private Integer id;

    private UsuarioExibicaoDto aluno;

    private UsuarioExibicaoDto professor;

    private StatusExibicaoDto status;

    private AulaExibicaoDto aula;

    private LocalDateTime horaCriacao;

    private LocalDateTime horaResposta;

    private LocalDateTime dataAula;

    private double valorAula;

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

    public UsuarioExibicaoDto getProfessor() {
        return professor;
    }

    public void setProfessor(UsuarioExibicaoDto professor) {
        this.professor = professor;
    }

    public StatusExibicaoDto getStatus() {
        return status;
    }

    public void setStatus(StatusExibicaoDto status) {
        this.status = status;
    }

    public LocalDateTime getHoraCriacao() {
        return horaCriacao;
    }

    public void setHoraCriacao(LocalDateTime horaCriacao) {
        this.horaCriacao = horaCriacao;
    }

    public LocalDateTime getHoraResposta() {
        return horaResposta;
    }

    public void setHoraResposta(LocalDateTime horaResposta) {
        this.horaResposta = horaResposta;
    }

    public LocalDateTime getDataAula() {
        return dataAula;
    }

    public void setDataAula(LocalDateTime dataAula) {
        this.dataAula = dataAula;
    }

    public AulaExibicaoDto getAula() {
        return aula;
    }

    public void setAula(AulaExibicaoDto aula) {
        this.aula = aula;
    }

    public double getValorAula() {
        return valorAula;
    }

    public void setValorAula(double valorAula) {
        this.valorAula = valorAula;
    }
}
