package school.sptech.harmonyospringapi.service.naipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.domain.Naipe;
import school.sptech.harmonyospringapi.repository.NaipeRepository;
import school.sptech.harmonyospringapi.service.exceptions.EntidadeConflitanteException;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeCriacaoDto;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeExibicaoDto;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeMapper;

import java.util.List;

@Service
public class NaipeService {

    @Autowired
    private NaipeRepository naipeRepository;

    public NaipeExibicaoDto cadastrar(NaipeCriacaoDto naipeCriacaoDto) {
        if (this.naipeRepository.existsNaipeByDescricaoIgnoreCase(naipeCriacaoDto.getDescricaoNaipe())) throw new EntidadeConflitanteException("Erro ao cadastrar. Naipe já cadastrado!");

        Naipe naipe = this.naipeRepository.save(NaipeMapper.of(naipeCriacaoDto));

        return NaipeMapper.ofNaipeExibicao(naipe);
    }

    public List<NaipeExibicaoDto> listar() {
        return this.naipeRepository.findAll()
                .stream()
                .map(NaipeMapper::ofNaipeExibicao)
                .toList();
    }

    public Naipe buscarPorId(Integer id) {
        return this.naipeRepository.findById(id).orElseThrow(() -> new EntitadeNaoEncontradaException("Naipe com id inexistente"));
    }
}
