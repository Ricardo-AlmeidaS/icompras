package br.com.devricsantos.ms_clientes.controller;

import br.com.devricsantos.ms_clientes.model.Cliente;
import br.com.devricsantos.ms_clientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente) {
        clienteService.salvar(cliente);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long codigo) {
        return clienteService.obterCodigoCliente(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{codigo}")
    private ResponseEntity<Void> deletar(@PathVariable("codigo") Long codigo) {
        var cliente = clienteService.obterCodigoCliente(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente inexistente"
                ));

        clienteService.deletar(cliente);
        return ResponseEntity.noContent().build();
    }
}
