package br.com.devricsantos.ms_pedidos.subscriber.representation;

import br.com.devricsantos.ms_pedidos.model.enums.StatusPedido;

public record AtualizacaoStatusPedidoRepresentation(
        Long codigo, StatusPedido status, String urlNotaFiscal, String codigoRastreio) {
}
