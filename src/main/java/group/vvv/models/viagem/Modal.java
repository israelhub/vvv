package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Modal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modal")
    private Long id_modal;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "capacidade", nullable = false)
    private int capacidade;

    @Column(name = "ano_fabricacao")
    private int ano_fabricacao;
    
    @ManyToOne
    @JoinColumn(name = "id_transportadora", nullable = false)
    private Transportadora transportadora;
}