package group.vvv.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.models.viagem.*;
import group.vvv.services.LocalService;
import group.vvv.services.CidadeService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LocalControllerTest {

    @InjectMocks
    private LocalController localController;

    @Mock
    private LocalService localService;

    @Mock
    private CidadeService cidadeService;

    @Mock
    private FuncionarioSession funcionarioSession;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    private Funcionario gerenteMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configura um gerente mock
        gerenteMock = new Funcionario();
        gerenteMock.setCargo(Funcionario.Cargo.GERENTE);
        when(funcionarioSession.getFuncionario()).thenReturn(gerenteMock);
    }

    @Test
    void testCadastrarLocalComDadosValidos() {
        // Arrange
        String tipoCidade = "nova";
        String nomeCidade = "São Paulo";
        String codigoCidade = "SP";
        String tipo = "aeroporto";
        String nomeInfraestrutura = "Aeroporto de Guarulhos";
        String codigoAeroporto = "GRU";

        // Act
        String viewName = localController.cadastrarLocal(
                tipoCidade, null, nomeCidade, codigoCidade,
                tipo, nomeInfraestrutura, codigoAeroporto, redirectAttributes);

        // Assert
        verify(localService).cadastrar(any(Local.class));
        verify(redirectAttributes).addFlashAttribute("mensagem", "Local cadastrado com sucesso!");
        verify(redirectAttributes).addFlashAttribute("tipoMensagem", "success");
        assertEquals("redirect:/web/administracao/local", viewName);
    }

    @Test
    void testCadastrarLocalComDadosInvalidos() {
        // Arrange
        doThrow(new RuntimeException("Erro ao cadastrar"))
                .when(localService).cadastrar(any(Local.class));

        // Act
        String viewName = localController.cadastrarLocal(
                "nova", null, "", "",
                "aeroporto", "", "", redirectAttributes);

        // Assert
        verify(redirectAttributes).addFlashAttribute(eq("mensagem"), contains("Erro ao cadastrar"));
        verify(redirectAttributes).addFlashAttribute("tipoMensagem", "error");
        assertEquals("redirect:/web/administracao/local", viewName);
    }

    @Test
    void testAtualizarLocalComDadosValidos() {
        // Arrange
        Long id = 1L;
        Local localExistente = new Local();
        localExistente.setId_cidade(new Cidade()); // Adiciona cidade para evitar NullPointer
        when(localService.getLocalById(id)).thenReturn(localExistente);
        doNothing().when(localService).cadastrar(any(Local.class)); // Configura comportamento do mock

        // Act
        String viewName = localController.atualizarLocal(
                id, "São Paulo", "SP", "aeroporto",
                "Aeroporto de Guarulhos", "GRU", redirectAttributes);

        // Assert
        verify(localService, times(1)).cadastrar(any(Local.class)); // Verifica se foi chamado exatamente 1 vez
        verify(redirectAttributes).addFlashAttribute("mensagem", "Local atualizado com sucesso!");
        verify(redirectAttributes).addFlashAttribute("tipoMensagem", "success");
        assertEquals("redirect:/web/administracao/local", viewName);
    }

    @Test
    void testAtualizarLocalComDadosInvalidos() {
        // Arrange
        Long id = 1L;
        when(localService.getLocalById(id))
                .thenThrow(new RuntimeException("Local não encontrado"));

        // Act
        String viewName = localController.atualizarLocal(
                id, "", "", "", "", "", redirectAttributes);

        // Assert
        verify(redirectAttributes).addFlashAttribute(eq("mensagem"), contains("Erro ao atualizar"));
        verify(redirectAttributes).addFlashAttribute("tipoMensagem", "error");
        assertEquals("redirect:/web/administracao/local", viewName);
    }

    @Test
    void testDeletarLocalComSucesso() {
        // Arrange
        Long id = 1L;

        // Act
        String viewName = localController.deletarLocal(id, redirectAttributes);

        // Assert
        verify(localService).deletar(id);
        verify(redirectAttributes).addFlashAttribute("mensagem", "Local excluído com sucesso!");
        verify(redirectAttributes).addFlashAttribute("tipoMensagem", "success");
        assertEquals("redirect:/web/administracao/local", viewName);
    }
}