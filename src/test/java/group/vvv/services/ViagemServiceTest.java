package group.vvv.services;

import group.vvv.models.viagem.*;
import group.vvv.repositories.ViagemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class ViagemServiceTest {

    @InjectMocks
    private ViagemService viagemService;

    @Mock
    private ViagemRepository viagemRepository;

    @Mock
    private EscalaService escalaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalvarViagemComEscalas() {
        // Arrange
        Viagem viagem = new Viagem();
        viagem.setValor(new BigDecimal("100.00"));
        viagem.setHorarioPartida(LocalDateTime.now());
        viagem.setHorarioChegada(LocalDateTime.now().plusHours(2));
        
        List<Escala> escalas = new ArrayList<>();
        Escala escala1 = new Escala();
        escala1.setHorarioPartida(LocalDateTime.now().plusMinutes(30));
        escala1.setHorarioChegada(LocalDateTime.now().plusMinutes(90));
        escala1.setOrdem(1);
        escalas.add(escala1);
        
        viagem.setEscalas(escalas);

        when(viagemRepository.save(any(Viagem.class))).thenReturn(viagem);

        // Act
        viagemService.salvarViagem(viagem);

        // Assert
        verify(viagemRepository, times(1)).save(viagem);
        verify(escalaService, times(1)).salvarEscalas(escalas);
        
        // Verifica se cada escala tem a referência para a viagem
        for (Escala escala : escalas) {
            verify(escalaService, times(1)).salvarEscalas(argThat(list -> 
                list.stream().allMatch(e -> e.getViagem() == viagem)
            ));
        }
    }

    @Test
    void testSalvarViagemSemEscalas() {
        // Arrange
        Viagem viagem = new Viagem();
        viagem.setValor(new BigDecimal("100.00"));
        viagem.setHorarioPartida(LocalDateTime.now());
        viagem.setHorarioChegada(LocalDateTime.now().plusHours(2));

        when(viagemRepository.save(any(Viagem.class))).thenReturn(viagem);

        // Act
        viagemService.salvarViagem(viagem);

        // Assert
        verify(viagemRepository, times(1)).save(viagem);
        verify(escalaService, never()).salvarEscalas(any());
    }

    @Test
void testSalvarViagemComValidacaoCompleta() {
    // Arrange
    Viagem viagem = new Viagem();
    viagem.setOrigem(new Local()); // Simular local origem
    viagem.setDestino(new Local()); // Simular local destino
    viagem.setModal(new Modal()); // Simular modal
    viagem.setValor(new BigDecimal("100.00"));
    viagem.setHorarioPartida(LocalDateTime.now());
    viagem.setHorarioChegada(LocalDateTime.now().plusHours(2));
    viagem.setNumReservasAssociadas(0);

    when(viagemRepository.save(any(Viagem.class))).thenReturn(viagem);

    // Act
    viagemService.salvarViagem(viagem);

    // Assert
    verify(viagemRepository).save(argThat(v -> 
        v.getOrigem() != null &&
        v.getDestino() != null &&
        v.getModal() != null &&
        v.getValor().compareTo(BigDecimal.ZERO) > 0 &&
        v.getHorarioPartida().isBefore(v.getHorarioChegada())
    ));
}

@Test
void testSalvarViagemAtualizacao() {
    // Arrange
    Viagem viagem = new Viagem();
    viagem.setId_viagem(1L); // Simular viagem existente
    
    // Act
    viagemService.salvarViagem(viagem);

    // Assert 
    verify(escalaService).deletarEscalasDaViagem(1L);
    verify(viagemRepository).save(viagem);
}

@Test
void testSalvarViagemComEscalasOrdenadas() {
    // Arrange
    Viagem viagem = new Viagem();
    List<Escala> escalas = new ArrayList<>();
    
    Escala escala1 = new Escala();
    escala1.setOrdem(1);
    Escala escala2 = new Escala();
    escala2.setOrdem(2);
    
    escalas.add(escala1);
    escalas.add(escala2);
    viagem.setEscalas(escalas);

    // Act
    viagemService.salvarViagem(viagem);

    // Assert
    verify(escalaService).salvarEscalas(argThat(list ->
        list.get(0).getOrdem() < list.get(1).getOrdem()
    ));
}
}