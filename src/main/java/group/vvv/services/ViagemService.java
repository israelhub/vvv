package group.vvv.services;

import group.vvv.models.viagem.*;
import group.vvv.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private ViagemLocalRepository viagemLocalRepository;

    @Autowired
    private ViagemModalRepository viagemModalRepository;

    public Viagem criarViagem(Long origemLocal, Long destinoLocal,
            List<Long> escalaLocal, Long modalOrigem,
            List<Long> modalEscala,
            LocalTime horarioPartida, LocalTime horarioChegada,
            Date dataPartida, Date dataChegada,
            BigDecimal valor) throws Exception {

        Viagem viagem = new Viagem();
        viagem.setNumReservasAssociadas(0);
        viagem.setHorarioPartida(horarioPartida);
        viagem.setHorarioChegada(horarioChegada);
        viagem.setDataPartida(dataPartida);
        viagem.setDataChegada(dataChegada);
        viagem.setValor(valor);

        viagem = viagemRepository.save(viagem);

        // Código existente para adicionar locais e modais
        adicionarLocal(viagem, origemLocal, ViagemLocal.Tipo.ORIGEM);
        adicionarLocal(viagem, destinoLocal, ViagemLocal.Tipo.DESTINO);

        if (escalaLocal != null) {
            for (Long escala : escalaLocal) {
                adicionarLocal(viagem, escala, ViagemLocal.Tipo.ESCALA);
            }
        }

        adicionarModal(viagem, modalOrigem, ViagemModal.Tipo.ORIGEM);
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

    public List<Viagem> getViagens() {
        return viagemRepository.findAll();
    }

    public Viagem getViagemById(Long id) {
        return viagemRepository.findById(id).orElseThrow(() -> new RuntimeException("Viagem não encontrada"));
    }

    public void atualizarViagem(Viagem viagem) {
        viagemRepository.save(viagem);
    }
}