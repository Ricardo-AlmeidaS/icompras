package br.com.devricsantos.ms_produtos.controller;

import br.com.devricsantos.ms_produtos.model.Produto;
import br.com.devricsantos.ms_produtos.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody Produto produto) {
        produtoService.salvar(produto);
        return ResponseEntity.ok().body(produto);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Produto> obterCodigoProduto(@PathVariable("codigo") Long codigo) {
       return produtoService
               .obterCodigoProduto(codigo)
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigo}")
    private ResponseEntity<Void> deletar(@PathVariable("codigo") Long codigo) {
        var produto = produtoService.obterCodigoProduto(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto inexistente"
                ));

        produtoService.deletar(produto);
        return ResponseEntity.noContent().build();
    }
}
