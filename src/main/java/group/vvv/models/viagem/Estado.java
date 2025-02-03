package group.vvv.models.viagem;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id_estado;

    @NotBlank(message = "A sigla do estado é obrigatória")
    @Size(min = 2, max = 2, message = "A sigla deve ter exatamente 2 caracteres")
    @Column(nullable = false)
    private String sigla;

    @NotBlank(message = "O nome do estado é obrigatório")
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
    @Column(nullable = false)
    private String nome;
}