package group.vvv.controllers;

import group.vvv.models.viagem.Viagem;
import group.vvv.services.LocalService;
import group.vvv.services.ModalService;
import group.vvv.services.ViagemService;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/web/administracao/viagem")
public class ViagemCadastroController {

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private LocalService localService;

    @Autowired
    private ModalService modalService;

    @Autowired
    private HttpSession session;

    @GetMapping
    public String iniciarCadastro() {
        session.removeAttribute("viagemTemp");
        return "redirect:/web/administracao/viagem/origem";
    }

    @GetMapping("/origem")
    public String origemForm(Model model) {
        model.addAttribute("locais", localService.getLocais());
        model.addAttribute("modais", modalService.getModais());
        return "admin/viagem-form";
    }

    @PostMapping("/origem")
    public String salvarOrigem(
            @RequestParam Long origemLocal,
            @RequestParam Long modalOrigem,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataPartida,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horarioPartida) {

        Map<String, Object> viagemTemp = new HashMap<>();
        viagemTemp.put("origemLocal", origemLocal);
        viagemTemp.put("modalOrigem", modalOrigem);
        viagemTemp.put("dataPartida", Date.valueOf(dataPartida)); // Converte LocalDate para Date
        viagemTemp.put("horarioPartida", horarioPartida);

        session.setAttribute("viagemTemp", viagemTemp);

        return "redirect:/web/administracao/viagem/escalas";
    }

    @GetMapping("/escalas")
    public String escalasForm(Model model) {
        model.addAttribute("locais", localService.getLocais());
        model.addAttribute("modais", modalService.getModais());
        return "admin/viagemEscala-form";
    }

    @PostMapping("/escalas")
    public String salvarEscalas(@RequestParam String escalasJson, RedirectAttributes ra) {
        try {
            Map<String, Object> viagemTemp = (Map<String, Object>) session.getAttribute("viagemTemp");
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> escalas = mapper.readValue(escalasJson,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            for (Map<String, Object> escala : escalas) {
                String dataStr = (String) escala.get("data");
                String horaStr = (String) escala.get("horario");

                LocalDate data = LocalDate.parse(dataStr);
                LocalTime hora = LocalTime.parse(horaStr);

                escala.put("data", Date.valueOf(data));
                escala.put("horario", hora);
            }

            viagemTemp.put("escalas", escalas);
            session.setAttribute("viagemTemp", viagemTemp);
            return "redirect:/web/administracao/viagem/destino";
        } catch (JsonProcessingException e) {
            ra.addFlashAttribute("mensagem", "Erro ao processar dados das escalas: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao/viagem/escalas";
        }
    }

    @GetMapping("/destino")
    public String destinoForm(Model model) {
        model.addAttribute("locais", localService.getLocais());
        return "admin/viagemDestino-form";
    }

    @PostMapping("/destino")
    public String finalizar(
            @RequestParam Long destinoLocal,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataChegada,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime horarioChegada,
            @RequestParam BigDecimal valor,
            RedirectAttributes ra) {

        try {
            Map<String, Object> viagemTemp = (Map<String, Object>) session.getAttribute("viagemTemp");

            List<Map<String, Object>> escalas = (List<Map<String, Object>>) viagemTemp.getOrDefault("escalas",
                    new ArrayList<>());
            List<Long> escalaLocal = escalas.stream()
                    .map(e -> Long.parseLong(e.get("localId").toString()))
                    .collect(Collectors.toList());

            List<Long> modalEscala = escalas.stream()
                    .map(e -> Long.parseLong(e.get("modalId").toString()))
                    .collect(Collectors.toList());

            Viagem viagem = viagemService.criarViagem(
                    (Long) viagemTemp.get("origemLocal"),
                    destinoLocal,
                    escalaLocal,
                    (Long) viagemTemp.get("modalOrigem"),
                    modalEscala,
                    (LocalTime) viagemTemp.get("horarioPartida"),
                    horarioChegada,
                    (Date) viagemTemp.get("dataPartida"),
                    Date.valueOf(dataChegada),
                    valor,
                    escalas);

            session.removeAttribute("viagemTemp");

            ra.addFlashAttribute("mensagem", "Viagem cadastrada com sucesso!");
            ra.addFlashAttribute("tipoMensagem", "success");
            ra.addFlashAttribute("locais", localService.getLocais());

            return "redirect:/web/administracao/viagem/destino";

        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar viagem: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            ra.addFlashAttribute("locais", localService.getLocais());
            return "redirect:/web/administracao/viagem/destino";
        }
    }
}