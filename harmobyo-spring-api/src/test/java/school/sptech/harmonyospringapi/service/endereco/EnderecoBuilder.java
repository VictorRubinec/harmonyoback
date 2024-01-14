package school.sptech.harmonyospringapi.service.endereco;

import school.sptech.harmonyospringapi.domain.Endereco;

public class EnderecoBuilder {

    public static Endereco criarEndereco() {
        Endereco endereco = new Endereco();
        endereco.setId(1);
        endereco.setCep("00000-000");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setComplemento("Casa");
        endereco.setBairro("Bairro Teste");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("Estado Teste");
        return endereco;

    }

    public static Endereco criarEndereco(Integer id) {
        Endereco endereco = new Endereco();
        endereco.setId(id);
        endereco.setCep("00000-000");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setComplemento("Casa");
        endereco.setBairro("Bairro Teste");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("Estado Teste");
        return endereco;

    }
}
