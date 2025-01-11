package group.vvv.models;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

@Data
@Entity
public class Passageiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_passageiro;

    @Column(nullable = false, length = 60)
    private String nome;

    @Column(nullable = false)
    private Date data_nascimento;

    @Column(nullable = false, length = 14)
    private String cpf;

    @Column(length = 15)
    private String telefone;

    @Column(length = 30)
    private String profissao;

    @ManyToOne
    @JoinColumn(name = "id_responsavel", nullable = true)
    private Passageiro responsavel;

    public int getIdade() {
        return Period.between(this.data_nascimento.toLocalDate(), LocalDate.now()).getYears();
    }
}