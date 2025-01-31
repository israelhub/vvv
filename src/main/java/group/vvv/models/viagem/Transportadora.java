package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Transportadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transportadora")
    private Long idTransportadora;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "cnpj", nullable = false, length = 14, unique = true)
    private String cnpj;
}