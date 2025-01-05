package group.vvv.services;

import group.vvv.models.viagem.*;
import group.vvv.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private ViagemLocalRepository viagemLocalRepository;

    @Autowired
    private ViagemModalRepository viagemModalRepository;

    public Viagem criarViagem(Long origemLocal, Long destinoLocal, List<Long> escalaLocal,
                              Long modalOrigem, Long modalDestino, List<Long> modalEscala) throws Exception {
        Viagem viagem = new Viagem();
        viagem.setNumReservasAssociadas(0); // Inicialmente, sem reservas associadas
        viagem = viagemRepository.save(viagem);

        // Adiciona locais à viagem
        adicionarLocal(viagem, origemLocal, ViagemLocal.Tipo.ORIGEM);
        adicionarLocal(viagem, destinoLocal, ViagemLocal.Tipo.DESTINO);
        if (escalaLocal != null) {
            for (Long escala : escalaLocal) {
                adicionarLocal(viagem, escala, ViagemLocal.Tipo.ESCALA);
            }
        }

        // Adiciona modais à viagem
        adicionarModal(viagem, modalOrigem, ViagemModal.Tipo.ORIGEM);
        adicionarModal(viagem, modalDestino, ViagemModal.Tipo.DESTINO);
        if (modalEscala != null) {
            for (Long escala : modalEscala) {
                adicionarModal(viagem, escala, ViagemModal.Tipo.ESCALA);
            }
        }

        return viagem;
    }

    private void adicionarLocal(Viagem viagem, Long localId, ViagemLocal.Tipo tipo) {
        ViagemLocal viagemLocal = new ViagemLocal();
        viagemLocal.setId(new ViagemLocal.ViagemLocalId(viagem.getId_viagem(), localId));
        viagemLocal.setViagem(viagem);
        viagemLocal.setLocal(new Local());
        viagemLocal.getLocal().setId_local(localId);
        viagemLocal.setTipo(tipo);
        viagemLocalRepository.save(viagemLocal);
    }

    private void adicionarModal(Viagem viagem, Long modalId, ViagemModal.Tipo tipo) {
        ViagemModal viagemModal = new ViagemModal();
        viagemModal.setId(new ViagemModal.ViagemModalId(viagem.getId_viagem(), modalId));
        viagemModal.setViagem(viagem);
        viagemModal.setModal(new Modal());
        viagemModal.getModal().setId_modal(modalId);
        viagemModal.setTipo(tipo);
        viagemModalRepository.save(viagemModal);
    }
}