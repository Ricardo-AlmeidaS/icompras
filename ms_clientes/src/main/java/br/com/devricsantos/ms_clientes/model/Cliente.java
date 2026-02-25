package br.com.devricsantos.ms_clientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "nome", length = 150)
    private String nome;

    @Column(name = "cpf", length = 11)
    private String cpf;

    @Column(name = "logradouro", length = 100)
    private String logradouro;

    @Column(name = "numero", length = 10)
    private String numero;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "ativo")
    private boolean ativo;

    @PrePersist
    public void prePersist() {
        setAtivo(true);
    }

}
