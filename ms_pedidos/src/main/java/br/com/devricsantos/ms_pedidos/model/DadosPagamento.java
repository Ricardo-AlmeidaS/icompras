package br.com.devricsantos.ms_pedidos.model;

import br.com.devricsantos.ms_pedidos.model.enums.TipoPagamento;
import lombok.Data;

@Data
public class DadosPagamento {
    private String dados;
    private TipoPagamento tipoPagamento;
}
