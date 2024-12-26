package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
public class ViagemLocal {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    public enum Tipo {
        ORIGEM, DESTINO, ESCALA
    }

    @Data
    @Embeddable
    public static class ViagemLocalId implements Serializable {
        private Long id_viagem;
        private Long id_local;

        // Construtor padrão
        public ViagemLocalId() {}

        // Construtor com parâmetros
        public ViagemLocalId(Long id_viagem, Long id_local) {
            this.id_viagem = id_viagem;
            this.id_local = id_local;
        }

        // hashCode e equals
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