package group.vvv.models;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ReservaPassageiro {

    @EmbeddedId
    private ReservaPassageiroId id;

    @ManyToOne
    @MapsId("id_reserva")
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @ManyToOne
    @MapsId("id_passageiro")
    @JoinColumn(name = "id_passageiro")
    private Passageiro passageiro;

    @Data
    @Embeddable
    public static class ReservaPassageiroId implements Serializable {
        private Long id_reserva;
        private Long id_passageiro;

        public ReservaPassageiroId() {}

        public ReservaPassageiroId(Long id_reserva, Long id_passageiro) {
            this.id_reserva = id_reserva;
            this.id_passageiro = id_passageiro;
        }
    }
}