package group.vvv.models.viagem;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_local;

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
}