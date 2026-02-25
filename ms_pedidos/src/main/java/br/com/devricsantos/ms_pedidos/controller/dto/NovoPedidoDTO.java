package br.com.devricsantos.ms_pedidos.controller.dto;

import java.util.List;

public record NovoPedidoDTO(
        Long codigoCliente,
        DadosPagamentoDTO dadosPagamento,
        List<ItemPedidoDTO> itens) {
}
