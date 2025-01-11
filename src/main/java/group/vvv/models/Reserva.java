package group.vvv.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Date;

import group.vvv.models.viagem.Viagem;

@Data
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_reserva;

    @Column(nullable = false)
    private Date data;

    @Column(nullable = false, length = 10)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 50)
    private String origem;

    @Column(nullable = false, length = 50)
    private String destino;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_funcionario", nullable = true)
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "id_viagem", nullable = false)
    private Viagem viagem;
}