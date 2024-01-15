package school.sptech.harmonyospringapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.harmonyospringapi.domain.Instrumento;
import school.sptech.harmonyospringapi.repository.InstrumentoRepository;
import school.sptech.harmonyospringapi.service.instrumento.InstrumentoService;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoCriacaoDto;
import school.sptech.harmonyospringapi.service.instrumento.dto.InstrumentoExibicaoDto;
import school.sptech.harmonyospringapi.utils.ArvoreBin;
import school.sptech.harmonyospringapi.utils.Node;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/instrumentos")
@Tag(name = "Controller de instrumentos para os instrumentos cadastrados na API (Exemplo: Violão, Piano, Xilofone)")
public class InstrumentoController {

    @Autowired
    private InstrumentoService instrumentoService;

    ArvoreBin arvoreBin = new ArvoreBin();

    @Operation( summary = "Cadastra um instrumento na plataforma a partir de uma DTO")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instrumento cadastrado. "),
            @ApiResponse(responseCode = "409", description = "Não foi possível cadastrar o instrumento. Verifique se as " +
                    "instruções do mesmo foram cadastradas corretamente ou se o instrumento já foi cadastrado")
    })
    @SecurityRequirement(name = "Bearer")
    @PostMapping
    public ResponseEntity<InstrumentoExibicaoDto> cadastrar(@RequestBody @Valid InstrumentoCriacaoDto instrumentoCriacaoDto) {
        InstrumentoExibicaoDto instrumentoExibicaoDto = this.instrumentoService.cadastrar(instrumentoCriacaoDto);

        return instrumentoExibicaoDto == null ? ResponseEntity.status(409).build() : ResponseEntity.status(201).body(instrumentoExibicaoDto);
    }

    @Operation( summary = "Lista os instrumentos cadastrados na plataforma")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista dos instrumentos cadastrados :  "),
            @ApiResponse(responseCode = "409", description = "Não foram encontrados instrumentos. ")
    })
    @SecurityRequirement(name = "Bearer")
    @GetMapping
    public ResponseEntity<List<InstrumentoExibicaoDto>> listar() {
        List<InstrumentoExibicaoDto> instrumentoExibicaoDtos = this.instrumentoService.listar();

        return instrumentoExibicaoDtos.isEmpty() ? ResponseEntity.status(204).build() : ResponseEntity.status(200).body(instrumentoExibicaoDtos);
    }


    @GetMapping("/arvore-binaria")
    public ResponseEntity<Boolean> exibirArvoreBinaria() {
        List<InstrumentoExibicaoDto> listInstrumento = this.instrumentoService.listar();

        if (!listInstrumento.isEmpty()) {

            arvoreBin.criaRaiz(listInstrumento.get(0));

            for (int i = 1; i < listInstrumento.size(); i++) {
                InstrumentoExibicaoDto instrumento = listInstrumento.get(i);
                Node<InstrumentoExibicaoDto> raiz = arvoreBin.getRaiz();

                insereInstrumentoNaArvore(raiz, instrumento);
            }

            arvoreBin.exibeArvore(arvoreBin.getRaiz());
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.noContent().build();
    }
    private void insereInstrumentoNaArvore(Node<InstrumentoExibicaoDto> noDaVez, InstrumentoExibicaoDto instrumento) {
        if (instrumento.getId().compareTo(noDaVez.getInfo().getId()) < 0) {
            if (noDaVez.getEsq() == null) {
                arvoreBin.insereEsq(noDaVez, instrumento);
            } else {
                insereInstrumentoNaArvore(noDaVez.getEsq(), instrumento);
            }
        } else {
            if (noDaVez.getDir() == null) {
                arvoreBin.insereDir(noDaVez, instrumento);
            } else {
                insereInstrumentoNaArvore(noDaVez.getDir(), instrumento);
            }
        }
    }
}
