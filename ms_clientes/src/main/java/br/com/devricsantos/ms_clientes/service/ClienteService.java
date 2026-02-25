package br.com.devricsantos.ms_clientes.service;

import br.com.devricsantos.ms_clientes.model.Cliente;
import br.com.devricsantos.ms_clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente salvar(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> obterCodigoCliente(Long id){
        return clienteRepository.findById(id);
    }

    public void deletar(Cliente cliente) {
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
}
