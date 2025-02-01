package group.vvv.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import group.vvv.models.Funcionario;
import group.vvv.models.PontoFuncionario;
import group.vvv.models.Funcionario.Cargo;
import group.vvv.repositories.FuncionarioRepository;
import group.vvv.repositories.PontoFuncionarioRepository;

import java.util.List;
import java.util.UUID;

@Service
public class FuncionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PontoFuncionarioRepository pontoFuncionarioRepository;

    public Funcionario cadastrar(Funcionario funcionario) {
        
        String senha = UUID.randomUUID().toString().substring(0, 8);
        funcionario.setSenha(senha);

        return funcionarioRepository.save(funcionario);
    }

    public void cadastrarPontoFuncionario(PontoFuncionario pontoFuncionario) {
        pontoFuncionarioRepository.save(pontoFuncionario);
    }

    public Funcionario login(String email, String senha) {
        return funcionarioRepository.findByEmailAndSenha(email, senha);
    }

    public void atualizarDados(Funcionario funcionario) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(funcionario.getId_funcionario()).orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));
        funcionarioExistente.setCep(funcionario.getCep());
        funcionarioExistente.setRua(funcionario.getRua());
        funcionarioExistente.setNumero_rua(funcionario.getNumero_rua());
        funcionarioExistente.setSenha(funcionario.getSenha());
        funcionarioExistente.setLoginInicialRealizado(true);
        funcionarioRepository.save(funcionarioExistente);
    }

    public void atualizarDados(Funcionario funcionario, List<PontoFuncionario> novosHorarios) {
        // Remove horários antigos
        pontoFuncionarioRepository.deleteByFuncionarioId(funcionario.getId_funcionario());
        
        // Salva funcionário atualizado
        funcionarioRepository.save(funcionario);
        
        // Adiciona novos horários
        if(novosHorarios != null) {
            novosHorarios.forEach(pf -> pontoFuncionarioRepository.save(pf));
        }
    }

    public List<Funcionario> listarGerentes() {
        return funcionarioRepository.findByCargo(Cargo.GERENTE);
    }

    public void deletar(Long id) {
        funcionarioRepository.deleteById(id);
    }

    public Funcionario buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public List<PontoFuncionario> buscarPontosFuncionario(Long id) {
        return pontoFuncionarioRepository.findByFuncionarioId(id);
    }

    public void removerPontosFuncionario(Long funcionarioId) {
        // Remove todos os pontos existentes do funcionário
        List<PontoFuncionario> pontos = buscarPontosFuncionario(funcionarioId);
        for (PontoFuncionario ponto : pontos) {
            pontoFuncionarioRepository.delete(ponto);
        }
    }

}