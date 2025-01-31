package group.vvv.models.viagem;

import jakarta.persistence.*;
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
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "id_origem")
    private Local origem;

    @ManyToOne
    @JoinColumn(name = "id_destino")
    private Local destino;

    @ManyToOne
    @JoinColumn(name = "id_modal")
    private Modal modal;

    @Column(name = "horario_partida", nullable = false)
    private LocalDateTime horarioPartida;

    @Column(name = "horario_chegada", nullable = false)
    private LocalDateTime horarioChegada;

    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL)
    @OrderBy("ordem ASC")
    private List<Escala> escalas;
}