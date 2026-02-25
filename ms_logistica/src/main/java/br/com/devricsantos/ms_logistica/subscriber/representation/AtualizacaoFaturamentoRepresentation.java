package br.com.devricsantos.ms_logistica.subscriber.representation;

import br.com.devricsantos.ms_logistica.model.StatusPedido;

public record AtualizacaoFaturamentoRepresentation(
        Long codigo, StatusPedido status, String urlNotaFiscal
) {
}
