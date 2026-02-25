package br.com.devricsantos.ms_pedidos.client.representation;

import java.math.BigDecimal;

public record ProdutoRepresentation(
        Long codigo,
        String nome,
        BigDecimal valorUnitarion,
        boolean ativo
) {
}
