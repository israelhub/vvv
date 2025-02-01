package group.vvv.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import group.vvv.models.PontoDeVenda;
import group.vvv.repositories.PontoDeVendaRepository;

@Service
public class PontoDeVendaService {

    @Autowired
    private PontoDeVendaRepository pontoDeVendaRepository;

    public PontoDeVenda cadastrar(PontoDeVenda pontoDeVenda) {
        return pontoDeVendaRepository.save(pontoDeVenda);
    }

    public List<PontoDeVenda> listarTodos() {
        return pontoDeVendaRepository.findAll();
    }

    public PontoDeVenda buscarPorId(Long id) {
        return pontoDeVendaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ponto de Venda não encontrado"));
    }

    public void deletar(Long id) {
        pontoDeVendaRepository.deleteById(id);
    }

    public void atualizar(PontoDeVenda pontoDeVenda) {
        pontoDeVendaRepository.save(pontoDeVenda);
    }
}