
package br.com.devricsantos.ms_logistica.service;

import br.com.devricsantos.ms_logistica.model.AtualizacaoEnvioPedido;
import br.com.devricsantos.ms_logistica.model.StatusPedido;
import br.com.devricsantos.ms_logistica.publisher.EnvioPedidoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EnvioPedidoService {

    private final EnvioPedidoPublisher publisher;

    public void enviar(Long codigoPedido, String urlNotaFiscal) {
        var codigoRastreio = gerarCodigoRastreio();
        var atualizacaoRepresentatation =
                new AtualizacaoEnvioPedido(codigoPedido, StatusPedido.ENVIADO, codigoRastreio);
        publisher.enviar(atualizacaoRepresentatation);
    }

    private String gerarCodigoRastreio() {
        // AB1234565789BR
        var random = new Random();

        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));

        int numeros = 100000000 + random.nextInt(900000000);

        return "" + letra1 + letra2 + numeros + "BR";

    }
}
