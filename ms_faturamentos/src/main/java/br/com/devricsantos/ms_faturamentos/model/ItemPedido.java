package br.com.devricsantos.ms_faturamentos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    private Long codigo;
    private String nome;
    private BigDecimal valorUnitario;
    private Integer quantidade;
    private BigDecimal total;
}
