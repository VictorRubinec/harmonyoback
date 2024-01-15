package school.sptech.harmonyospringapi.service.instrumento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.domain.Naipe;
import school.sptech.harmonyospringapi.repository.InstrumentoRepository;
import school.sptech.harmonyospringapi.repository.NaipeRepository;
import school.sptech.harmonyospringapi.service.exceptions.EntidadeConflitanteException;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoCriacaoDto;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoMapper;
import school.sptech.harmonyospringapi.service.naipe.NaipeService;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentoService {

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private NaipeService naipeService;


    public InstrumentoExibicaoDto cadastrar(InstrumentoCriacaoDto instrumentoCriacaoDto) {
        if (this.instrumentoRepository.existsInstrumentoByNomeIgnoreCase(instrumentoCriacaoDto.getNome())) throw new EntidadeConflitanteException("Erro ao cadastrar. Instrumento já cadastrado!");
        Naipe naipe = this.naipeService.buscarPorId(instrumentoCriacaoDto.getNaipeId());

        Instrumento novoInstrumento = InstrumentoMapper.of(instrumentoCriacaoDto, naipe);

        return InstrumentoMapper.ofInstrumentoExibicao(this.instrumentoRepository.save(novoInstrumento));
    }

    public List<InstrumentoExibicaoDto> listar() {
        return this.instrumentoRepository.findAll().stream().map(InstrumentoMapper::ofInstrumentoExibicao).toList();
    }

    /* ============= PESQUISA ================ */
    public Instrumento buscarPorId(Integer id) {
        Optional<Instrumento> optionalInstrumento = this.instrumentoRepository.findById(id);

        if (optionalInstrumento.isEmpty()) throw new EntitadeNaoEncontradaException("Instrumento com id inexistente");

        return optionalInstrumento.get();
    }
}
