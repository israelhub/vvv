package group.vvv.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente();
    }

    @Test
    public void testSetAndGetIdCliente() {
        cliente.setId_cliente(1L);
        assertEquals(1L, cliente.getId_cliente());
    }

    @Test
    public void testSetAndGetNome() {
        cliente.setNome("João Silva");
        assertEquals("João Silva", cliente.getNome());
    }

    @Test
    public void testSetAndGetDataNascimento() {
        LocalDate dataNascimento = LocalDate.of(1990, 1, 1);
        cliente.setData_nascimento(dataNascimento);
        assertEquals(dataNascimento, cliente.getData_nascimento());
    }

    @Test
    public void testSetAndGetEmail() {
        cliente.setEmail("joao.silva@example.com");
        assertEquals("joao.silva@example.com", cliente.getEmail());
    }

    @Test
    public void testSetAndGetSenha() {
        cliente.setSenha("senha123");
        assertEquals("senha123", cliente.getSenha());
    }

    @Test
    public void testSetAndGetCpf() {
        cliente.setCpf("12345678901");
        assertEquals("12345678901", cliente.getCpf());
    }

    @Test
    public void testSetAndGetProfissao() {
        cliente.setProfissao("Engenheiro");
        assertEquals("Engenheiro", cliente.getProfissao());
    }

    @Test
    public void testSetAndGetCep() {
        cliente.setCep("12345678");
        assertEquals("12345678", cliente.getCep());
    }

    @Test
    public void testSetAndGetRua() {
        cliente.setRua("Rua das Flores");
        assertEquals("Rua das Flores", cliente.getRua());
    }

    @Test
    public void testSetAndGetNumeroRua() {
        cliente.setNumero_rua("123");
        assertEquals("123", cliente.getNumero_rua());
    }

    @Test
    public void testSetAndGetUf() {
        cliente.setUf("SP");
        assertEquals("SP", cliente.getUf());
    }

    @Test
    public void testSetAndGetTelefone() {
        cliente.setTelefone("11987654321");
        assertEquals("11987654321", cliente.getTelefone());
    }
}