package group.vvv.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TicketDTO {
    private String localizador;
    private String tipoPassagem;
    private LocalDateTime horaPartida;
    private LocalDateTime horaChegada;
    private String origem;
    private String destino;
    private String modalPrincipal;
    private String companhia;
    private Double valor;
}