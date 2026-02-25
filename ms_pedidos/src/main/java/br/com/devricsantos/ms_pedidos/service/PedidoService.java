package br.com.devricsantos.ms_pedidos.service;

import br.com.devricsantos.ms_pedidos.client.ClientesClient;
import br.com.devricsantos.ms_pedidos.client.ProdutosClient;
import br.com.devricsantos.ms_pedidos.client.ServicoBancarioClient;
import br.com.devricsantos.ms_pedidos.model.DadosPagamento;
import br.com.devricsantos.ms_pedidos.model.ItemPedido;
import br.com.devricsantos.ms_pedidos.model.Pedido;
import br.com.devricsantos.ms_pedidos.model.enums.StatusPedido;
import br.com.devricsantos.ms_pedidos.model.enums.TipoPagamento;
import br.com.devricsantos.ms_pedidos.model.exception.ItemNaoEncontradoException;
import br.com.devricsantos.ms_pedidos.publisher.PagamentoPublisher;
import br.com.devricsantos.ms_pedidos.repository.ItemPedidoRepository;
import br.com.devricsantos.ms_pedidos.repository.PedidoRepository;
import br.com.devricsantos.ms_pedidos.validator.PedidoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator pedidoValidator;
    private final ServicoBancarioClient servicoBancarioClient;
    private final ClientesClient clientesClient;
    private final ProdutosClient produtosClient;
    private final PagamentoPublisher pagamentoPublisher;

    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        pedidoValidator.validar(pedido);
        realizarPersistencia(pedido);
        enviarSolitacaoPagamento(pedido);
        return pedido;
    }

    private void enviarSolitacaoPagamento(Pedido pedido) {
        var chavePagamento = servicoBancarioClient.solictarPagamento(pedido);
        pedido.setChavePagamento(chavePagamento);
    }

    private void realizarPersistencia(Pedido pedido) {
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(pedido.getItens());
    }

    public void atualizarStatusPagamento(
            Long codigoPedido, String chavePagamento, boolean sucesso, String observacoes) {

        var pedidoEncontrado =
                pedidoRepository.findByCodigoAndChavePagamento(codigoPedido, chavePagamento);

        if (pedidoEncontrado.isEmpty()) {
            var msg = String.format("Pedido não encontrado para o código %d e chave pgmto %s",
                    codigoPedido, chavePagamento);
            log.error(msg);
            return;
        }

        Pedido pedido = pedidoEncontrado.get();

        if (sucesso) {
            prepararEPublicarPedidoPago(observacoes, pedido);
        } else {
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
        }

        pedidoRepository.save(pedido);
    }

    private void prepararEPublicarPedidoPago(String observacoes, Pedido pedido) {
        pedido.setStatus(StatusPedido.PAGO);
        pedido.setObservacoes(observacoes);
        carregarDadosCliente(pedido);
        carregarItensPedidos(pedido);
        pagamentoPublisher.publicar(pedido);
    }

    @Transactional
    public void adicionarNovoPagamento(
            Long codigoPedido, String dadosCartao, TipoPagamento  tipoPagamento) {
        var pedidoEncontrado = pedidoRepository.findById(codigoPedido);

        if (pedidoEncontrado.isEmpty()) {
            var message = String.format("Pedido não encontrado para o código %d informado.", codigoPedido);
            throw new ItemNaoEncontradoException(message);
        }

        var pedido = pedidoEncontrado.get();

        DadosPagamento dadosPagamento = new DadosPagamento();
        dadosPagamento.setTipoPagamento(tipoPagamento);
        dadosPagamento.setDados(dadosCartao);

        pedido.setDadosPagamento(dadosPagamento);
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setObservacoes("Novo pagamento realizado, aguardando processamento.");

        pedidoRepository.save(pedido);

        String novaChavePagamento = servicoBancarioClient.solictarPagamento(pedido);
        pedido.setChavePagamento(novaChavePagamento);
    }

    public Optional<Pedido> carregarDadosCompletosPedido(Long codigo) {
        Optional<Pedido> pedido = pedidoRepository.findById(codigo);
        pedido.ifPresent(this::carregarDadosCliente);
        pedido.ifPresent(this::carregarItensPedidos);
        return pedido;
    }

    private void carregarDadosCliente(Pedido pedido) {
        Long codigoCliente = pedido.getCodigoCliente();
        var resposnse = clientesClient.obterDados(codigoCliente);
        pedido.setDadosCliente(resposnse.getBody());
    }

    private void carregarItensPedidos(Pedido pedido) {
        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);
        pedido.setItens(itens);
        pedido.getItens().forEach(this::carregarDadosProduto);
    }

    private void carregarDadosProduto(ItemPedido itemPedido) {
        Long coodigoProduto = itemPedido.getCodigoProduto();
        var response = produtosClient.obterDados(coodigoProduto);
        itemPedido.setNome(response.getBody().nome());
    }
}
