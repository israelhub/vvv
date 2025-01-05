package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Aeroporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_aeroporto;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private int codigo;  
}