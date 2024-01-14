package school.sptech.harmonyospringapi.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double valorAula;

    @ManyToOne
    @JoinColumn(name = "professor_fk")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "instrumento_fk")
    private Instrumento instrumento;

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

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
}
