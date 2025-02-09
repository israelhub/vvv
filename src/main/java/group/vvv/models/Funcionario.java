package group.vvv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_funcionario;

    @Column(nullable = false, length = 60)
    private String nome;

    @Column(nullable = false, length = 320, unique = true)
    private String email;

    @Column(nullable = false, length = 64)
    private String senha;

    @Column(length = 9)
    private String cep;

    @Column(length = 255)
    private String rua;

    @Column(length = 10)
    private String numero_rua;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cargo cargo;

    @Column(name = "login_inicial_realizado", nullable = false)
    private boolean loginInicialRealizado = false;

    public enum Cargo {
        GERENTE, GERENTE_DE_NEGOCIOS_VIRTUAIS, PADRAO
    }
}
