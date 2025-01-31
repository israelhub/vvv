package group.vvv.controllers;

import group.vvv.config.FuncionarioSession;
import group.vvv.models.*;
import group.vvv.models.Cartao.TipoCartao;
import group.vvv.models.Reserva.StatusReserva;
import group.vvv.models.viagem.Viagem;
import group.vvv.services.*;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/pontos-de-venda/reservas")
public class ReservaPresencialController {

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private PassageiroService passageiroService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private FuncionarioSession funcionarioSession;

    @Autowired
    private CartaoService cartaoService;

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("/viagem/{id}")
    public String iniciarReservaPresencial(@PathVariable Long id, Model model, RedirectAttributes ra) {
        if (funcionarioSession.getFuncionario() == null) {
            ra.addFlashAttribute("mensagem", "Funcionário precisa estar logado");
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/funcionarios/login";
        }

        try {
            Viagem viagem = viagemService.getViagemById(id);
            model.addAttribute("viagem", viagem);
            return "reservaAdmin/reservaPresencial";
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao buscar viagem: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/administracao";
        }
    }

    @PostMapping("/viagem/{id}/passageiros")
    public String cadastrarPassageirosPresencial(
            @PathVariable Long id,
            @RequestParam("nome[]") String[] nomes,
            @RequestParam("data_nascimento[]") String[] datasNascimento,
            @RequestParam("cpf[]") String[] cpfs,
            @RequestParam("telefone[]") String[] telefones,
            @RequestParam("profissao[]") String[] profissoes,
            @RequestParam(name = "responsavel[]", required = false) String[] responsaveisCpf,
            Model model,
            RedirectAttributes ra) {

        try {
            List<Passageiro> passageiros = new ArrayList<>();
            boolean hasChildren = false;
            boolean hasAdult = false;

            if (nomes.length == 1) {
                Passageiro unicoPassageiro = new Passageiro();
                unicoPassageiro.setData_nascimento(Date.valueOf(datasNascimento[0]));

                if (unicoPassageiro.getIdade() >= 2 && unicoPassageiro.getIdade() <= 10) {
                    ra.addFlashAttribute("mensagem",
                            "Não é permitido fazer reserva apenas para uma criança. É necessário ter pelo menos um passageiro adulto como responsável.");
                    ra.addFlashAttribute("tipoMensagem", "error");
                    return "redirect:/web/pontos-de-venda/reservas/viagem/" + id;
                }
            }

            for (int i = 0; i < nomes.length; i++) {
                Passageiro passageiro = new Passageiro();
                passageiro.setNome(nomes[i]);
                passageiro.setData_nascimento(Date.valueOf(datasNascimento[i]));
                passageiro.setCpf(cpfs[i]);
                passageiro.setTelefone(telefones[i]);
                passageiro.setProfissao(profissoes[i]);

                if (responsaveisCpf != null && i < responsaveisCpf.length && !responsaveisCpf[i].isEmpty()) {
                    try {
                        Passageiro responsavel = passageiroService.getPassageiroByCpf(responsaveisCpf[i]);
                        passageiro.setResponsavel(responsavel);
                    } catch (Exception e) {
                        // Trata exceção se necessário
                    }
                }

                passageiroService.salvarPassageiro(passageiro);
                passageiros.add(passageiro);

                if (passageiro.getIdade() >= 2 && passageiro.getIdade() <= 10) {
                    hasChildren = true;
                }
                if (passageiro.getIdade() > 10) {
                    hasAdult = true;
                }
            }

            Viagem viagem = viagemService.getViagemById(id);
            Reserva reserva = criarReservaPresencial(viagem, passageiros, funcionarioSession.getFuncionario());

            reservaService.salvarReserva(reserva);
            associarPassageirosReserva(reserva, passageiros);

            if (hasChildren) {
                ra.addFlashAttribute("reservaId", reserva.getId_reserva());
                return "redirect:/web/pontos-de-venda/reservas/viagem/" + reserva.getId_reserva() + "/responsaveis";
            } else {
                return "redirect:/web/pontos-de-venda/reservas/viagem/" + reserva.getId_reserva() + "/pagamento";
            }

        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao cadastrar reserva: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/pontos-de-venda/reservas/viagem/" + id;
        }
    }

