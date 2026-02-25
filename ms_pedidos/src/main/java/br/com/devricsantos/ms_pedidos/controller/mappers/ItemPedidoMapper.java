package br.com.devricsantos.ms_pedidos.controller.mappers;

import br.com.devricsantos.ms_pedidos.controller.dto.ItemPedidoDTO;
import br.com.devricsantos.ms_pedidos.model.ItemPedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    ItemPedido map(ItemPedidoDTO dto);
}
