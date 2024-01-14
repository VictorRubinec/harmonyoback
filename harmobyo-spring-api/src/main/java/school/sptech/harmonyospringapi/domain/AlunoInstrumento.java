package school.sptech.harmonyospringapi.domain;

import jakarta.persistence.*;

@Entity
public class AlunoInstrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @MapsId("alunoFk")
    @JoinColumn(name = "aluno_fk")
    private Aluno aluno;

    @ManyToOne
    @MapsId("instrumentoFk")
    @JoinColumn(name = "instrumento_fk")
    private Instrumento instrumento;

    private String nivelConhecimento;

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

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }

    public String getNivelConhecimento() {
        return nivelConhecimento;
    }

    public void setNivelConhecimento(String nivelConhecimento) {
        this.nivelConhecimento = nivelConhecimento;
    }
}
