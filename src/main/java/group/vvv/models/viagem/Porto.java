package group.vvv.models.viagem;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Porto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_porto;

    @NotNull(message = "O nome do porto não pode ser nulo")
    @Size(min = 2, max = 60, message = "O nome do porto deve ter entre 2 e 60 caracteres")
    @Column(nullable = false, length = 60)
    private String nome;  
}
