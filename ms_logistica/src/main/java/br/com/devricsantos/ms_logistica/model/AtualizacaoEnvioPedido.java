package br.com.devricsantos.ms_logistica.model;

public record AtualizacaoEnvioPedido(
        Long codigo, StatusPedido status, String codigoRastreio) {
}
