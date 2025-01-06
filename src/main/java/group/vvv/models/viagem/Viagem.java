package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_viagem;

    @Column(nullable = false)
    private int numReservasAssociadas;

    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL)
    private List<ViagemLocal> locais;

    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL) 
    private List<ViagemModal> modais;

    public List<ViagemLocal> getEscalas() {
        if (locais != null) {
            return locais.stream()
                .filter(vl -> vl.getTipo() == ViagemLocal.Tipo.ESCALA)
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public ViagemLocal getOrigemLocal() {
        if (locais != null) {
            return locais.stream()
                .filter(vl -> vl.getTipo() == ViagemLocal.Tipo.ORIGEM)
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    public ViagemLocal getDestinoLocal() {
        if (locais != null) {
            return locais.stream()
                .filter(vl -> vl.getTipo() == ViagemLocal.Tipo.DESTINO)
                .findFirst()
                .orElse(null);
        }
        return null;
    }
}