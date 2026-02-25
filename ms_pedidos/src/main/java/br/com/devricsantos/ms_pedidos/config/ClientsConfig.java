package br.com.devricsantos.ms_pedidos.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "br.com.devricsantos.ms_pedidos.client")
public class ClientsConfig {


}
