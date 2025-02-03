package group.vvv.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import group.vvv.config.UserSession;
import group.vvv.models.Cliente;
import group.vvv.services.ClienteService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private UserSession userSession;

    @Mock
    private Model model;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastroClienteComDadosValidos() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("José Silva");
        cliente.setEmail("jose@email.com");
        cliente.setSenha("123456");
        cliente.setCpf("12345678901");
        cliente.setData_nascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("11999887766");
        
        when(clienteService.cadastrar(any(Cliente.class))).thenReturn(cliente);

        // Act
        String viewName = clienteController.cadastrarClienteWeb(cliente, model);

        // Assert
        verify(clienteService).cadastrar(cliente);
        verify(model).addAttribute("mensagem", "Cliente cadastrado com sucesso!");
        assertEquals("cliente/areaCadastroCliente", viewName);
    }

    @Test 
    void testCadastroClienteComEmailInvalido() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("José Silva");
        cliente.setEmail("emailinvalido"); // Email inválido
        cliente.setSenha("123456");
        cliente.setCpf("12345678901");
        cliente.setData_nascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("11999887766");

        when(clienteService.cadastrar(any(Cliente.class)))
            .thenThrow(new RuntimeException("Email inválido"));

        // Act
        String viewName = clienteController.cadastrarClienteWeb(cliente, model);

        // Assert
        verify(model).addAttribute("mensagem", "Erro ao cadastrar: Email inválido");
        assertEquals("cliente/areaCadastroCliente", viewName);
    }

    @Test
    void testCadastroClienteComCPFInvalido() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("José Silva");
        cliente.setEmail("jose@email.com");
        cliente.setSenha("123456");
        cliente.setCpf("123"); // CPF inválido
        cliente.setData_nascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("11999887766");

        when(clienteService.cadastrar(any(Cliente.class)))
            .thenThrow(new RuntimeException("CPF deve conter 11 dígitos"));

        // Act
        String viewName = clienteController.cadastrarClienteWeb(cliente, model);

        // Assert  
        verify(model).addAttribute("mensagem", "Erro ao cadastrar: CPF deve conter 11 dígitos");
        assertEquals("cliente/areaCadastroCliente", viewName);
    }

    @Test
    void testCadastroClienteComNomeInvalido() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("A"); // Nome muito curto
        cliente.setEmail("jose@email.com");
        cliente.setSenha("123456");
        cliente.setCpf("12345678901");
        cliente.setData_nascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("11999887766");

        when(clienteService.cadastrar(any(Cliente.class)))
            .thenThrow(new RuntimeException("O nome deve ter entre 3 e 60 caracteres"));

        // Act
        String viewName = clienteController.cadastrarClienteWeb(cliente, model);

        // Assert
        verify(model).addAttribute("mensagem", "Erro ao cadastrar: O nome deve ter entre 3 e 60 caracteres");
        assertEquals("cliente/areaCadastroCliente", viewName);
    }

    @Test
    void testCadastroClienteComTelefoneInvalido() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("José Silva");
        cliente.setEmail("jose@email.com");
        cliente.setSenha("123456");
        cliente.setCpf("12345678901");
        cliente.setData_nascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("123"); // Telefone inválido

        when(clienteService.cadastrar(any(Cliente.class)))
            .thenThrow(new RuntimeException("Telefone deve ter entre 10 e 11 dígitos"));

        // Act
        String viewName = clienteController.cadastrarClienteWeb(cliente, model);

        // Assert
        verify(model).addAttribute("mensagem", "Erro ao cadastrar: Telefone deve ter entre 10 e 11 dígitos");
        assertEquals("cliente/areaCadastroCliente", viewName);
    }

    @Test
    void testCadastroClienteComCamposObrigatoriosAusentes() {
        // Arrange
        Cliente cliente = new Cliente();
        // Não preenchendo campos obrigatórios

        when(clienteService.cadastrar(any(Cliente.class)))
            .thenThrow(new RuntimeException("Campos obrigatórios não preenchidos"));

        // Act
        String viewName = clienteController.cadastrarClienteWeb(cliente, model);

        // Assert
        verify(model).addAttribute("mensagem", "Erro ao cadastrar: Campos obrigatórios não preenchidos");
        assertEquals("cliente/areaCadastroCliente", viewName);
    }
}