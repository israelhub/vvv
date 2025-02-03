package group.vvv.models.viagem;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Escala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_escala;

    @NotNull(message = "A viagem é obrigatória")
    @ManyToOne
    @JoinColumn(name = "id_viagem")
    private Viagem viagem;

    @NotNull(message = "A origem é obrigatória")
    @ManyToOne
    @JoinColumn(name = "id_origem")
    private Local origem;

    @NotNull(message = "O destino é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_destino")
    private Local destino;

    @NotNull(message = "O modal é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_modal")
    private Modal modal;

    @NotNull(message = "O horário de partida é obrigatório")
    @Future(message = "O horário de partida deve ser no futuro")
    @Column(nullable = false)
    private LocalDateTime horarioPartida;

    @NotNull(message = "O horário de chegada é obrigatório")
    @Future(message = "O horário de chegada deve ser no futuro")
    @Column(nullable = false)
    private LocalDateTime horarioChegada;

    @Column(nullable = false)
    private int ordem;

}