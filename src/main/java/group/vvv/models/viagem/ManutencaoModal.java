package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

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

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "descricao", nullable = false)
    private String descricao;
}