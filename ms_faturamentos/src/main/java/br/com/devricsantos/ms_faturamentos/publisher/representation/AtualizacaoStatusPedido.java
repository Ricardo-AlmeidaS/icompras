package br.com.devricsantos.ms_faturamentos.publisher.representation;

public record AtualizacaoStatusPedido(
        Long codigo, StatusPedido status, String urlNotaFiscal)
{
}
