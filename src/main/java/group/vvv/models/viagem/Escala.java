package group.vvv.models.viagem;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Escala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_escala;

    @ManyToOne
    @JoinColumn(name = "id_viagem")
    private Viagem viagem;

    @ManyToOne
    @JoinColumn(name = "id_origem")
    private Local origem;

    @ManyToOne
    @JoinColumn(name = "id_destino")
    private Local destino;

    @ManyToOne
    @JoinColumn(name = "id_modal")
    private Modal modal;

    @Column(nullable = false)
    private LocalDateTime horarioPartida;

    @Column(nullable = false)
    private LocalDateTime horarioChegada;

    @Column(nullable = false)
    private int ordem;

}