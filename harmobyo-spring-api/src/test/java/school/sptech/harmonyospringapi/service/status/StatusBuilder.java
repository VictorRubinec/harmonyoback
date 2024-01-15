package school.sptech.harmonyospringapi.service.status;

import school.sptech.harmonyospringapi.domain.Status;

public class StatusBuilder {

    public static Status criarStatus() {
        Status status = new Status();
        status.setId(1);
        status.setDescricao("Aguardando confirmação");
        return status;
    }

    public static Status criarStatus(int id, String descricao) {
        Status status = new Status();
        status.setId(id);
        status.setDescricao(descricao);
        return status;
    }
}
