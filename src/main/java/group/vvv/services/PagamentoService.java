package group.vvv.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import group.vvv.models.Cartao;
import group.vvv.models.Pagamento;
import group.vvv.models.Pagamento.StatusPagamento;
import group.vvv.models.Parcela;
import group.vvv.models.Reserva;
import group.vvv.repositories.PagamentoRepository;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ParcelaService parcelaService;

    public Pagamento criarPagamento(Reserva reserva, Cartao cartao, int numParcelas) {
        Pagamento pagamento = new Pagamento();
        pagamento.setReserva(reserva);
        pagamento.setCartao(cartao);
        pagamento.setNumParcelas(numParcelas);
        pagamento.setStatusPagamento(StatusPagamento.AUTORIZADO);

        // Calcula valor total com juros se necessário
        BigDecimal valorTotal = reserva.getValorTotal();
        if (numParcelas > 4) {
            // Aplica 5% de juros e atualiza o valor total da reserva
            valorTotal = valorTotal.multiply(new BigDecimal("1.05"));
            reserva.setValorTotal(valorTotal); // Atualiza o valor total da reserva
        }

        pagamento = pagamentoRepository.save(pagamento);

        // Cria as parcelas
        List<Parcela> parcelas = new ArrayList<>();
        BigDecimal valorParcela = valorTotal.divide(new BigDecimal(numParcelas), 2, RoundingMode.HALF_UP);

        for (int i = 1; i <= numParcelas; i++) {
            Parcela parcela = new Parcela();
            parcela.setPagamento(pagamento);
            parcela.setNumeroParcela(i);
            parcela.setValorParcela(valorParcela);
            parcelas.add(parcelaService.salvarParcela(parcela));
        }

        pagamento.setParcelas(parcelas);
        return pagamento;
    }
}