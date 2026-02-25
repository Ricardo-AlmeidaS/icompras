package br.com.devricsantos.ms_faturamentos.subscriber;

import br.com.devricsantos.ms_faturamentos.service.GeradorNotaFiscalService;
import br.com.devricsantos.ms_faturamentos.mapper.PedidoMapper;
import br.com.devricsantos.ms_faturamentos.model.Pedido;
import br.com.devricsantos.ms_faturamentos.subscriber.representation.DetalhePedidoRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoPagoSubscriber {

    private final ObjectMapper objectMapper;
    private final GeradorNotaFiscalService geradorNotaFiscalService;
    private final PedidoMapper pedidoMapper;

    @KafkaListener(groupId = "icompras-faturamento",
            topics = "${icompras.config.kafka.topics.pedidos-pagos}")
    public void listen(String json) {
        try {
            log.info("Recebendo pedido de faturamento: {}", json);
            var representation = objectMapper.readValue(json, DetalhePedidoRepresentation.class);
            Pedido pedido = pedidoMapper.map(representation);
            geradorNotaFiscalService.gerar(pedido);
        } catch (Exception e) {
            log.error("Erro na consumação to topico de pedidos pagos");
        }
    }
}
