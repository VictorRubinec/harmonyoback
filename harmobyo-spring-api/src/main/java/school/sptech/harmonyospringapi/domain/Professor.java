package school.sptech.harmonyospringapi.domain;

import jakarta.persistence.*;
import school.sptech.harmonyospringapi.utils.FilaObj;

import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("Professor")
public class Professor extends Usuario {

    @OneToMany(mappedBy = "professor")
    private List<Aula> aulas;

    @OneToMany(mappedBy = "usuarioAvaliado")
    private List<Avaliacao> avaliacoesRecebidas;

    @OneToMany(mappedBy = "usuarioAvaliador")
    private List<Avaliacao> avaliacoesFeitas;

    @OneToMany(mappedBy = "professor")
    private List<ProfessorInstrumento> instrumentos;

    @OneToOne
    private Endereco endereco;
    

}
