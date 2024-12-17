package group.vvv.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import jakarta.persistence.*;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;

    @Column(nullable = false, length = 60)
    private String nome;

    private LocalDate data_nascimento;

    @Column(nullable = false, length = 320)
    @Email(message = "O email deve ser válido")
    private String email;

    @Column(length = 255)
    private String senha;

    @Column(length = 14)
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos")
    private String cpf;

    @Column(length = 30)
    private String profissao;

    @Column(length = 9)
    private String cep;

    @Column(length = 255)
    private String rua;

    @Column(length = 10)
    private String numero_rua;

    @Column(length = 2)
    private String uf;

    @Column(length = 15)
    private String telefone;
}