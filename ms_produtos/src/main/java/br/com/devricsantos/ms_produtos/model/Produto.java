package br.com.devricsantos.ms_produtos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "valor_unitario", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "ativo")
    private boolean ativo;

    @PrePersist
    public void prePersist() {
        setAtivo(true);
    }
}