    @GetMapping("/viagem/{id}/responsaveis")
    public String associarResponsaveis(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Reserva reserva = reservaService.getReservaById(id);
            List<Passageiro> criancas = reservaService.getPassageiros(reserva).stream()
                    .map(ReservaPassageiro::getPassageiro)
                    .filter(p -> p.getIdade() >= 2 && p.getIdade() <= 10)
                    .collect(Collectors.toList());
            List<Passageiro> adultos = reservaService.getPassageiros(reserva).stream()
                    .map(ReservaPassageiro::getPassageiro)
                    .filter(p -> p.getIdade() > 10)
                    .collect(Collectors.toList());

            model.addAttribute("criancas", criancas);
            model.addAttribute("adultos", adultos);
            model.addAttribute("reservaId", id);
            return "reservaAdmin/associarResponsaveisAdmin";
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", e.getMessage());
            return "redirect:/web/pontos-de-venda/viagens";
        }
    }

    @PostMapping("/viagem/{id}/responsaveis")
    public String processarResponsaveis(
            @PathVariable Long id,
            @RequestParam Map<String, String> responsaveis,
            RedirectAttributes ra) {

        try {
            Reserva reserva = reservaService.getReservaById(id);

            Map<String, String> responsaveisMap = responsaveis.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("responsaveis["))
                    .collect(Collectors.toMap(
                            entry -> entry.getKey().replaceAll("responsaveis\\[(\\d+)\\]", "$1"),
                            Map.Entry::getValue));

            responsaveisMap.forEach((criancaId, responsavelId) -> {
                Passageiro crianca = passageiroService.getPassageiroById(Long.parseLong(criancaId));
                Passageiro responsavel = passageiroService.getPassageiroById(Long.parseLong(responsavelId));
                if (crianca != null && responsavel != null) {
                    crianca.setResponsavel(responsavel);
                    passageiroService.salvarPassageiro(crianca);
                }
            });

            return "redirect:/web/pontos-de-venda/reservas/viagem/" + reserva.getId_reserva() + "/pagamento";
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao associar responsáveis: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/pontos-de-venda/reservas/viagem/" + id + "/responsaveis";
        }
    }

    @GetMapping("/viagem/{id}/pagamento")
    public String exibirPaginaPagamento(@PathVariable Long id, Model model) {
        Reserva reserva = reservaService.getReservaById(id);
        model.addAttribute("reserva", reserva);
        return "reservaAdmin/pagamentoReservaAdmin";
    }

    @PostMapping("/viagem/{id}/pagamento")
    public String processarPagamento(
            @PathVariable Long id,
            @RequestParam String numero,
            @RequestParam String cvv,
            @RequestParam String validade,
            @RequestParam String nomeTitular,
            @RequestParam TipoCartao tipoCartao,
            @RequestParam(defaultValue = "1") Integer numParcelas,
            RedirectAttributes ra,
            Model model) {
    
        try {
            Reserva reserva = reservaService.getReservaById(id);
            // Salva o cartão para o funcionário
            Cartao cartao = cartaoService.salvarCartaoParaFuncionario(numero, cvv, validade, nomeTitular, tipoCartao);
            
            // Cria o pagamento e as parcelas
            pagamentoService.criarPagamento(reserva, cartao, numParcelas);
            
            // Atualiza status da reserva
            reserva.setStatus(StatusReserva.CONFIRMADA);
            reservaService.salvarReserva(reserva);
            
            // Gera os tickets
            ticketService.gerarTickets(reserva);
            
            return "redirect:/web/pontos-de-venda/reservas/viagem/" + id + "/tickets";
        } catch (Exception e) {
            ra.addFlashAttribute("mensagem", "Erro ao processar pagamento: " + e.getMessage());
            ra.addFlashAttribute("tipoMensagem", "error");
            return "redirect:/web/pontos-de-venda/reservas/viagem/" + id + "/pagamento";
        }
    }
    @GetMapping("/viagem/{id}/tickets")
    public String exibirTickets(@PathVariable Long id, Model model) {
        Reserva reserva = reservaService.getReservaById(id);
        List<Ticket> tickets = ticketService.getTicketsByReserva(id);

        model.addAttribute("reserva", reserva);
        model.addAttribute("tickets", tickets);

        return "reservaAdmin/emitirTicketPresencial";
    }

    @GetMapping("/viagem/{id}/ticket/download")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id) {
        if (funcionarioSession.getFuncionario() == null) {
            throw new RuntimeException("Funcionário não autenticado");
        }

        try {
            Reserva reserva = reservaService.getReservaById(id);
            if (reserva.getStatus() != StatusReserva.CONFIRMADA) {
                throw new RuntimeException("Reserva não está confirmada");
            }

            List<Ticket> tickets = ticketService.getTicketsByReserva(id);
            if (tickets.isEmpty()) {
                throw new RuntimeException("Não há tickets disponíveis para esta reserva");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 90, 36);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
            Font smallFont = new Font(Font.FontFamily.HELVETICA, 8);

            for (Ticket ticket : tickets) {
                PdfContentByte cb = writer.getDirectContent();

                // Adiciona borda decorativa
                cb.setColorStroke(new BaseColor(0, 102, 204));
                cb.setLineWidth(2f);
                cb.roundRectangle(30, 30, document.getPageSize().getWidth() - 60,
                        document.getPageSize().getHeight() - 60, 10);
                cb.stroke();

                // Header com logo
                Paragraph header = new Paragraph();
                header.setAlignment(Element.ALIGN_CENTER);
                header.add(new Chunk("VVV VIAGENS\n", titleFont));
                header.add(new Chunk("Explore novos destinos\n\n",
                        new Font(Font.FontFamily.HELVETICA, 14, Font.ITALIC)));
                document.add(header);

                // Info do ticket
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(90);
                table.setSpacingBefore(20);
                table.setSpacingAfter(20);

                // Célula do localizador
                PdfPCell localizadorCell = new PdfPCell(
                        new Phrase("LOCALIZADOR: " + ticket.getLocalizador(), headerFont));
                localizadorCell.setColspan(2);
                localizadorCell.setBackgroundColor(new BaseColor(0, 102, 204));
                localizadorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                localizadorCell.setPadding(8);
                localizadorCell.setBorder(Rectangle.NO_BORDER);
                table.addCell(localizadorCell);

                // Dados do passageiro e viagem
                addTicketRow(table, "Passageiro", ticket.getPassageiro().getNome(), headerFont, normalFont);
                addTicketRow(table, "Tipo de Passagem", ticket.getTipoPassagem(), headerFont, normalFont);
                addTicketRow(table, "Origem", ticket.getReserva().getOrigem(), headerFont, normalFont);
                addTicketRow(table, "Destino", ticket.getReserva().getDestino(), headerFont, normalFont);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                addTicketRow(table, "Data",
                        ticket.getHoraPartida().toLocalDate().format(dateFormatter),
                        headerFont, normalFont);
                addTicketRow(table, "Horário Partida",
                        ticket.getHoraPartida().format(timeFormatter), headerFont, normalFont);
                addTicketRow(table, "Horário Chegada",
                        ticket.getHoraChegada().format(timeFormatter), headerFont, normalFont);

                document.add(table);

                // QR Code
                BarcodeQRCode qrCode = new BarcodeQRCode(
                        String.format("TICKET:%s;PASS:%s;DATA:%s",
                                ticket.getLocalizador(),
                                ticket.getPassageiro().getNome(),
                                ticket.getReserva().getData()),
                        100, 100, null);

                Image qrCodeImage = qrCode.getImage();
                qrCodeImage.setAlignment(Element.ALIGN_CENTER);
                qrCodeImage.scaleAbsolute(100, 100);
                document.add(qrCodeImage);

                // Rodapé
                Paragraph footer = new Paragraph();
                footer.setAlignment(Element.ALIGN_CENTER);
                footer.setSpacingBefore(20);
                footer.add(new Chunk("Apresente este ticket na hora do embarque\n", smallFont));
                footer.add(new Chunk("VVV Viagens - Explore novos destinos", smallFont));
                document.add(footer);

                if (tickets.indexOf(ticket) < tickets.size() - 1) {
                    document.newPage();
                }
            }

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename("tickets-" + reserva.getId_reserva() + ".pdf")
                            .build());

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar tickets: " + e.getMessage());
        }
    }

    private void addTicketRow(PdfPTable table, String label, String value, Font headerFont, Font normalFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label + ":", headerFont));
        labelCell.setBorderColor(BaseColor.LIGHT_GRAY);
        labelCell.setBackgroundColor(new BaseColor(245, 245, 245));
        labelCell.setPadding(8);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, normalFont));
        valueCell.setBorderColor(BaseColor.LIGHT_GRAY);
        valueCell.setPadding(8);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private Reserva criarReservaPresencial(Viagem viagem, List<Passageiro> passageiros, Funcionario funcionario) {
        Reserva reserva = new Reserva();
        reserva.setViagem(viagem);
        reserva.setData(new Date(System.currentTimeMillis()));
        reserva.setStatus(StatusReserva.PENDENTE_PAGAMENTO);
        reserva.setValorTotal(calcularValorTotal(viagem, passageiros));
        reserva.setOrigem(viagem.getOrigem().getDescricaoCompleta());
        reserva.setDestino(viagem.getDestino().getDescricaoCompleta());
        reserva.setFuncionario(funcionario);
        return reserva;
    }

    private void associarPassageirosReserva(Reserva reserva, List<Passageiro> passageiros) {
        for (Passageiro passageiro : passageiros) {
            ReservaPassageiro reservaPassageiro = new ReservaPassageiro();
            reservaPassageiro.setId(new ReservaPassageiro.ReservaPassageiroId(
                    reserva.getId_reserva(),
                    passageiro.getId_passageiro()));
            reservaPassageiro.setReserva(reserva);
            reservaPassageiro.setPassageiro(passageiro);
            reservaService.salvarReservaPassageiro(reservaPassageiro);
        }
    }

    private BigDecimal calcularValorTotal(Viagem viagem, List<Passageiro> passageiros) {
        BigDecimal total = BigDecimal.ZERO;
        for (Passageiro passageiro : passageiros) {
            if (passageiro.getIdade() >= 2 && passageiro.getIdade() <= 10) {
                total = total.add(viagem.getValor().multiply(new BigDecimal("0.6")));
            } else {
                total = total.add(viagem.getValor());
            }
        }
        return total;
    }

}