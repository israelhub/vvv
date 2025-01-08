package group.vvv.controllers;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import group.vvv.models.Funcionario;
import group.vvv.models.PontoFuncionario;
import group.vvv.services.FuncionarioService;
import group.vvv.services.PontoDeVendaService;

@Controller
@RequestMapping("/web/funcionarios")
public class FuncionarioWebController {
    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private PontoDeVendaService pontoDeVendaService;

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        model.addAttribute("pontosDeVenda", pontoDeVendaService.listarTodos());
        return "funcionario/areaCadastroFuncionario";
    }

    @PostMapping
    public String cadastrarFuncionarioWeb(@ModelAttribute Funcionario funcionario, 
                                        @RequestParam(required = false) List<Long> pontoDeVenda,
                                        @RequestParam(required = false) List<String> diaSemana,
                                        @RequestParam(required = false) List<LocalTime> horarioInicial,
                                        @RequestParam(required = false) List<LocalTime> horarioFinal, 
                                        Model model) {
        try {
            Funcionario novoFuncionario = funcionarioService.cadastrar(funcionario);
    
            if (pontoDeVenda != null && diaSemana != null && 
                horarioInicial != null && horarioFinal != null) {
    
                int index = 0;
                // Itera sobre cada ponto de venda
                for (int i = 0; i < pontoDeVenda.size(); i++) {
                    Long idPonto = pontoDeVenda.get(i);
                    
                    // Conta quantos dias existem para este ponto de venda
                    int diasCount = 0;
                    while (index + diasCount < diaSemana.size() && 
                           diaSemana.get(index + diasCount) != null && 
                           !diaSemana.get(index + diasCount).isEmpty()) {
                        diasCount++;
                    }
    
                    // Salva cada dia para este ponto de venda
                    for (int j = 0; j < diasCount; j++) {
                        String dia = diaSemana.get(index + j);
                        LocalTime horaInicial = horarioInicial.get(index + j);
                        LocalTime horaFinal = horarioFinal.get(index + j);
    
                        if (idPonto != null && dia != null && !dia.isEmpty() &&
                            horaInicial != null && horaFinal != null) {
                            
                            PontoFuncionario pontoFuncionario = new PontoFuncionario();
                            PontoFuncionario.PontoFuncionarioId id = new PontoFuncionario.PontoFuncionarioId(
                                novoFuncionario.getId_funcionario(),
                                idPonto,
                                PontoFuncionario.DiaSemana.valueOf(dia.toUpperCase())
                            );
                            pontoFuncionario.setId(id);
                            pontoFuncionario.setFuncionario(novoFuncionario);
                            pontoFuncionario.setPontoDeVenda(pontoDeVendaService.buscarPorId(idPonto));
                            pontoFuncionario.setHorarioInicial(horaInicial);
                            pontoFuncionario.setHorarioFinal(horaFinal);
    
                            funcionarioService.cadastrarPontoFuncionario(pontoFuncionario);
                        }
                    }
                    index += diasCount;
                }
            }
    
            model.addAttribute("mensagem", "Funcionário cadastrado com sucesso! Código: " + 
                novoFuncionario.getCodigo_funcionario() + " Senha: " + novoFuncionario.getSenha());
            return "funcionario/areaCadastroFuncionario";
    
        } catch (Exception e) {
            model.addAttribute("mensagem", "Erro ao cadastrar funcionário: " + e.getMessage());
            model.addAttribute("pontosDeVenda", pontoDeVendaService.listarTodos());
            return "funcionario/areaCadastroFuncionario";
        }
    }


    @GetMapping("/login")
    public String exibirFormularioLogin(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionario/loginFuncionario";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Funcionario funcionario, Model model) {
        Funcionario funcionarioExistente = funcionarioService.login(funcionario.getEmail(), funcionario.getSenha());
        if (funcionarioExistente != null) {
            if (!funcionarioExistente.isLoginInicialRealizado()) {
                model.addAttribute("funcionario", funcionarioExistente);
                return "funcionario/atualizarDadosFuncionario";
            } else {
                // Redirecionar para o endpoint de administração (a ser configurado no futuro)
                return "redirect:/web/administracao";
            }
        } else {
            model.addAttribute("mensagem", "Email ou senha inválidos.");
            return "funcionario/loginFuncionario";
        }
    }

    @GetMapping("/login-inicial")
    public String exibirFormularioLoginInicial(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionario/loginInicialFuncionario";
    }

    @PostMapping("/atualizar-dados")
    public String atualizarDadosFuncionario(@ModelAttribute Funcionario funcionario, Model model) {
        funcionarioService.atualizarDados(funcionario);
        model.addAttribute("mensagem", "Dados atualizados com sucesso!");
        return "funcionario/atualizarDadosFuncionario";
    }
}