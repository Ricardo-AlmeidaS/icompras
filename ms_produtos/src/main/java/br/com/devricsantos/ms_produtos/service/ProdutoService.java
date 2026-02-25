package br.com.devricsantos.ms_produtos.service;

import br.com.devricsantos.ms_produtos.model.Produto;
import br.com.devricsantos.ms_produtos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Optional<Produto> obterCodigoProduto(Long codigo) {
        return produtoRepository.findById(codigo);
    }

    public void deletar(Produto produto) {
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }
}
