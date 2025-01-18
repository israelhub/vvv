package group.vvv.models;

import java.time.LocalTime;
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
    private LocalTime horaPartida;
    
    @Column(nullable = false)
    private LocalTime horaChegada;
    
    @ManyToOne
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_passageiro")
    private Passageiro passageiro;
}

