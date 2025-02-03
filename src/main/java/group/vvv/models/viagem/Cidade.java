package group.vvv.models.viagem;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cidade;

    @NotNull(message = "O nome da cidade não pode ser nulo")
    @Size(min = 2, max = 50, message = "O nome da cidade deve ter entre 2 e 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nome;

    @NotNull(message = "O código da cidade não pode ser nulo")
    @Pattern(regexp = "[A-Z]{3}", message = "O código deve conter exatamente 3 letras maiúsculas")
    @Column(nullable = false, length = 3)
    private String codigo;  
}