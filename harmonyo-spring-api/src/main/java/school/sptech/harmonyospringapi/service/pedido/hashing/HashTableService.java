package school.sptech.harmonyospringapi.service.pedido.hashing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import school.sptech.harmonyospringapi.domain.Pedido;
import school.sptech.harmonyospringapi.domain.hashing.HashTable;
import school.sptech.harmonyospringapi.domain.hashing.Node;
import school.sptech.harmonyospringapi.service.pedido.PedidoService;
import school.sptech.harmonyospringapi.service.pedido.dto.PedidoExibicaoDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class HashTableService {

    @Autowired
    @Lazy
    private PedidoService pedidoService;

    private HashTable hashTable = new HashTable(7);

    private int funcaoHash(String status){
        int posicao = 0;

        switch (status) {
            case "Em Fila":
                posicao = 0;
                break;
            case "Pendente":
                posicao = 1;
                break;
            case "Confirmado":
                posicao = 2;
                break;
            case "Cancelado":
                posicao = 3;
                break;
            case "Recusado":
                posicao = 4;
                break;
            case "Concluído":
                posicao = 5;
                break;
            case "Aguardando Pagamento":
                posicao = 6;
                break;
        }

        return posicao;
    }
    public void insere(PedidoExibicaoDto pedidoExibicaoDto){
        int lista = funcaoHash(pedidoExibicaoDto.getStatus().getDescricao());

        hashTable.getTab()[lista].insereNode(pedidoExibicaoDto);
    }

    public boolean busca(PedidoExibicaoDto pedidoExibicaoDto){
        int lista = funcaoHash(pedidoExibicaoDto.getStatus().getDescricao());

        Node resultado = hashTable.getTab()[lista].buscaNode(pedidoExibicaoDto);

        if (resultado != null) return true;

        return false;
    }

    public List<PedidoExibicaoDto> buscarPedidosPorIdEStatus(int idUsuario, String status){
        int lista = funcaoHash(status);
        List<PedidoExibicaoDto> pedidoExibicaoDto = new ArrayList<>();

        for (int i = 0; i < hashTable.getTab()[lista].getTamanho(); i++){
            PedidoExibicaoDto pedido = hashTable.getTab()[lista].getElementoRecursivo(i).getPedido();
            if (idUsuario == pedido.getProfessor().getId() || idUsuario == pedido.getAluno().getId()){
                pedidoExibicaoDto.add(pedido);
            }
        }

        return pedidoExibicaoDto;
    }

    public PedidoExibicaoDto atualizarStatusPedidoPorId(int id, Pedido pedido, String status){
        int lista = funcaoHash(pedido.getStatus().getDescricao());
        int listaNova = funcaoHash(status);
        for (int j = 0; j < hashTable.getTab()[lista].getTamanho(); j++){
            PedidoExibicaoDto pedidoExibicaoDto = hashTable.getTab()[lista].getElementoRecursivo(j).getPedido();
            if (id == pedidoExibicaoDto.getId()){
                pedidoExibicaoDto.getStatus().setDescricao(status);
                hashTable.getTab()[listaNova].insereNode(pedidoExibicaoDto);
                hashTable.getTab()[lista].removeNode(pedidoExibicaoDto);
                return pedidoExibicaoDto;
            }
        }
        return null;
    }

    public boolean remove(PedidoExibicaoDto pedidoExibicaoDto){
        int lista = funcaoHash(pedidoExibicaoDto.getStatus().getDescricao());

        Node resultado = hashTable.getTab()[lista].buscaNode(pedidoExibicaoDto);

        if (resultado != null) {
            hashTable.getTab()[lista].removeNode(pedidoExibicaoDto);
            return true;
        }

        return false;
    }

    public void adicionarBanco(){
        List<PedidoExibicaoDto> pedidoExibicaoDto = pedidoService.obterTodos();

        for (int i = 0; i < pedidoExibicaoDto.size(); i++){
            insere(pedidoExibicaoDto.get(i));
        }
    }

    public boolean isEmpty(){
        for (int i = 0; i < 7; i++){
            if (this.hashTable.getTab()[i].getTamanho() != 0){
                return false;
            }
        }
        return true;
    }
}
