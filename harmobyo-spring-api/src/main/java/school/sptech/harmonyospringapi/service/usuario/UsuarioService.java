package school.sptech.harmonyospringapi.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.harmonyospringapi.configuration.security.jwt.GerenciadorTokenJwt;
import school.sptech.harmonyospringapi.domain.*;
import school.sptech.harmonyospringapi.repository.*;
import school.sptech.harmonyospringapi.service.endereco.dto.EnderecoAtualizacaoDto;
import school.sptech.harmonyospringapi.service.experiencia.dto.ExperienciaMapper;
import school.sptech.harmonyospringapi.service.experiencia.dto.ExperienciaExibicaoDto;
import school.sptech.harmonyospringapi.service.pedido.PedidoService;
import school.sptech.harmonyospringapi.service.usuario.dto.*;
import school.sptech.harmonyospringapi.service.usuario.dto.avaliacao.AvaliacaoCardDto;
import school.sptech.harmonyospringapi.service.usuario.dto.avaliacao.AvaliacaoCriacaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.avaliacao.AvaliacaoExibicaoDto;
import school.sptech.harmonyospringapi.service.usuario.dto.avaliacao.AvaliacaoMapper;
import school.sptech.harmonyospringapi.utils.ListaGenericaObj;
import school.sptech.harmonyospringapi.service.exceptions.EntitadeNaoEncontradaException;
import school.sptech.harmonyospringapi.service.endereco.EnderecoService;
import school.sptech.harmonyospringapi.service.endereco.dto.EnderecoExibicaoDto;
import school.sptech.harmonyospringapi.service.endereco.dto.EnderecoMapper;
import school.sptech.harmonyospringapi.service.usuario.autenticacao.dto.UsuarioLoginDto;
import school.sptech.harmonyospringapi.service.usuario.autenticacao.dto.UsuarioTokenDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Autowired
    private ExperienciaRepository experienciaRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    @Lazy
    private PedidoService pedidoService;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;


    public List<UsuarioExibicaoDto> listarCadastrados() {

        List<Usuario> ltUsuarios = this.usuarioRepository.findAll();

        return ltUsuarios.stream().map(UsuarioMapper::ofUsuarioExibicao).toList();
    }


    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {

        final UsernamePasswordAuthenticationToken credentials =
                new UsernamePasswordAuthenticationToken(
                        usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha()
                );

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado =
                usuarioRepository.findByEmail(usuarioLoginDto.getEmail())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email de usuário não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);

    }

    public List<UsuarioExibicaoDto> exibeTodosOrdemAlfabetica() {

        List<Usuario> ltUsuarios = this.usuarioRepository.findAll();


        ListaGenericaObj<Usuario> ltUsuariosGenerica = new ListaGenericaObj<>(ltUsuarios.size());

        ltUsuarios.forEach(ltUsuariosGenerica::adiciona);


        ltUsuariosGenerica = new UsuarioComparador(ltUsuariosGenerica).ordenacaoAlfabetica();

        ltUsuarios.clear();

        for (int i = 0; i < ltUsuariosGenerica.size(); i++) {
            ltUsuarios.add(ltUsuariosGenerica.getElemento(i));
        }

        return ltUsuarios.stream().map(UsuarioMapper::ofUsuarioExibicao).toList();
    }

    public Integer quantidadeCadastradosUsuarios(LocalDateTime dataComeco, LocalDateTime dataFim) {
        return this.usuarioRepository.obterQuantidadeUsuario(dataComeco, dataFim);
    }

    /* ================ PESQUISA ================ */

    public boolean existeUsuarioPorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existeUsuarioPorCpf(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntitadeNaoEncontradaException("Usuário não encontrado")
        );
    }

    /* ================ ENDEREÇO ================ */

    public UsuarioExibicaoDto inserirEndereco(Integer idUsuario, Endereco endereco) {
        Endereco enderecoInserido = enderecoService.cadastrarEndereco(endereco);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new EntitadeNaoEncontradaException("Usuário não encontrado")
        );
        usuario.setEndereco(enderecoInserido);
        usuarioRepository.save(usuario);
        return UsuarioMapper.ofUsuarioExibicao(usuario);
    }

    public UsuarioExibicaoDto atualizarEndereco(Integer idUsuario, EnderecoAtualizacaoDto enderecoAtualizacaoDto) {
        Usuario usuario = buscarPorId(idUsuario);

        Endereco endereco = EnderecoMapper.of(enderecoAtualizacaoDto);
        endereco.setId(usuario.getEndereco().getId());

        Endereco enderecoInserido = this.enderecoService.atualizarEndereco(endereco);
        usuario.setEndereco(enderecoInserido);

        this.usuarioRepository.save(usuario);
        return UsuarioMapper.ofUsuarioExibicao(usuario);
    }

    public void deletarEndereco(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new EntitadeNaoEncontradaException("Usuário não encontrado")
        );
        usuario.setEndereco(null);
        usuarioRepository.save(usuario);
        Endereco endereco = usuario.getEndereco();
        enderecoService.deletarEndereco(endereco);
    }

    public EnderecoExibicaoDto buscarEndereco(Integer idUsuario) {
        Usuario usuario = buscarPorId(idUsuario);

        return EnderecoMapper.ofExibicaoDto(usuario.getEndereco());
    }

    /* ================ AVALIAÇÃO ================ */

    public boolean existeAvaliacaoNoPedidoPorUsuarioAutor(Integer idPedido, Integer idUsuarioAutor){
        Usuario autor = buscarPorId(idUsuarioAutor);

        return avaliacaoRepository.existsAvaliacaoByPedidoIdAndUsuarioAvaliador(idPedido, autor);
    }

    public AvaliacaoExibicaoDto criarAvaliacao(Integer idAvaliado, AvaliacaoCriacaoDto avaliacaoCriacaoDto) {
        Usuario receptor = buscarPorId(idAvaliado);
        Usuario autor = buscarPorId(avaliacaoCriacaoDto.getUsuarioAvaliadorId());
        Pedido pedido = pedidoService.buscarPorId(avaliacaoCriacaoDto.getPedidoId());



        if (receptor.getId().equals(autor.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível avaliar a si mesmo");
        } else if (!Objects.equals(pedido.getStatus().getDescricao(), "Concluído")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido não foi concluído");
        } else if (avaliacaoRepository.existsAvaliacaoByPedidoIdAndUsuarioAvaliador(pedido.getId(), autor)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido já foi avaliado");
        } else if ((!pedido.getAluno().getId().equals(receptor.getId())) && (!pedido.getProfessor().getId().equals(receptor.getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido não pertence ao usuário receptor");
        } else if (receptor instanceof Aluno) {
            if (autor instanceof Aluno) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aluno não pode avaliar outro aluno");
            } else if ((!pedido.getAluno().getId().equals(autor.getId())) && (!pedido.getProfessor().getId().equals(autor.getId()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido não pertence ao usuário autor");
            }
        } else if (receptor instanceof Professor) {
            if (autor instanceof Professor) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Professor não pode avaliar outro professor");
            } else if ((!pedido.getAluno().getId().equals(autor.getId())) && (!pedido.getProfessor().getId().equals(autor.getId()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido não pertence ao usuário autor");
            }
        }
        System.out.println("Avaliação criada");
        Avaliacao avaliacao = AvaliacaoMapper.of(avaliacaoCriacaoDto, receptor, autor, pedido);

        Avaliacao avaliacaoCadastrada = this.avaliacaoRepository.save(avaliacao);
        System.out.println("Avaliação autor: " + avaliacaoCadastrada.getUsuarioAvaliador().getNome());
        System.out.println("Avaliação receptor: " + avaliacaoCadastrada.getUsuarioAvaliado().getNome());
        System.out.println("Avaliação pedido: " + avaliacaoCadastrada.getPedidoAula().getId());
        System.out.println("Avaliação cadastrada");
        return AvaliacaoMapper.ofAvaliacaoExibicao(avaliacaoCadastrada);
    }

    public List<AvaliacaoExibicaoDto> listarAvaliacoesPorUsuario(Integer idUsuario) {
        Usuario usuario = buscarPorId(idUsuario);

        List<Avaliacao> avaliacoes = this.avaliacaoRepository.findByUsuarioAvaliadoId(usuario.getId());

        return avaliacoes.stream().map(AvaliacaoMapper::ofAvaliacaoExibicao).toList();
    }

    public UsuarioTelaFeedback obterDadosUsuarioTelaFeedback(Integer idUsuario) {

        if (!this.usuarioRepository.existsById(idUsuario)) throw new EntitadeNaoEncontradaException("ID de Usuário invalido. Usuário não encontrado !");

        Usuario usuario = buscarPorId(idUsuario);

        List<AvaliacaoCardDto> avaliacoes = this.avaliacaoRepository.findByUsuarioAvaliadoId(usuario.getId())
                                                                    .stream().map(AvaliacaoMapper::ofAvaliacaoCard)
                                                                    .toList();

        Double avaliacaoMedia = this.avaliacaoRepository.getMediaAvaliacaoUsuario(idUsuario).orElse(0.0);

        return UsuarioMapper.ofUsuarioTelaFeedback(usuario, avaliacoes, avaliacaoMedia);
    }

    public UsuarioDadosPerfilDto obterDadosPerfilUsuario(int id) {

        Optional<Usuario> usuario = this.usuarioRepository.findById(id);

        if (usuario.isPresent()) {

            Usuario usuarioEncontrado = usuario.get();

            List<ExperienciaExibicaoDto> experiencias = new ArrayList<>();

            Double avaliacaoMedia = this.avaliacaoRepository.getMediaAvaliacaoUsuario(id).orElse(0.0);

            if (usuarioEncontrado instanceof Professor){
                experiencias = this.experienciaRepository.findAllByProfessorId(id).stream().map(ExperienciaMapper::of).toList();
            }

            String categoriaUsuario = usuarioEncontrado instanceof Professor ? "Professor": "Aluno";

            return UsuarioMapper.ofDadosPerfilUsuario(usuarioEncontrado, experiencias, avaliacaoMedia, categoriaUsuario);

        }

        throw new EntitadeNaoEncontradaException("ID de Usuário invalido. Usuário não encontrado !");
    }

    /* ================ UTILIDADE ===============*/

    public FiltroMinimoMaximo filtroMinimoMaximo() {
        Double precoMinimo = this.usuarioRepository.obterPrecoMinimo();
        Double precoMaximo = this.usuarioRepository.obterPrecoMaximo();

        FiltroMinimoMaximo filtroMinimoMaximo = new FiltroMinimoMaximo();

        filtroMinimoMaximo.setPrecoMinimo(precoMinimo);
        filtroMinimoMaximo.setPrecoMaximo(precoMaximo);
        filtroMinimoMaximo.setDistanciaMinima(0.0);
        filtroMinimoMaximo.setDistanciaMaxima(100.0);

        return filtroMinimoMaximo;
    }

    /* ================ ATUALIZAR DADOS PESSOAIS ===============*/

    public void atualizarDadosPessoais(int id, UsuarioAtulizarDadosPessoaisDto dadosUsuario) {

         if (usuarioRepository.existsById(id)){
             usuarioRepository.atualizarDadosPessoais(id, dadosUsuario.getNome(), dadosUsuario.getEmail(), dadosUsuario.getDataNasc(), dadosUsuario.getSexo());
         }
         else {
             throw new EntitadeNaoEncontradaException("ID de Usuário Inválido!");
         }
    }

    public void atualizarBibliografia(int id, String bibliografia) {
        if (usuarioRepository.existsById(id)){
            this.usuarioRepository.atualizarBibliografia(id, bibliografia);
        }
        else {
            throw new EntitadeNaoEncontradaException("ID de Usuário Inválido!");
        }
    }

    /* ================ METRICAS DASHBOARD ADMIN ===============*/

    public List<Integer> obterQuantidadeUsuariosCadastradosSemana() {
        List<Integer> ltQtdUsuario = new ArrayList<>();

        LocalDateTime dataAtual = obterPrimeiroDiaSemana();
        LocalDateTime dataInicialAux, dataFinalAux;

        for (int i = 0; i < 7; i++) {
            dataInicialAux = dataAtual.withHour(0).withMinute(0).withSecond(0);
            dataFinalAux = dataAtual.withHour(23).withMinute(59).withSecond(59);

            ltQtdUsuario.add(this.usuarioRepository.obterQuantidadeUsuariosCadastradosEntre(dataInicialAux, dataFinalAux));


            dataAtual = dataAtual.plusDays(1);
        }

        return ltQtdUsuario;
    }

    public List<Integer> obterQuantidadeUsuariosRetidosSemana() {
        List<Integer> ltQtdUsuario = new ArrayList<>();

        LocalDateTime dataAtual = obterPrimeiroDiaSemana();
        LocalDateTime dataInicialAux, dataFinalAux;

        for (int i = 0; i < 7; i++) {
            dataInicialAux = dataAtual.withHour(0).withMinute(0).withSecond(0);
            dataFinalAux = dataAtual.withHour(23).withMinute(59).withSecond(59);

            ltQtdUsuario.add(this.usuarioRepository.obterQuantidadeUsuariosRetidosEntre(dataInicialAux, dataFinalAux));


            dataAtual = dataAtual.plusDays(1);
        }

        return ltQtdUsuario;
    }

    public List<Integer> obterUsuariosCadastradosMes() {
        LocalDateTime dataInicial = obterPrimeiroDiaMes();
        LocalDateTime dataAtual = dataInicial;
        LocalDateTime dataFinal = obterUltimoDiaMes();

        List<Integer> ltQtdUsuario = new ArrayList<>();
        int difencaDias = dataFinal.getDayOfMonth() - dataInicial.getDayOfMonth();

        LocalDateTime dataInicialAux, dataFinalAux;

        for(int i = 0; i <= difencaDias; i++) {
            dataInicialAux = dataAtual.withHour(0).withMinute(0).withSecond(0);
            dataFinalAux = dataAtual.withHour(23).withMinute(59).withSecond(59);

            ltQtdUsuario.add(this.usuarioRepository.obterQuantidadeUsuariosCadastradosEntre(dataInicialAux, dataFinalAux));

            dataAtual = dataAtual.plusDays(1);
        }

        return ltQtdUsuario;
    }

    public List<Integer> obterUsuariosCadastradosMesAnterior() {
        LocalDateTime dataInicial = obterPrimeiroDiaMes().minusMonths(1);
        LocalDateTime dataAtual = dataInicial;
        LocalDateTime dataFinal = obterUltimoDiaMes().minusMonths(1);

        List<Integer> ltQtdUsuario = new ArrayList<>();
        int difencaDias = dataFinal.getDayOfMonth() - dataInicial.getDayOfMonth();

        LocalDateTime dataInicialAux, dataFinalAux;

        for(int i = 0; i <= difencaDias; i++) {
            dataInicialAux = dataAtual.withHour(0).withMinute(0).withSecond(0);
            dataFinalAux = dataAtual.withHour(23).withMinute(59).withSecond(59);

            ltQtdUsuario.add(this.usuarioRepository.obterQuantidadeUsuariosCadastradosEntre(dataInicialAux, dataFinalAux));

            dataAtual = dataAtual.plusDays(1);
        }

        return ltQtdUsuario;
    }

    private LocalDateTime obterPrimeiroDiaSemana() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        LocalDateTime dataInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();

        dataInicial = dataInicial.withHour(0).withMinute(0).withSecond(0);

        return dataInicial;
    }

    private LocalDateTime obterUltimoDiaSemana() {
        LocalDateTime dataInicial = obterPrimeiroDiaSemana();

        return dataInicial.plusDays(6).withHour(23).withMinute(59).withSecond(59);
    }

    private LocalDateTime obterPrimeiroDiaMes() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_MONTH, 1);

        LocalDateTime dataInicial = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();
        dataInicial = dataInicial.withHour(0).withMinute(0).withSecond(0);

        return dataInicial;
    }

    private LocalDateTime obterUltimoDiaMes() {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

        LocalDateTime dataFinal = c.getTime().toInstant().atZone(c.getTimeZone().toZoneId()).toLocalDateTime();
        dataFinal = dataFinal.withHour(23).withMinute(59).withSecond(59);

        return dataFinal;
    }

    public boolean lerSalvarTxt(BufferedReader entrada) {
        String registro, tipoRegistro;
        int id, contaRegDadosGravados = 0, contaRegDadosLidos = 0;
        String nomeAluno, email, genero, instrumento, telefone, cpf, logradouro, numero, complemento, cidade, bairro;
        String nomeProfessor, exp;
        List<Aluno> listaLida = new ArrayList<>();
        System.out.println("aloo");
        try {
            registro = entrada.readLine();
            while (registro != null) {
                tipoRegistro = registro.substring(0, 2);
                if (tipoRegistro.equals("00")) {
                    System.out.println("Tipo do arquivo: " + registro.substring(2, 6));
                    System.out.println("Data e hora da gravação: " + registro.substring(6, 25));
                    System.out.println("Versão do documento de layout: " + registro.substring(25, 27));
                } else if (tipoRegistro.equals("01")) {
                    contaRegDadosGravados = Integer.parseInt(registro.substring(2, 7));
                    if (contaRegDadosGravados == contaRegDadosLidos) {
                        System.out.println("Quantidade de registros de dados lidos eh compativel com " +
                                "quantidade de registros de dados gravados");
                    } else {
                        System.out.println("Quantidade de registros de dados lidos eh incompativel com " +
                                "quantidade de registros de dados gravados");
                    }
                } else if (tipoRegistro.equals("02")) {

                    nomeAluno = registro.substring(5, 45).trim();
                    email = registro.substring(45, 95).trim();
                    genero = registro.substring(95, 96).trim();
                    telefone = registro.substring(96, 111).trim();
                    cpf = registro.substring(111, 125).trim();
                    logradouro = registro.substring(125, 155).trim();
                    numero = registro.substring(155, 160).trim();
                    complemento = registro.substring(160, 190).trim();
                    cidade = registro.substring(190, 210).trim();
                    bairro = registro.substring(210, 240).trim();


                    Endereco e = new Endereco();
                    e.setLogradouro(logradouro);
                    e.setNumero(numero);
                    e.setComplemento(complemento);
                    e.setCidade(cidade);
                    e.setBairro(bairro);


                    Aluno a = new Aluno();
                    a.setNome(nomeAluno);
                    a.setEmail(email);
                    a.setSexo(genero);
                    a.setTelefone(telefone);
                    a.setCpf(cpf);
                    a.setEndereco(e);
                    a.setCategoria("Aluno");
                    a.setDataCriacao(LocalDateTime.now());

                    usuarioRepository.save(a);

                    contaRegDadosLidos++;


                } else if (tipoRegistro.equals("03")) {

                    nomeProfessor = registro.substring(5, 45).trim();
                    email = registro.substring(45, 95).trim();
                    genero = registro.substring(95, 96).trim();
                    telefone = registro.substring(96,111).trim();
                    cpf = registro.substring(111, 125).trim();
                    logradouro = registro.substring(125, 155).trim();
                    numero = registro.substring(155, 160).trim();
                    complemento = registro.substring(160, 190).trim();
                    cidade = registro.substring(190, 210).trim();
                    bairro = registro.substring(210, 240).trim();


                    Endereco e = new Endereco();
                    e.setLogradouro(logradouro);
                    e.setNumero(numero);
                    e.setComplemento(complemento);
                    e.setCidade(cidade);
                    e.setBairro(bairro);


                    Professor p = new Professor();
                    p.setNome(nomeProfessor);
                    p.setEmail(email);
                    p.setSexo(genero);
                    p.setTelefone(telefone);
                    p.setCpf(cpf);
                    p.setEndereco(e);
                    p.setCategoria("Professor");
                    p.setDataCriacao(LocalDateTime.now());


                    usuarioRepository.save(p);

                    contaRegDadosLidos++;

                }
                else {
                    System.out.println("tipo de registro invalido");
                }

                registro = entrada.readLine();
            }
            entrada.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return  true;
    }
}


