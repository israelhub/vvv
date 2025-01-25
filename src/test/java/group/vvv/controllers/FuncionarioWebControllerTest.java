package group.vvv.controllers;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.services.FuncionarioService;
import group.vvv.services.PontoDeVendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FuncionarioWebControllerTest {

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private PontoDeVendaService pontoDeVendaService;

    @Mock
    private FuncionarioSession funcionarioSession;

    @Mock
    private Model model;

    @InjectMocks
    private FuncionarioWebController funcionarioWebController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExibirFormularioLogin() {
        String viewName = funcionarioWebController.exibirFormularioLogin(model);

        assertEquals("funcionario/loginFuncionario", viewName);
        verify(model, times(1)).addAttribute(eq("funcionario"), any(Funcionario.class));
    }

    @Test
    void testLogin() {
        Funcionario funcionario = new Funcionario();
        funcionario.setEmail("test@example.com");
        funcionario.setSenha("password");
        when(funcionarioService.login(funcionario.getEmail(), funcionario.getSenha())).thenReturn(funcionario);

        String viewName = funcionarioWebController.login(funcionario, model);

        assertEquals("redirect:/web/administracao", viewName);
        verify(funcionarioSession, times(1)).login(funcionario);
    }

    @Test
    void testLoginInvalid() {
        Funcionario funcionario = new Funcionario();
        funcionario.setEmail("test@example.com");
        funcionario.setSenha("wrongpassword");
        when(funcionarioService.login(funcionario.getEmail(), funcionario.getSenha())).thenReturn(null);

        String viewName = funcionarioWebController.login(funcionario, model);

        assertEquals("funcionario/loginFuncionario", viewName);
        verify(model, times(1)).addAttribute("mensagem", "Email ou senha inválidos.");
    }

    @Test
    void testLogout() {
        String viewName = funcionarioWebController.logout();

        assertEquals("redirect:/web/funcionarios/login", viewName);
        verify(funcionarioSession, times(1)).logout();
    }

    @Test
    void testExibirFormularioLoginInicial() {
        String viewName = funcionarioWebController.exibirFormularioLoginInicial(model);

        assertEquals("funcionario/loginInicialFuncionario", viewName);
        verify(model, times(1)).addAttribute(eq("funcionario"), any(Funcionario.class));
    }

    @Test
    void testAtualizarDadosFuncionario() {
        Funcionario funcionario = new Funcionario();

        String viewName = funcionarioWebController.atualizarDadosFuncionario(funcionario, model);

        assertEquals("funcionario/atualizarDadosFuncionario", viewName);
        verify(funcionarioService, times(1)).atualizarDados(funcionario);
        verify(model, times(1)).addAttribute("mensagem", "Dados atualizados com sucesso!");
    }
}