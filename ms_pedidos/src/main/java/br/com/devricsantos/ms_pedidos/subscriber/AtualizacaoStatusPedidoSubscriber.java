package br.com.devricsantos.ms_pedidos.subscriber;

import br.com.devricsantos.ms_pedidos.service.AtualizacaoStatusPedidoService;
import br.com.devricsantos.ms_pedidos.subscriber.representation.AtualizacaoStatusPedidoRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AtualizacaoStatusPedidoSubscriber {

    private final AtualizacaoStatusPedidoService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = {
            "${icompras.config.kafka.topics.pedidos-faturados}",
            "${icompras.config.kafka.topics.pedidos-enviados}"
    })
    public void receberAtualizacaO(String json) {
        log.info("Recebendo atualizacao de status: {}", json);

        try {
            var atualizacaoStatus =
                    objectMapper.readValue(json, AtualizacaoStatusPedidoRepresentation.class);

            service.atualizarStatus(
                    atualizacaoStatus.codigo(),
                    atualizacaoStatus.status(),
                    atualizacaoStatus.urlNotaFiscal(),
                    atualizacaoStatus.codigoRastreio());

            log.info("Pedido atualizado com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao atulizar status do pedido: {}", e.getMessage());
        }
    }
}
