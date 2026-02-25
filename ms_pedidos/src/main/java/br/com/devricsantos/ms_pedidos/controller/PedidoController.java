package br.com.devricsantos.ms_pedidos.controller;

import br.com.devricsantos.ms_pedidos.controller.dto.AdicaoNovoPagamentoDTO;
import br.com.devricsantos.ms_pedidos.controller.dto.NovoPedidoDTO;
import br.com.devricsantos.ms_pedidos.controller.mappers.PedidoMapper;
import br.com.devricsantos.ms_pedidos.model.ErroResposta;
import br.com.devricsantos.ms_pedidos.model.exception.ItemNaoEncontradoException;
import br.com.devricsantos.ms_pedidos.model.exception.ValidationException;
import br.com.devricsantos.ms_pedidos.publisher.DetalhePedidoMapper;
import br.com.devricsantos.ms_pedidos.publisher.representation.DetalheItemPedidoRepresentation;
import br.com.devricsantos.ms_pedidos.publisher.representation.DetalhePedidoRepresentation;
import br.com.devricsantos.ms_pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;
    private final DetalhePedidoMapper detalhePedidoMapper;

    @PostMapping
    public ResponseEntity<Object> criarPedido(@RequestBody NovoPedidoDTO dto) {
        try {
            var pedido = pedidoMapper.map(dto);
            var novoPedido = pedidoService.criarPedido(pedido);
            return ResponseEntity.ok(novoPedido.getCodigo());
        } catch (ValidationException e) {
            var erro = new ErroResposta("Erro Validação:", e.getField(), e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @PostMapping("/pagamentos")
    public ResponseEntity<Object> adicionarNovoPagamento(
            @RequestBody AdicaoNovoPagamentoDTO dto) {
        try {
            pedidoService.adicionarNovoPagamento(dto.codigoPedido(), dto.dados(), dto.tipoPagamento());
            return ResponseEntity.noContent().build();
        } catch (ItemNaoEncontradoException e) {
            var erro = new ErroResposta("Item não encontrado", "codigoPedido", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<DetalhePedidoRepresentation> obterDetalhesPedido(
            @PathVariable Long codigo) {
        return pedidoService
                .carregarDadosCompletosPedido(codigo)
                .map(detalhePedidoMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
