package group.vvv.models.viagem;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Aeroporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_aeroporto;

    @NotNull(message = "O nome do aeroporto não pode ser nulo")
    @Size(min = 2, max = 50, message = "O nome do aeroporto deve ter entre 2 e 50 caracteres")
    private String nome;

    @NotNull(message = "O código do aeroporto não pode ser nulo")
    @Column(nullable = false, unique = true)
    private int codigo;  
}