package group.vvv.models;

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

    @Column(name = "nome_empresa", nullable = false, length = 50)
    private String nome_empresa;

    @Column(name = "esta_em_manuntencao", nullable = false)
    private boolean esta_em_manuntencao = false;

}