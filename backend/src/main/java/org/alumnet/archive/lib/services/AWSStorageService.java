package org.alumnet.archive.lib.services;

import lombok.Builder;
import org.springframework.stereotype.Service;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.alumnet.archive.lib.services.interfaces.IStorageService;

import java.io.IOException;

@Builder
@Service("awsStorageService")
public class AWSStorageService implements IStorageService {

    private final S3Template s3Template;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, String key) {
        try {
            s3Template.upload(bucketName, key, file.getInputStream());
            return "https://" + bucketName + ".s3.amazonaws.com/" + key;
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo a S3", e);
        }
    }

    @Override
    public byte[] downloadFile(String key) {
        try {
            return s3Template.download(bucketName, key).getContentAsByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar el archivo de S3", e);
        }
    }
}