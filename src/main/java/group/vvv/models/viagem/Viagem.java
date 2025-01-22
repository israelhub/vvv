package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
public class Viagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_viagem;

    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Conexao> roteiros;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private int numReservasAssociadas;

    public Conexao getOrigemLocal() {
        if (roteiros == null || roteiros.isEmpty()) {
            return null;
        }
        return roteiros.stream()
                .filter(r -> r != null && Conexao.Tipo.ORIGEM.equals(r.getTipo()))
                .findFirst()
                .orElse(null);
    }

    public Conexao getDestinoLocal() {
        if (roteiros == null || roteiros.isEmpty()) {
            return null;
        }
        return roteiros.stream()
                .filter(r -> r != null && Conexao.Tipo.DESTINO.equals(r.getTipo()))
                .findFirst()
                .orElse(null);
    }

    public List<Conexao> getEscalas() {
        if (roteiros == null || roteiros.isEmpty()) {
            return Collections.emptyList();
        }
        return roteiros.stream()
                .filter(r -> r != null && Conexao.Tipo.ESCALA.equals(r.getTipo()))
                .collect(Collectors.toUnmodifiableList());
    }
}