package api.loja.lotus.services.storage;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j 
@RequiredArgsConstructor 
@Service 
public class SupabaseStorageService {
    
    private final S3Client s3Client;

    @Value("${supabase.s3.bucket}")
    private String bucket;

    @Value("${supabase.storage.public-url}")
    private String publicUrl;

    public String upload(MultipartFile arquivo) {

        try {
            
            String imagemUrl = UUID.randomUUID() + "-" + arquivo.getOriginalFilename();

            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(imagemUrl)
                .contentType(arquivo.getContentType())
            .build();

            s3Client.putObject(
                request,
                RequestBody.fromBytes(arquivo.getBytes())
            );

            return publicUrl + "/" + bucket + "/" + imagemUrl;

        } catch (IOException ex) {

            throw new RuntimeException("Erro ao enviar imagem para storage!");

        } catch (RuntimeException ex) {

            log.error("Erro ao enviar arquivo para o S3/Supabase", ex);
            throw ex;
        }

    }

    public void delete(String imagemUrl) {

        String nomeArquivo = imagemUrl.substring(
            imagemUrl.lastIndexOf("/") + 1
        );

        DeleteObjectRequest request = DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(nomeArquivo)
        .build();

        s3Client.deleteObject(request);
    }

}
