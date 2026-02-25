package br.com.devricsantos.ms_pedidos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class MsPedidosApplication {

//    @Bean
//    public CommandLineRunner commandLineRunner(KafkaTemplate<String, String> template) {
//        return args -> template.send("icompras.pedidos-pagos", "dados", "{ Homi Aranha }");
//    }

	public static void main(String[] args) {
		SpringApplication.run(MsPedidosApplication.class, args);
	}

}
