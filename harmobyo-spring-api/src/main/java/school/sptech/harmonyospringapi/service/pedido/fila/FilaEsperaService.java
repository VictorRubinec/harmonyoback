package school.sptech.harmonyospringapi.service.pedido.fila;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.fila.FilaEspera;
import school.sptech.harmonyospringapi.domain.fila.ListaFilas;
import school.sptech.harmonyospringapi.service.pedido.PedidoService;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;
import school.sptech.harmonyospringapi.service.pedido.hashing.HashTableService;
import school.sptech.harmonyospringapi.utils.NodeObj;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FilaEsperaService {

    @Lazy
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private HashTableService hashTableService;

    private ListaFilas listaFilas;

    public FilaEsperaService() {
        this.listaFilas = new ListaFilas();
    }

    //pesquisa o pedido pai e já cria uma fila pronta para inserir o pedido
    public FilaEspera criarFila(PedidoExibicaoDto pai){
        List<FilaEspera> filas = this.listaFilas.getFilas();
        for(FilaEspera fila : filas){
            if (fila.getPai().getId() == pai.getId()){
                return fila;
            }
        }

        FilaEspera novaFila = new FilaEspera(pai);
        this.listaFilas.novaFila(novaFila);
        return novaFila;
    }

    public PedidoExibicaoDto adicionarPedidoFilaEspera(int fkUsuario, String dataAula){

        // formatando data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dataAulaFormatada = LocalDateTime.parse(dataAula, formatter);

        // buscando todos os pedidos com essa data e usuario
        List<PedidoExibicaoDto> pedidoExibicaoDtos = this.pedidoService.buscarAulasPorIdUsuarioEDataAula(fkUsuario, dataAulaFormatada);

        // buscando o ultimo pedido inserido
        PedidoExibicaoDto pedidoExibicaoDto = this.pedidoService.buscarPorIdParaExibicao(
                pedidoExibicaoDtos.get(0).getId());

        // buscando o pai da fila de espera
        PedidoExibicaoDto pai = this.listaFilas.buscarPai(pedidoExibicaoDto.getId());

        // se o a fila não existir, cria uma nova fila e insere o pedido
        if (pai == null){
            // cria a fila com o pedido pai
            FilaEspera filaEspera = criarFila(this.pedidoService.buscarPorIdParaExibicao(
                    pedidoExibicaoDtos.get(pedidoExibicaoDtos.size()-1).getId()));
            // atualiza o status do pedido inserido
            this.pedidoService.atualizarStatus(
                    this.pedidoService.buscarPorId(pedidoExibicaoDto.getId()), "Em Fila");
            this.hashTableService.atualizarStatusPedidoPorId(
                    pedidoExibicaoDto.getId(), this.pedidoService.buscarPorId(pedidoExibicaoDto.getId()),"Em Fila");
            // insere o pedido na fila
            filaEspera.insert(pedidoExibicaoDto);
            // retorna o pedido inserido
            return filaEspera.peek();
        }
        // caso exista a fila, só insere o pedido na fila
        else {
            // busca a fila
            FilaEspera filaEspera = this.listaFilas.buscaFila(pai.getId());
            // atualiza o status do pedido inserido
            this.pedidoService.atualizarStatus(
                    this.pedidoService.buscarPorId(pedidoExibicaoDto.getId()), "Em Fila");
            this.hashTableService.atualizarStatusPedidoPorId(
                    pedidoExibicaoDto.getId(), this.pedidoService.buscarPorId(pedidoExibicaoDto.getId()),"Em Fila");
            // insere o pedido na fila
            filaEspera.insert(pedidoExibicaoDto);
            // retorna o pedido inserido
            return filaEspera.peek();
        }
    }

    public PedidoExibicaoDto removerPrimeiroPedidoFilaEspera(int id){
        PedidoExibicaoDto pai = this.listaFilas.buscarPai(id);
        if (pai == null){
            return null;
        } else {
            FilaEspera filaEspera = this.listaFilas.buscaFila(pai.getId());

            this.pedidoService.atualizarStatus(
                    this.pedidoService.buscarPorId(id), "Pendente");
            this.hashTableService.atualizarStatusPedidoPorId(
                    id, this.pedidoService.buscarPorId(id),"Pendente");

            return filaEspera.poll();
        }
    }

    public int pegarPosicaoNaFila(int id){
        PedidoExibicaoDto pedidoExibicaoDto = this.pedidoService.buscarPorIdParaExibicao(id);
        PedidoExibicaoDto pai = this.listaFilas.buscarPai(pedidoExibicaoDto.getId());
        List<FilaEspera> filas = this.listaFilas.getFilas();
        if (pai == null){
            return 0;
        } else {
            FilaEspera filaEspera = this.listaFilas.buscaFila(pai.getId());

            return filaEspera.posicaoNaFila(pedidoExibicaoDto);
        }
    }

    public PedidoExibicaoDto buscarPai(int idPedido){
        return this.listaFilas.buscarPai(idPedido);
    }

    public List<PedidoExibicaoDto> listarFilaDeEspera(int idPai){
        PedidoExibicaoDto pai = this.listaFilas.buscarPai(idPai);
        if (pai == null){
            return null;
        } else {
            FilaEspera filaEspera = this.listaFilas.buscaFila(pai.getId());
            return filaEspera.getPedidos();
        }
    }

    public List<FilaEspera> listarFilas(){
        return this.listaFilas.getFilas();
    }

    public boolean isEmpty(){
        return this.listaFilas.isEmpty();
    }

    public void adicionarBanco(){
        List<PedidoExibicaoDto> pedidoExibicaoDtos = this.pedidoService.obterTodos();
        List<PedidoExibicaoDto> listaPedidos = this.pedidoService.inverterLista(pedidoExibicaoDtos);

        for (PedidoExibicaoDto pedidoExibicaoDto : listaPedidos){
            if (pedidoExibicaoDto.getStatus().getDescricao().equals("Em Fila")){
                PedidoExibicaoDto pai = this.listaFilas.buscarPai(pedidoExibicaoDto.getId());
                if (pai == null){
                    List<PedidoExibicaoDto> pedidosDatasIguais = this.pedidoService.buscarAulasPorIdUsuarioEDataAula(
                            pedidoExibicaoDto.getAluno().getId(), pedidoExibicaoDto.getDataAula());
                    pai = pedidosDatasIguais.get(pedidosDatasIguais.size()-1);
                    FilaEspera filaEspera = criarFila(pai);
                    filaEspera.insert(pedidoExibicaoDto);
                } else {
                    FilaEspera filaEspera = this.listaFilas.buscaFila(pai.getId());
                    filaEspera.insert(pedidoExibicaoDto);
                }
            }
        }
    }

    public ListaFilas getListaFilas() {
        return listaFilas;
    }

    public void setListaFilas(ListaFilas listaFilas) {
        this.listaFilas = listaFilas;
    }
}
