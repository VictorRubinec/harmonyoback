package school.sptech.harmonyospringapi.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descricao;

    /*
        Status Possíveis:
        Pendente,
        Confirmado,
        Cancelado,
        Recusado,
        Concluído,
        Aguardando Pagamento;
    * */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
