package group.vvv.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioTest {

    @Test
    void testFuncionarioFields() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("John Doe");
        funcionario.setEmail("john.doe@example.com");
        funcionario.setSenha("password");
        funcionario.setCep("12345678");
        funcionario.setRua("Main St");
        funcionario.setNumero_rua("123");
        funcionario.setCargo(Funcionario.Cargo.GERENTE);
        funcionario.setLoginInicialRealizado(true);

        assertEquals("John Doe", funcionario.getNome());
        assertEquals("john.doe@example.com", funcionario.getEmail());
        assertEquals("password", funcionario.getSenha());
        assertEquals("12345678", funcionario.getCep());
        assertEquals("Main St", funcionario.getRua());
        assertEquals("123", funcionario.getNumero_rua());
        assertEquals(Funcionario.Cargo.GERENTE, funcionario.getCargo());
        assertTrue(funcionario.isLoginInicialRealizado());
    }
}