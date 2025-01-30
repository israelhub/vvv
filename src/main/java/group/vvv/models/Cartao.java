package group.vvv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cartao;

    @Column(nullable = false, length = 255)
    private String numeroEncriptado;
    
    @Column(nullable = false, length = 255)
    private String cvvEncriptado;
    
    @Column(nullable = false, length = 7)
    private String validade;
    
    @Column(nullable = false, length = 60)
    private String nomeTitular;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCartao tipo;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = true)
    private Cliente cliente;
    
    public enum TipoCartao {
        CREDITO, DEBITO
    }
}
