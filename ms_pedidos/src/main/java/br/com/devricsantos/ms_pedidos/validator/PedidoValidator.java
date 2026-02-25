package br.com.devricsantos.ms_pedidos.validator;

import br.com.devricsantos.ms_pedidos.client.ClientesClient;
import br.com.devricsantos.ms_pedidos.client.ProdutosClient;
import br.com.devricsantos.ms_pedidos.client.representation.ClienteRepresentation;
import br.com.devricsantos.ms_pedidos.client.representation.ProdutoRepresentation;
import br.com.devricsantos.ms_pedidos.model.ItemPedido;
import br.com.devricsantos.ms_pedidos.model.Pedido;
import br.com.devricsantos.ms_pedidos.model.exception.ValidationException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoValidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

//    public void validar(Pedido pedido){
//        List<Long> codigoProdutos =
//                pedido.getItens()
//                        .stream().map(i -> i.getCodigoProduto()).toList();
//        codigoProdutos.forEach(codigoProduto -> {
//            ResponseEntity<ProdutoRepresentation> response = produtosClient.obterDados(codigoProduto);
//            ProdutoRepresentation produto = response.getBody();
//        });
//    }

    public void validar(Pedido pedido) {
        Long codigoCliente = pedido.getCodigoCliente();
        validarCliente(codigoCliente);
        pedido.getItens().forEach(this::validarItem);
    }

    public void validarCliente(Long codigoCliente) {
        try {
            var response = clientesClient.obterDados(codigoCliente);
            ClienteRepresentation cliente = response.getBody();
            log.info("Cliente de código {} encontrado: {}", cliente.codigo(), cliente.nome());

            if(!cliente.ativo()){
                throw new ValidationException("codigoCliente", "Cliente Inativo.");
            }

        } catch (FeignException.NotFound e) {
            var message = String.format("Cliente de código %d não encontrado.", codigoCliente);
            throw new ValidationException("codigoCliente", message);
        }
    }

    public void validarItem(ItemPedido itemPedido) {
        try {
            var response = produtosClient.obterDados(itemPedido.getCodigoProduto());
            ProdutoRepresentation produto = response.getBody();
            log.info("Produto de código {} encontrado: {}", produto.codigo(), produto.nome());

            if(!produto.ativo()){
                throw new ValidationException("codigoProduto", "Produto inativo.");
            }

        } catch (FeignException.NotFound e){
            var message = String.format("Produto de código %d não encontrado.", itemPedido.getCodigoProduto());
            throw new ValidationException("codigoProduto", message);
        }
    }

}
