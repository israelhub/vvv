package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
public class ViagemModal {

    @EmbeddedId
    private ViagemModalId id;

    @ManyToOne
    @MapsId("id_viagem")
    @JoinColumn(name = "id_viagem")
    private Viagem viagem;

    @ManyToOne
    @MapsId("id_modal")
    @JoinColumn(name = "id_modal")
    private Modal modal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    public enum Tipo {
        ORIGEM, DESTINO, ESCALA
    }

    @Data
    @Embeddable
    public static class ViagemModalId implements Serializable {
        private Long id_viagem;
        private Long id_modal;

        public ViagemModalId() {}

        public ViagemModalId(Long id_viagem, Long id_modal) {
            this.id_viagem = id_viagem;
            this.id_modal = id_modal;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id_viagem, id_modal);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ViagemModalId that = (ViagemModalId) obj;
            return Objects.equals(id_viagem, that.id_viagem) &&
                   Objects.equals(id_modal, that.id_modal);
        }
    }
}