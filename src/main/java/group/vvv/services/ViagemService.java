package group.vvv.services;

import group.vvv.models.viagem.*;
import group.vvv.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    public void salvarViagem(Viagem viagem) {
        viagemRepository.save(viagem);
    }

    public List<Viagem> getViagens() {
        return viagemRepository.findAllWithEscalas();
    }

    public List<Viagem> getViagensDisponiveis() {
        List<Viagem> todasViagens = viagemRepository.findAll();
        return todasViagens.stream()
                .filter(viagem -> {
                    // Encontra a menor capacidade entre os modais da viagem
                    final int capacidadeInicial = viagem.getModal().getCapacidade();
                    int menorCapacidade = capacidadeInicial;

                    // Verifica também os modais das escalas
                    if (viagem.getEscalas() != null) {
                        menorCapacidade = viagem.getEscalas().stream()
                                .map(escala -> escala.getModal().getCapacidade())
                                .min(Integer::compare)
                                .map(cap -> Math.min(cap, capacidadeInicial))
                                .orElse(capacidadeInicial);
                    }

                    // Retorna true se ainda há capacidade disponível
                    return viagem.getNumReservasAssociadas() < menorCapacidade;
                })
                .collect(Collectors.toList());
    }

    public Viagem getViagemById(Long id) {
        return viagemRepository.findByIdWithEscalas(id)
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));
    }

    public void deletarViagem(Long id) {
        viagemRepository.deleteById(id);
    }

    public void atualizarViagem(Viagem viagem) {
        viagemRepository.save(viagem);
    }
}