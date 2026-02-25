package br.com.devricsantos.ms_pedidos.controller.dto;

public record RecebimentoCallbackPagamentoDTO(
        Long codigo,
        String chavePagamento,
        boolean status,
        String observacoes) {
}
