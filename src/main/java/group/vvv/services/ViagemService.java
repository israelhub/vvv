package group.vvv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import group.vvv.models.viagem.*;
import group.vvv.models.viagem.ViagemLocal.ViagemLocalId;
import group.vvv.repositories.*;

import java.util.List;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private ViagemLocalRepository viagemLocalRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @Autowired
    private PortoRepository portoRepository;

    @Autowired
    private EstacaoRepository estacaoRepository;

    public Viagem criarViagem(Long origemLocalId, Long destinoLocalId, List<Long> escalaLocalIds) {
        Viagem viagem = new Viagem();
        viagem.setNumReservasAssociadas(0);
        viagem = viagemRepository.save(viagem);

        if (origemLocalId != null) {
            Local origem = localRepository.findById(origemLocalId).orElseThrow(() -> new IllegalArgumentException("Local de origem não encontrado"));
            ViagemLocalId viagemLocalIdOrigem = new ViagemLocalId(viagem.getId_viagem(), origem.getId_local());
            ViagemLocal viagemLocalOrigem = new ViagemLocal();
            viagemLocalOrigem.setId(viagemLocalIdOrigem);
            viagemLocalOrigem.setViagem(viagem);
            viagemLocalOrigem.setLocal(origem);
            viagemLocalOrigem.setTipo(ViagemLocal.Tipo.ORIGEM);
            viagemLocalRepository.save(viagemLocalOrigem);
        }

        if (destinoLocalId != null) {
            Local destino = localRepository.findById(destinoLocalId).orElseThrow(() -> new IllegalArgumentException("Local de destino não encontrado"));
            ViagemLocalId viagemLocalIdDestino = new ViagemLocalId(viagem.getId_viagem(), destino.getId_local());
            ViagemLocal viagemLocalDestino = new ViagemLocal();
            viagemLocalDestino.setId(viagemLocalIdDestino);
            viagemLocalDestino.setViagem(viagem);
            viagemLocalDestino.setLocal(destino);
            viagemLocalDestino.setTipo(ViagemLocal.Tipo.DESTINO);
            viagemLocalRepository.save(viagemLocalDestino);
        }

        if (escalaLocalIds != null) {
            for (Long escalaLocalId : escalaLocalIds) {
                Local escala = localRepository.findById(escalaLocalId).orElseThrow(() -> new IllegalArgumentException("Local de escala não encontrado"));
                ViagemLocalId viagemLocalIdEscala = new ViagemLocalId(viagem.getId_viagem(), escala.getId_local());
                ViagemLocal viagemLocalEscala = new ViagemLocal();
                viagemLocalEscala.setId(viagemLocalIdEscala);
                viagemLocalEscala.setViagem(viagem);
                viagemLocalEscala.setLocal(escala);
                viagemLocalEscala.setTipo(ViagemLocal.Tipo.ESCALA);
                viagemLocalRepository.save(viagemLocalEscala);
            }
        }

        return viagem;
    }
}