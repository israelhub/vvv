package group.vvv.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pagamento;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @ManyToOne 
    @JoinColumn(name = "id_cartao", nullable = false)
    private Cartao cartao;

    @Column(nullable = false)
    private Integer numParcelas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento statusPagamento;

    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL)
    private List<Parcela> parcelas;

    public enum StatusPagamento {
        PENDENTE, AUTORIZADO, NEGADO
    }
}