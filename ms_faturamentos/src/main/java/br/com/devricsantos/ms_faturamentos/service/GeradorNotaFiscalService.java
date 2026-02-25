package br.com.devricsantos.ms_faturamentos.service;

import br.com.devricsantos.ms_faturamentos.bucket.BucketFile;
import br.com.devricsantos.ms_faturamentos.bucket.BucketService;
import br.com.devricsantos.ms_faturamentos.model.Pedido;
import br.com.devricsantos.ms_faturamentos.publisher.FaturamentoPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeradorNotaFiscalService {

    private final NotaFiscalService notaFiscalService;
    private final BucketService bucketService;
    private final FaturamentoPublisher faturamentoPublisher;

    public void gerar(Pedido pedido) {
        log.info("Gerando nota fical para o pedido {}", pedido.codigo());

        try {
            byte[] byteArray = notaFiscalService.gerarNota(pedido);

            String nomeArquivo = String.format("notafiscal_pedido_%d.pdf", pedido.codigo());

            var file = new BucketFile(
                    nomeArquivo, new ByteArrayInputStream(byteArray), MediaType.APPLICATION_PDF, byteArray.length);

            bucketService.upload(file);
            String url = bucketService.getUrl(nomeArquivo);
            faturamentoPublisher.publicar(pedido, url);
            log.info("Gerada a nota fiscal, nome do arquivo: {}", nomeArquivo);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }


}
