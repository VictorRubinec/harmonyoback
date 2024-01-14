package school.sptech.harmonyospringapi.service.endereco.dto;

import school.sptech.harmonyospringapi.domain.Endereco;

public class EnderecoMapper {

    public static Endereco of(EnderecoExibicaoDto dto) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCidade(dto.getCidade());
        endereco.setBairro(dto.getBairro());
        return endereco;
    }

    public static EnderecoExibicaoDto ofExibicaoDto(Endereco endereco) {
        EnderecoExibicaoDto dto = new EnderecoExibicaoDto();
        dto.setLogradouro(endereco.getLogradouro());
        dto.setNumero(endereco.getNumero());
        dto.setComplemento(endereco.getComplemento());
        dto.setCidade(endereco.getCidade());
        dto.setBairro(endereco.getBairro());
        return dto;
    }

    public static Endereco of(EnderecoAtualizacaoDto dto) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCidade(dto.getCidade());
        endereco.setBairro(dto.getBairro());
        return endereco;
    }

}
