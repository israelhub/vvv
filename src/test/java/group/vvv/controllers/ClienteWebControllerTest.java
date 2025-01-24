package group.vvv.controllers;

import group.vvv.config.UserSession;
import group.vvv.models.Cliente;
import group.vvv.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

public class ClienteWebControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private UserSession userSession;

    @Mock
    private Model model;

    @InjectMocks
    private ClienteWebController clienteWebController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExibirFormularioCadastro() {
        String viewName = clienteWebController.exibirFormularioCadastro(model);
        verify(model, times(1)).addAttribute(eq("cliente"), any(Cliente.class));
        assertEquals("cliente/areaCadastroCliente", viewName);
    }

    @Test
    public void testCadastrarClienteWebSuccess() {
        Cliente cliente = new Cliente();
        cliente.setNome("Test Name");
        cliente.setData_nascimento(LocalDate.of(1990, 1, 1));
        cliente.setEmail("test@example.com");
        cliente.setSenha("password");
        cliente.setCpf("12345678901");
        cliente.setTelefone("1234567890");
        doReturn(cliente).when(clienteService).cadastrar(cliente);

        String viewName = clienteWebController.cadastrarClienteWeb(cliente, model);
        verify(clienteService, times(1)).cadastrar(cliente);
        verify(model, times(1)).addAttribute("mensagem", "Cliente cadastrado com sucesso!");
        assertEquals("cliente/areaCadastroCliente", viewName);
    }

    @Test
    public void testCadastrarClienteWebFailure() {
        Cliente cliente = new Cliente();
        doThrow(new RuntimeException("Error")).when(clienteService).cadastrar(cliente);

        String viewName = clienteWebController.cadastrarClienteWeb(cliente, model);
        verify(clienteService, times(1)).cadastrar(cliente);
        verify(model, times(1)).addAttribute("mensagem", "Erro ao cadastrar: Error");
        assertEquals("cliente/areaCadastroCliente", viewName);
    }

    @Test
    public void testExibirFormularioLogin() {
        String viewName = clienteWebController.exibirFormularioLogin(model);
        verify(model, times(1)).addAttribute(eq("cliente"), any(Cliente.class));
        assertEquals("cliente/loginCliente", viewName);
    }

    @Test
    public void testLoginSuccess() {
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        cliente.setSenha("password");
        Cliente clienteLogado = new Cliente();
        when(clienteService.login(cliente.getEmail(), cliente.getSenha())).thenReturn(clienteLogado);

        String viewName = clienteWebController.login(cliente, model);
        verify(clienteService, times(1)).login(cliente.getEmail(), cliente.getSenha());
        verify(userSession, times(1)).login(clienteLogado);
        assertEquals("redirect:/web/paginaInicial", viewName);
    }

    @Test
    public void testLoginFailure() {
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        cliente.setSenha("password");
        when(clienteService.login(cliente.getEmail(), cliente.getSenha())).thenReturn(null);

        String viewName = clienteWebController.login(cliente, model);
        verify(clienteService, times(1)).login(cliente.getEmail(), cliente.getSenha());
        verify(userSession, times(0)).login(any(Cliente.class));
        verify(model, times(1)).addAttribute("mensagem", "Email ou senha inválidos.");
        assertEquals("cliente/loginCliente", viewName);
    }

    @Test
    public void testLogout() {
        String viewName = clienteWebController.logout();
        verify(userSession, times(1)).logout();
        assertEquals("redirect:/web/paginaInicial", viewName);
    }
}