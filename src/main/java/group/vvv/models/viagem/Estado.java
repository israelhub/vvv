package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id_estado;

    @Column(nullable = false)
    private String sigla;

    @Column(nullable = false)
    private String nome;
}
