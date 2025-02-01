package group.vvv.services;

import group.vvv.models.viagem.*;
import group.vvv.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private EscalaService escalaService;

    @Transactional
    public void salvarViagem(Viagem viagem) {
        if (viagem.getId_viagem() != null) {
            Viagem viagemExistente = viagemRepository.findById(viagem.getId_viagem())
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));
            
            // Atualiza dados básicos
            viagemExistente.setOrigem(viagem.getOrigem());
            viagemExistente.setDestino(viagem.getDestino());
            viagemExistente.setModal(viagem.getModal());
            viagemExistente.setHorarioPartida(viagem.getHorarioPartida());
            viagemExistente.setHorarioChegada(viagem.getHorarioChegada());
            viagemExistente.setValor(viagem.getValor());
            
            // Limpa as escalas existentes
            viagemExistente.getEscalas().clear();
            
            // Adiciona as novas escalas
            if (viagem.getEscalas() != null) {
                for (Escala escala : viagem.getEscalas()) {
                    Escala novaEscala = new Escala();
                    novaEscala.setViagem(viagemExistente);
                    novaEscala.setOrigem(escala.getOrigem());
                    novaEscala.setDestino(escala.getDestino());
                    novaEscala.setModal(escala.getModal());
                    novaEscala.setHorarioPartida(escala.getHorarioPartida());
                    novaEscala.setHorarioChegada(escala.getHorarioChegada());
                    novaEscala.setOrdem(escala.getOrdem());
                    viagemExistente.getEscalas().add(novaEscala);
                }
            }
            
            // Salva a viagem atualizada
            viagemRepository.save(viagemExistente);
        } else {
            // Para nova viagem
            if (viagem.getEscalas() != null) {
                viagem.getEscalas().forEach(escala -> escala.setViagem(viagem));
            }
            viagemRepository.save(viagem);
        }
    }

    public List<Viagem> getViagens() {
        return viagemRepository.findAllWithEscalas();
    }

    public Viagem getViagemById(Long id) {
        return viagemRepository.findByIdWithEscalas(id)
            .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));
    }

    @Transactional
    public void deletarViagem(Long id) {
        viagemRepository.deleteById(id);
    }

    @Transactional
    public void atualizarViagem(Viagem viagem) {
        salvarViagem(viagem);
    }
}