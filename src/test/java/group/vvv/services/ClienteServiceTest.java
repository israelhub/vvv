package group.vvv.services;

import group.vvv.models.Cliente;
import group.vvv.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrar() {
        Cliente cliente = new Cliente();
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.cadastrar(cliente);

        assertNotNull(result);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void testListarTodos() {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> result = clienteService.listarTodos();

        assertEquals(2, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarPorId() {
        Cliente cliente = new Cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.buscarPorId(1L);

        assertNotNull(result);
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeletar() {
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.deletar(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testLogin() {
        Cliente cliente = new Cliente();
        when(clienteRepository.findByEmailAndSenha("email@example.com", "senha")).thenReturn(cliente);

        Cliente result = clienteService.login("email@example.com", "senha");

        assertNotNull(result);
        verify(clienteRepository, times(1)).findByEmailAndSenha("email@example.com", "senha");
    }
}