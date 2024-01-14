package school.sptech.harmonyospringapi.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "aluno_fk")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "professor_fk")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "status_fk")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "aula_fk")
    private Aula aula;

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

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public double getValorAula() {
        return valorAula;
    }

    public void setValorAula(double valorAula) {
        this.valorAula = valorAula;
    }
}
