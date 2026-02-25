package br.com.devricsantos.ms_faturamento.api;

import br.com.devricsantos.ms_faturamento.bucket.BucketFile;
import br.com.devricsantos.ms_faturamento.bucket.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/bucket")
@RequiredArgsConstructor
public class BucketCntroller {

    private final BucketService bucketService;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try(InputStream is = file.getInputStream()) {

            MediaType type = MediaType.parseMediaType(file.getContentType());
            var bucketFile = new BucketFile(file.getOriginalFilename(), is, type, file.getSize());
            bucketService.upload(bucketFile);

            return ResponseEntity.ok("Arquivo enviado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar o arquivo: " + e);
        }
    }

    @GetMapping
    public ResponseEntity<String> getUrl(@RequestParam String filename){
        try {
            String url = bucketService.getUrl(filename);
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).body(url);
        } catch (Exception e){
            return ResponseEntity.status(500).body("Erro ao obter URL do arquivo: " + e.getMessage());
        }
    }
}
