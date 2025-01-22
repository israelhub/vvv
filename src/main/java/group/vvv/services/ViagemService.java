package group.vvv.services;

import group.vvv.models.viagem.*;
import group.vvv.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private ConexaoRepository roteiroRepository;

    private Conexao adicionarLocal(Viagem viagem, Long localId, Long modalId, Conexao.Tipo tipo) {
        Conexao viagemLocal = new Conexao();
        viagemLocal.setId(new Conexao.ViagemLocalId(viagem.getId_viagem(), localId));
        viagemLocal.setViagem(viagem);
        viagemLocal.setLocal(new Local());
        viagemLocal.getLocal().setId_local(localId);
        viagemLocal.setTipo(tipo);

        Modal modal = new Modal();
        modal.setId_modal(modalId);
        viagemLocal.setModal(modal);

        return roteiroRepository.save(viagemLocal);
    }

    public Viagem criarViagem(Long origemLocal, Long destinoLocal,
            List<Long> escalaLocal, Long modalOrigem,
            List<Long> modalEscala,
            LocalTime horarioPartida, LocalTime horarioChegada,
            Date dataPartida, Date dataChegada,
            BigDecimal valor,
            List<Map<String, Object>> escalasInfo) throws Exception {

        Viagem viagem = new Viagem();
        viagem.setNumReservasAssociadas(0);
        viagem.setValor(valor);
        viagem = viagemRepository.save(viagem);

        Conexao origem = adicionarLocal(viagem, origemLocal, modalOrigem, Conexao.Tipo.ORIGEM);
        origem.setHorarioPartida(horarioPartida);
        origem.setDataPartida(dataPartida);
        roteiroRepository.save(origem);

        if (escalaLocal != null && !escalaLocal.isEmpty()) {
            for (int i = 0; i < escalaLocal.size(); i++) {
                Conexao escalaLocalObj = adicionarLocal(viagem, escalaLocal.get(i),
                        modalEscala.get(i), Conexao.Tipo.ESCALA);
                Map<String, Object> escalaInfo = escalasInfo.get(i);
                escalaLocalObj.setHorarioPartida((LocalTime) escalaInfo.get("horario"));
                escalaLocalObj.setDataPartida((Date) escalaInfo.get("data"));
                roteiroRepository.save(escalaLocalObj);
            }
        }

        Conexao destino = new Conexao();
        destino.setId(new Conexao.ViagemLocalId(viagem.getId_viagem(), destinoLocal));
        destino.setViagem(viagem);
        destino.setLocal(new Local());
        destino.getLocal().setId_local(destinoLocal);
        destino.setTipo(Conexao.Tipo.DESTINO);
        destino.setHorarioChegada(horarioChegada);
        destino.setDataChegada(dataChegada);
        roteiroRepository.save(destino);

        return viagemRepository.findById(viagem.getId_viagem())
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada após salvar"));
    }

    public List<Viagem> getViagens() {
        return viagemRepository.findAllWithDetails();
    }

    public Viagem getViagemById(Long id) {
        return viagemRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new RuntimeException("Viagem não encontrada com ID: " + id));
    }

    public void atualizarViagem(Viagem viagem) {
        viagemRepository.save(viagem);
    }
}