package group.vvv.controllers;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.Funcionario;
import group.vvv.models.viagem.Modal;
import group.vvv.models.viagem.Transportadora;
import group.vvv.services.ModalService;
import group.vvv.services.TransportadoraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ModalControllerTest {

    @InjectMocks
    private ModalController modalController;

    @Mock
    private ModalService modalService;

    @Mock
    private TransportadoraService transportadoraService;

    @Mock
    private FuncionarioSession funcionarioSession;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    private Funcionario funcionario;
    private Modal modal;
    private Transportadora transportadora;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar funcionário com permissão
        funcionario = new Funcionario();
        funcionario.setCargo(Funcionario.Cargo.GERENTE);
        when(funcionarioSession.getFuncionario()).thenReturn(funcionario);

        // Configurar transportadora
        transportadora = new Transportadora();
        transportadora.setIdTransportadora(1L);
        transportadora.setNome("Transportadora Teste");

        // Configurar modal válido
        modal = new Modal();
        modal.setTipo("Ônibus");
        modal.setModelo("Mercedes Benz");
        modal.setCapacidade(40);
        modal.setAno_fabricacao(2023);
        modal.setTransportadora(transportadora);
    }

    @Test
    void quandoCriarModalValido_EntaoSucesso() {
        // Arrange
        doNothing().when(modalService).cadastrar(any(Modal.class));

        // Act
        String viewName = modalController.salvarModal(modal, redirectAttributes);

        // Assert
        assertEquals("redirect:/web/administracao/modal", viewName);
        verify(redirectAttributes).addFlashAttribute("mensagem", "Modal cadastrado com sucesso!");
        verify(redirectAttributes).addFlashAttribute("tipoMensagem", "success");
        verify(modalService).cadastrar(modal);
    }

    @Test
    void quandoCriarModalInvalido_EntaoFalha() {
        // Arrange
        Modal modalInvalido = new Modal();
        doThrow(new RuntimeException("Dados inválidos")).when(modalService).cadastrar(modalInvalido);

        // Act
        String viewName = modalController.salvarModal(modalInvalido, redirectAttributes);

        // Assert
        assertEquals("redirect:/web/administracao/modal", viewName);
        verify(redirectAttributes).addFlashAttribute(eq("mensagem"), contains("Erro ao cadastrar modal"));
        verify(redirectAttributes).addFlashAttribute(eq("tipoMensagem"), eq("error"));
    }

    @Test
    void quandoAtualizarModalValido_EntaoSucesso() {
        // Arrange
        Long id = 1L;
        modal.setId_modal(id);
        doNothing().when(modalService).cadastrar(any(Modal.class));

        // Act
        String viewName = modalController.atualizarModal(id, modal, redirectAttributes);

        // Assert
        assertEquals("redirect:/web/administracao/modal", viewName);
        verify(redirectAttributes).addFlashAttribute("mensagem", "Modal atualizado com sucesso!");
        verify(redirectAttributes).addFlashAttribute("tipoMensagem", "success");
        verify(modalService).cadastrar(modal);
    }

    @Test
    void quandoAtualizarModalInvalido_EntaoFalha() {
        // Arrange
        Long id = 1L;
        Modal modalInvalido = new Modal(); // Modal sem dados obrigatórios
        doThrow(new RuntimeException("Dados inválidos")).when(modalService).cadastrar(modalInvalido);

        // Act
        String viewName = modalController.atualizarModal(id, modalInvalido, redirectAttributes);

        // Assert
        assertEquals("redirect:/web/administracao/modal", viewName);
        verify(redirectAttributes).addFlashAttribute(eq("mensagem"), contains("Erro ao atualizar modal"));
        verify(redirectAttributes).addFlashAttribute(eq("tipoMensagem"), eq("error"));
    }

    @Test
    void quandoDeletarModalExistente_EntaoSucesso() {
        // Arrange
        Long id = 1L;
        doNothing().when(modalService).deletar(id);

        // Act
        String viewName = modalController.deletarModal(id, redirectAttributes);

        // Assert
        assertEquals("redirect:/web/administracao/modal", viewName);
        verify(redirectAttributes).addFlashAttribute("mensagem", "Modal excluído com sucesso!");
        verify(redirectAttributes).addFlashAttribute("tipoMensagem", "success");
        verify(modalService).deletar(id);
    }

    @Test
    void quandoUsuarioSemPermissao_EntaoRedireciona() {
        // Arrange
        funcionario.setCargo(Funcionario.Cargo.GERENTE_DE_NEGOCIOS_VIRTUAIS);
        when(funcionarioSession.getFuncionario()).thenReturn(funcionario);

        // Act
        String viewName = modalController.modalForm(model, redirectAttributes);

        // Assert
        assertEquals("redirect:/web/administracao", viewName);
        verify(redirectAttributes).addFlashAttribute("mensagem",
                "Só o Gerente e o Funcionário podem acessar essa página");
        verify(redirectAttributes).addFlashAttribute("tipoMensagem", "error");
    }
}