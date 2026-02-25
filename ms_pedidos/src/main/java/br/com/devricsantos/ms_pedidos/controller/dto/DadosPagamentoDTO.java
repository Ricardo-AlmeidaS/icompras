package br.com.devricsantos.ms_pedidos.controller.dto;

import br.com.devricsantos.ms_pedidos.model.enums.TipoPagamento;

public record DadosPagamentoDTO(String dados, TipoPagamento tipoPagamento) {
}
