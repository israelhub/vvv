package group.vvv.services;

import group.vvv.models.Funcionario;
import group.vvv.models.PontoFuncionario;
import group.vvv.repositories.FuncionarioRepository;
import group.vvv.repositories.PontoFuncionarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private PontoFuncionarioRepository pontoFuncionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrar() {
        Funcionario funcionario = new Funcionario();
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionario);

        Funcionario result = funcionarioService.cadastrar(funcionario);

        assertNotNull(result.getSenha());
        assertEquals(8, result.getSenha().length());
        verify(funcionarioRepository, times(1)).save(funcionario);
    }

    @Test
    void testCadastrarPontoFuncionario() {
        PontoFuncionario pontoFuncionario = new PontoFuncionario();
        funcionarioService.cadastrarPontoFuncionario(pontoFuncionario);

        verify(pontoFuncionarioRepository, times(1)).save(pontoFuncionario);
    }

    @Test
    void testLogin() {
        String email = "test@example.com";
        String senha = "password";
        Funcionario funcionario = new Funcionario();
        when(funcionarioRepository.findByEmailAndSenha(email, senha)).thenReturn(funcionario);

        Funcionario result = funcionarioService.login(email, senha);

        assertEquals(funcionario, result);
    }

    @Test
    void testAtualizarDados() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId_funcionario(1L);
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionario));

        funcionarioService.atualizarDados(funcionario);

        verify(funcionarioRepository, times(1)).save(funcionario);
    }

    @Test
    void testListarGerentes() {
        funcionarioService.listarGerentes();

        verify(funcionarioRepository, times(1)).findByCargo(Funcionario.Cargo.GERENTE);
    }
}