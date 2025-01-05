package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Estacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_estacao;

    @Column(nullable = false)
    private String nome; 
}