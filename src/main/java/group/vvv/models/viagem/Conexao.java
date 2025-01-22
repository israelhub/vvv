package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Data
@Entity
public class Conexao {

    @EmbeddedId
    private ViagemLocalId id;

    @ManyToOne
    @MapsId("id_viagem")
    @JoinColumn(name = "id_viagem")
    private Viagem viagem;

    @ManyToOne
    @MapsId("id_local")
    @JoinColumn(name = "id_local")
    private Local local;

    @ManyToOne
    @JoinColumn(name = "id_modal")
    private Modal modal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;
    
    @Column(name = "horario_partida")
    private LocalTime horarioPartida;

    @Column(name = "horario_chegada")
    private LocalTime horarioChegada;

    @Column(name = "data_partida")
    private Date dataPartida;

    @Column(name = "data_chegada")
    private Date dataChegada;

    public enum Tipo {
        ORIGEM, DESTINO, ESCALA
    }

    @Data
    @Embeddable
    public static class ViagemLocalId implements Serializable {
        private Long id_viagem;
        private Long id_local;

        public ViagemLocalId() {}

        public ViagemLocalId(Long id_viagem, Long id_local) {
            this.id_viagem = id_viagem;
            this.id_local = id_local;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(id_viagem, id_local);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ViagemLocalId that = (ViagemLocalId) obj;
            return Objects.equals(id_viagem, that.id_viagem) &&
                   Objects.equals(id_local, that.id_local);
        }
    }
}