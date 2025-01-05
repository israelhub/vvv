package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Porto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_porto;

    @Column(nullable = false)
    private String nome;  
}