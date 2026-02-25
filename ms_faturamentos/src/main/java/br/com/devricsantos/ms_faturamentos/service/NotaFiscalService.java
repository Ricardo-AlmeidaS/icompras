package br.com.devricsantos.ms_faturamentos.service;

import br.com.devricsantos.ms_faturamentos.model.Pedido;
import br.com.devricsantos.ms_faturamentos.subscriber.representation.DetalhePedidoRepresentation;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotaFiscalService {

    @Value("classpath:reports/nota_fiscal.jrxml")
    private Resource notaFsical;

    @Value("classpath:reports/logo.png")
    private Resource logo;

    public byte[] gerarNota(Pedido pedido) {
        try (InputStream inputStream = notaFsical.getInputStream()) {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("NOME", pedido.cliente().nome());
            parameters.put("CPF", pedido.cliente().cpf());
            parameters.put("LOGRADOURO", pedido.cliente().logradouro());
            parameters.put("NUMERO", pedido.cliente().numero());
            parameters.put("BAIRRO", pedido.cliente().bairro());
            parameters.put("EMAIL", pedido.cliente().email());
            parameters.put("TELEFONE", pedido.cliente().telefone());

            var dataSource = new JRBeanCollectionDataSource(pedido.itens());

            parameters.put("DATA_PEDIDO", pedido.data());
            parameters.put("TOTAL_PEDIDO", pedido.total());

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
