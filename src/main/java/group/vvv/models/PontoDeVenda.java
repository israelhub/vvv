package group.vvv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PontoDeVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ponto_de_venda;

    @Column(nullable = false, length = 60)
    private String nome;

    @Column(nullable = false, length = 14, unique = true)   
    private String cnpj;

    @Column(length = 9)
    private String cep;

    @Column(length = 255)
    private String rua;

    @Column(length = 10)
    private String numero_rua;

    @Column(length = 15)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "id_gerente")
    private Funcionario gerente;
}