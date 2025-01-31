package group.vvv.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ticket;
    
    @Column(nullable = false)
    private String tipoPassagem;
    
    @Column(nullable = false)
    private String localizador;
    
    @Column(nullable = false)
    private LocalDateTime horaPartida;
    
    @Column(nullable = false)
    private LocalDateTime horaChegada;
    
    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_passageiro")
    private Passageiro passageiro;
}
