package group.vvv.models.viagem;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import java.time.LocalDate;


@Entity
@Data
@Table(name = "manutencao_modal")
public class ManutencaoModal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_manutencao")
    private Long idManutencao;

    @ManyToOne
    @JoinColumn(name = "id_modal", nullable = false)
    private Modal modal;

    @NotNull(message = "A data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Future(message = "A data fim deve ser no futuro")
    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "descricao", nullable = false)
    private String descricao;
}