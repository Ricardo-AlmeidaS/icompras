package br.com.devricsantos.ms_faturamento.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoPagoSubscriber {

    private final ObjectMapper objectMapper;
}
