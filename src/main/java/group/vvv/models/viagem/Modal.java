package group.vvv.models.viagem;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Modal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id_modal;

    @Column(nullable = false, length = 30)
    @NotNull(message = "O tipo não pode ser nulo")
    private String tipo;

    @Column(nullable = false, length = 50)
    @NotNull(message = "O modelo não pode ser nulo") 
    private String modelo;

    @Column(nullable = false)
    @NotNull(message = "A capacidade não pode ser nula")
    @Min(value = 1, message = "A capacidade deve ser maior que zero")
    private Integer capacidade;

    @Column(nullable = false)
    @NotNull(message = "O ano de fabricação não pode ser nulo")
    @Max(value = 2100, message = "O ano deve ser menor que 2100") 
    private Integer ano_fabricacao;
    
    @ManyToOne
    @JoinColumn(name = "id_transportadora", nullable = false)
    private Transportadora transportadora;
}