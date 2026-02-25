package br.com.devricsantos.ms_pedidos.service;

import br.com.devricsantos.ms_pedidos.model.enums.StatusPedido;
import br.com.devricsantos.ms_pedidos.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizacaoStatusPedidoService {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public void atualizarStatus(
            Long codigo, StatusPedido status, String urlNotaFiscal, String codigoRastreio) {

        pedidoRepository.findById(codigo).ifPresent(pedido -> {
            pedido.setStatus(status);

            if (urlNotaFiscal != null) {
                pedido.setUrlNotaFiscal(urlNotaFiscal);
            }

            if (codigoRastreio != null) {
                pedido.setCodigoRastreio(codigoRastreio);
            }

        });


    }

}
