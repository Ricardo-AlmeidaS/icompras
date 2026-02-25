package br.com.devricsantos.ms_pedidos.controller.dto;

import br.com.devricsantos.ms_pedidos.model.enums.TipoPagamento;

public record AdicaoNovoPagamentoDTO(
        Long codigoPedido,
        String dados,
        TipoPagamento tipoPagamento) {
}
