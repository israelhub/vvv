package group.vvv.models.viagem;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_local")
    private Long id_local;

    @NotNull(message = "A cidade é obrigatória")
    @ManyToOne
    @JoinColumn(name = "id_cidade", nullable = false)
    private Cidade id_cidade;

    @ManyToOne
    @JoinColumn(name = "id_aeroporto", nullable = true)
    private Aeroporto id_aeroporto;

    @ManyToOne
    @JoinColumn(name = "id_estacao", nullable = true)
    private Estacao id_estacao;

    @ManyToOne
    @JoinColumn(name = "id_porto", nullable = true)
    private Porto id_porto;

    public String getDescricaoCompleta() {
        String infraestrutura = "";
        if (id_aeroporto != null) {
            infraestrutura = id_aeroporto.getNome();
        } else if (id_estacao != null) {
            infraestrutura = id_estacao.getNome();
        } else if (id_porto != null) {
            infraestrutura = id_porto.getNome();
        }
        return id_cidade.getNome() + " - " + infraestrutura;
    }
}