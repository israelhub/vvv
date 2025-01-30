package group.vvv.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group.vvv.models.Cartao;
import group.vvv.models.Cliente;
import group.vvv.models.Funcionario;
import group.vvv.models.Cartao.TipoCartao;
import group.vvv.repositories.CartaoRepository;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class CartaoService {
    @Autowired
    private CartaoRepository cartaoRepository;
    
    private static final String ALGORITMO = "AES";
    private static final String CHAVE_ENCRIPTACAO = "Ch@veSecr3ta2024"; 
    
    public Cartao salvarCartao(String numero, String cvv, String validade, 
                              String nomeTitular, TipoCartao tipo, Cliente cliente) {
        Cartao cartao = new Cartao();
        cartao.setNumeroEncriptado(encriptar(numero));
        cartao.setCvvEncriptado(encriptar(cvv));
        cartao.setValidade(validade);
        cartao.setNomeTitular(nomeTitular);
        cartao.setTipo(tipo);
        cartao.setCliente(cliente);
        return cartaoRepository.save(cartao);
    }

        public Cartao salvarCartaoParaFuncionario(String numero, String cvv, String validade, 
                                              String nomeTitular, TipoCartao tipo) {
        Cartao cartao = new Cartao();
        cartao.setNumeroEncriptado(encriptar(numero));
        cartao.setCvvEncriptado(encriptar(cvv));
        cartao.setValidade(validade);
        cartao.setNomeTitular(nomeTitular);
        cartao.setTipo(tipo);
        return cartaoRepository.save(cartao);
    }
    
    private String encriptar(String valor) {
        try {
            SecretKeySpec chave = new SecretKeySpec(CHAVE_ENCRIPTACAO.getBytes(), ALGORITMO);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chave);
            byte[] valorEncriptado = cipher.doFinal(valor.getBytes());
            return Base64.getEncoder().encodeToString(valorEncriptado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao encriptar: " + e.getMessage());
        }
    }
    
    public String decriptar(String valorEncriptado) {
        try {
            SecretKeySpec chave = new SecretKeySpec(CHAVE_ENCRIPTACAO.getBytes(), ALGORITMO);
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave);
            byte[] decriptado = cipher.doFinal(Base64.getDecoder().decode(valorEncriptado));
            return new String(decriptado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao decriptar: " + e.getMessage());
        }
    }
}
