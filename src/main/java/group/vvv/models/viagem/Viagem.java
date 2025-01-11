package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;
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

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "horario_partida", nullable = false)
    private LocalTime horarioPartida;

    @Column(name = "horario_chegada", nullable = false)
    private LocalTime horarioChegada;

    @Column(name = "data_partida", nullable = false)
    private Date dataPartida;

    @Column(name = "data_chegada", nullable = false)
    private Date dataChegada;

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