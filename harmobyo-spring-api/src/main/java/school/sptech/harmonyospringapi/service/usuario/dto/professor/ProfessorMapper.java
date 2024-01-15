package school.sptech.harmonyospringapi.service.usuario.dto.professor;

import org.springframework.beans.factory.annotation.Autowired;
import school.sptech.harmonyospringapi.domain.Professor;
import school.sptech.harmonyospringapi.service.aula.dto.AulaExibicaoDto;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.ProfessorService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class ProfessorMapper {

    public static ProfessorExibicaoResumidoDto of(Professor p,
                                                  List<InstrumentoExibicaoDto> instrumentos,
                                                  double valorMinimo,
                                                  double valorMaximo,
                                                  boolean empresaInstrumento,
                                                  double mediaAvaliacao,
                                                  int qtdeAvaliacoes,
                                                  double distancia,
                                                  String bairro,
                                                  String cidade,
                                                  String estado){
        ProfessorExibicaoResumidoDto dto = new ProfessorExibicaoResumidoDto();

        Integer idade = Period.between(p.getDataNasc(), LocalDate.now()).getYears();

        dto.setId(p.getId());
        dto.setNome(p.getNome());
        dto.setInstrumentos(instrumentos);
        dto.setValorMinimo(valorMinimo);
        dto.setValorMaximo(valorMaximo);
        dto.setDistancia(distancia);
        dto.setBairro(bairro);
        dto.setCidade(cidade);
        dto.setEstado(estado);
        dto.setIdade(idade);
        dto.setEmprestaInstrumento(empresaInstrumento);
        dto.setMediaAvaliacao(mediaAvaliacao);
        dto.setQtdeAvaliacoes(qtdeAvaliacoes);

        return dto;
    }

    public static ProfessorPopularDto ofPopular(Professor p, Double mediaAvaiacao) {
        ProfessorPopularDto dto = new ProfessorPopularDto();

        dto.setId(p.getId());
        dto.setNome(p.getNome());
        dto.setLocalizacao(p.getEndereco().getBairro());
        dto.setMediaAvaliacao(mediaAvaiacao);

        return dto;
    }

}
