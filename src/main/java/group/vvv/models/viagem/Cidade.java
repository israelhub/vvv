package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cidade;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 3)
    private String codigo;  // Código de 3 caracteres, ex: "RIO"
}