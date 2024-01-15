package school.sptech.harmonyospringapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.harmonyospringapi.service.naipe.NaipeService;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeCriacaoDto;
import school.sptech.harmonyospringapi.service.naipe.dto.NaipeExibicaoDto;

import java.util.List;

@RestController
@RequestMapping("/naipes")
@Tag(name = "Controller de naipe para os instrumentos cadastrados na API (Exemplo: Corda, Percussão, Sopro...)")
public class NaipeController {

    @Autowired
    private NaipeService naipeService;

    @Operation( summary = "Lista os naipes encontrados para a exibição")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Naipes encontrados: "),
            @ApiResponse(responseCode = "200", description = "Nao foram encontrados naipes cadastrados ")
    })
    @GetMapping
    public ResponseEntity<List<NaipeExibicaoDto>> listar() {
        List<NaipeExibicaoDto> naipeExibicaoDtos = this.naipeService.listar();

        return naipeExibicaoDtos.isEmpty() ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(naipeExibicaoDtos);
    }

    @Operation( summary = "Cadastra um naipe.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Naipe cadastrados. ")
    })
    @PostMapping
    public ResponseEntity<NaipeExibicaoDto> cadastrar(@RequestBody @Valid NaipeCriacaoDto naipeCriacaoDto) {
        NaipeExibicaoDto naipeExibicaoDto = this.naipeService.cadastrar(naipeCriacaoDto);

        return ResponseEntity.status(201).body(naipeExibicaoDto);
    }
}
