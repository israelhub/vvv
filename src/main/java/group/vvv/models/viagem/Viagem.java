package group.vvv.models.viagem;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Viagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_viagem;

    @Column(nullable = false)
    private int numReservasAssociadas;

    @Column(nullable = false)
    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "id_origem")
    @NotNull(message = "A origem é obrigatória")
    private Local origem;

    @ManyToOne
    @JoinColumn(name = "id_destino")
    @NotNull(message = "O destino é obrigatório")
    private Local destino;

    @ManyToOne
    @JoinColumn(name = "id_modal")
    @NotNull(message = "O modal é obrigatório")
    private Modal modal;

    @Column(name = "horario_partida", nullable = false)
    @NotNull(message = "O horário de partida é obrigatório")
    @Future(message = "O horário de partida deve ser no futuro")
    private LocalDateTime horarioPartida;

    @Column(name = "horario_chegada", nullable = false)
    @NotNull(message = "O horário de chegada é obrigatório")
    @Future(message = "O horário de chegada deve ser no futuro")
    private LocalDateTime horarioChegada;

    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL)
    @OrderBy("ordem ASC")
    private List<Escala> escalas;
}