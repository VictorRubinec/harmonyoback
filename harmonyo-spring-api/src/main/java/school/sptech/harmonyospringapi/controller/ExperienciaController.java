package school.sptech.harmonyospringapi.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.harmonyospringapi.service.experiencia.dto.ExperienciaAtualizacaoDto;
import school.sptech.harmonyospringapi.service.experiencia.dto.ExperienciaCriacaoDto;
import school.sptech.harmonyospringapi.service.experiencia.ExperienciaService;
import school.sptech.harmonyospringapi.service.experiencia.dto.ExperienciaExibicaoDto;

import java.util.List;

@RestController
@RequestMapping("/experiencias")
public class ExperienciaController {

    @Autowired
    private ExperienciaService experienciaService;

    @Transactional
    @PutMapping("/atualiza-exp/{id}")
    public ResponseEntity<ExperienciaExibicaoDto> atualizarExperienciaPorId(@PathVariable int id, @RequestBody ExperienciaAtualizacaoDto experiencia){
        ExperienciaExibicaoDto expExibicao = this.experienciaService.atualizarExperienciaPorId(id, experiencia.getTitulo(), experiencia.getDescricao());
        return ResponseEntity.ok().body(expExibicao);
    }

    @PostMapping
    public ResponseEntity<ExperienciaExibicaoDto>  cadastrarExp(@RequestBody ExperienciaCriacaoDto novaExperiencia){
        ExperienciaExibicaoDto expExibicao = this.experienciaService.cadastrarExp(novaExperiencia);
        return ResponseEntity.ok().body(expExibicao);
    }

    @GetMapping("/professor/{id}")
    public ResponseEntity<List<ExperienciaExibicaoDto>> buscarPorId(@PathVariable int id){
        List<ExperienciaExibicaoDto> listExpExibicao = this.experienciaService.buscarExperienciasPorIdProfessor(id);
        return ResponseEntity.ok().body(listExpExibicao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deletarExperienciaPorId(@PathVariable int id){
        this.experienciaService.deletarExperienciaPorId(id);
        return ResponseEntity.ok().build();
    }
}
