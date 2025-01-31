package group.vvv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import group.vvv.models.viagem.*;
import group.vvv.repositories.*;

import java.util.List;

@Service
public class LocalService {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private AeroportoRepository aeroportoRepository;

    @Autowired
    private EstacaoRepository estacaoRepository;

    @Autowired
    private PortoRepository portoRepository;

    public List<Local> getLocais() {
        return localRepository.findAll();
    }

    public Local getLocalById(Long id) {
        return localRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Local não encontrado"));
    }

    public void cadastrar(Local local) {
        cidadeRepository.save(local.getId_cidade());

        if (local.getId_aeroporto() != null) {
            aeroportoRepository.save(local.getId_aeroporto());
        } else if (local.getId_estacao() != null) {
            estacaoRepository.save(local.getId_estacao());
        } else if (local.getId_porto() != null) {
            portoRepository.save(local.getId_porto());
        }

        localRepository.save(local);
    }

    public void deletar(Long id) {
        localRepository.deleteById(id);
    }

    public void cadastrarCidade(Cidade cidade) {
        cidadeRepository.save(cidade);
    }

    public void cadastrarAeroporto(Aeroporto aeroporto) {
        aeroportoRepository.save(aeroporto);
    }

    public void cadastrarEstacao(Estacao estacao) {
        estacaoRepository.save(estacao);
    }

    public void cadastrarPorto(Porto porto) {
        portoRepository.save(porto);
    }

    public List<Cidade> getCidades() {
        return cidadeRepository.findAll();
    }
}