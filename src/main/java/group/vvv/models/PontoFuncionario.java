package group.vvv.models;

import java.io.Serializable;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PontoFuncionario {

    @EmbeddedId
    private PontoFuncionarioId id;

    @ManyToOne
    @MapsId("id_funcionario") 
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

    @ManyToOne(optional = true)  // Torna o relacionamento opcional
    @MapsId("id_ponto_de_venda")
    @JoinColumn(name = "id_ponto_de_venda", nullable = true)  // Permite valores nulos
    private PontoDeVenda pontoDeVenda;

    @Column(name = "horario_inicial")  // Remove nullable = false
    private LocalTime horarioInicial;

    @Column(name = "horario_final")    // Remove nullable = false
    private LocalTime horarioFinal;

    public enum DiaSemana {
        SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO, DOMINGO
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        if (this.id == null) {
            this.id = new PontoFuncionarioId();
        }
        this.id.setDiaSemana(diaSemana);
    }

    public DiaSemana getDiaSemana() {
        return this.id != null ? this.id.getDiaSemana() : null;
    }

    @Embeddable
    @Data
    public static class PontoFuncionarioId implements Serializable {
        private Long id_funcionario;
        private Long id_ponto_de_venda;
        
        @Enumerated(EnumType.STRING)
        @Column(name = "dia_semana")
        private DiaSemana diaSemana;

        public PontoFuncionarioId() {}
        
        public PontoFuncionarioId(Long id_funcionario, Long id_ponto_de_venda, DiaSemana diaSemana) {
            this.id_funcionario = id_funcionario;
            this.id_ponto_de_venda = id_ponto_de_venda;
            this.diaSemana = diaSemana;
        }
    }
}