package group.vvv.controllers;

import group.vvv.config.UserSession;
import group.vvv.models.Reserva;
import group.vvv.models.Ticket;
import group.vvv.models.Reserva.StatusReserva;
import group.vvv.services.ReservaService;
import group.vvv.services.TicketService;

import java.io.ByteArrayOutputStream;
import java.util.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/minhas-reservas")
public class ConsultarReservasController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String exibirReservas(Model model) {
        if (!userSession.isAuthenticated()) {
            return "redirect:/web/clientes/login";
        }

        model.addAttribute("reservas",
                reservaService.getReservasByCliente(userSession.getCliente()));
        return "cliente/minhasReservas";
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id) {
        if (!userSession.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        try {
            Reserva reserva = reservaService.getReservaById(id);
            if (reserva == null) {
                throw new RuntimeException("Reserva não encontrada");
            }

            if (!reserva.getCliente().getId_cliente().equals(userSession.getCliente().getId_cliente())) {
                throw new RuntimeException("Acesso não autorizado a esta reserva");
            }

            if (reserva.getStatus() != StatusReserva.CONFIRMADA) {
                throw new RuntimeException("Reserva não está confirmada");
            }

            List<Ticket> tickets = ticketService.getTicketsByReserva(id);
            if (tickets.isEmpty()) {
                throw new RuntimeException("Não há tickets disponíveis para esta reserva");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 90, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);

            try {
                for (Ticket ticket : tickets) {
                    addTicketContent(document, ticket, titleFont, headerFont, normalFont);

                    if (tickets.indexOf(ticket) < tickets.size() - 1) {
                        document.newPage();
                    }
                }
            } finally {
                document.close();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename("tickets-" + reserva.getId_reserva() + ".pdf")
                            .build());

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar tickets: " + e.getMessage());
        }
    }

    private String calcularTempoViagem(LocalDateTime partida, LocalDateTime chegada) {
        Duration duracao = Duration.between(partida, chegada);
        long horas = duracao.toHours();
        long minutos = duracao.toMinutesPart();

        if (horas > 0) {
            return String.format("%dh%02dm", horas, minutos);
        } else {
            return String.format("%dm", minutos);
        }
    }

    private void addTicketContent(Document document, Ticket ticket,
            Font titleFont, Font headerFont, Font normalFont) throws DocumentException {

        Paragraph header = new Paragraph();
        header.setAlignment(Element.ALIGN_CENTER);
        header.add(new Chunk("VVV VIAGENS\n", titleFont));
        header.add(new Chunk("Sua Viagem, Nosso Compromisso",
                new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC)));
        header.setSpacingAfter(20);
        document.add(header);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        PdfPCell localizadorCell = new PdfPCell(
                new Phrase("LOCALIZADOR: " + ticket.getLocalizador(), headerFont));
        localizadorCell.setColspan(2);
        localizadorCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        localizadorCell.setPadding(8);
        localizadorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(localizadorCell);

        addTableRow(table, "Passageiro:", ticket.getPassageiro().getNome(), headerFont, normalFont);
        addTableRow(table, "Tipo de Passagem:", ticket.getTipoPassagem(), headerFont, normalFont);
        addTableRow(table, "Origem:", ticket.getOrigem(), headerFont, normalFont);
        addTableRow(table, "Destino:", ticket.getDestino(), headerFont, normalFont);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        addTableRow(table, "Data Partida:",
                ticket.getHoraPartida().format(dateFormatter), headerFont, normalFont);

        addTableRow(table, "Horário Partida:",
                ticket.getHoraPartida().format(timeFormatter),
                headerFont, normalFont);

        addTableRow(table, "Horário Chegada:",
                ticket.getHoraChegada().format(timeFormatter),
                headerFont, normalFont);

        String tempoViagem = calcularTempoViagem(ticket.getHoraPartida(), ticket.getHoraChegada());
        addTableRow(table, "Tempo de Viagem:", tempoViagem, headerFont, normalFont);

        document.add(table);

        BarcodeQRCode qrCode = new BarcodeQRCode(ticket.getLocalizador(), 100, 100, null);
        Image qrCodeImage = qrCode.getImage();
        qrCodeImage.setAlignment(Element.ALIGN_CENTER);
        document.add(qrCodeImage);

        Paragraph footer = new Paragraph();
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.add(new Chunk("\n\nApresente este ticket na hora do embarque",
                new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC)));
        document.add(footer);
    }

    private void addTableRow(PdfPTable table, String label, String value, Font headerFont, Font normalFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, headerFont));
        labelCell.setBorderColor(BaseColor.LIGHT_GRAY);
        labelCell.setPadding(5);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, normalFont));
        valueCell.setBorderColor(BaseColor.LIGHT_GRAY);
        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}