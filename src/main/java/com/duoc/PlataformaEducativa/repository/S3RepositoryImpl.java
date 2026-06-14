package com.duoc.PlataformaEducativa.repository;

import com.duoc.PlataformaEducativa.model.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Repository
@RequiredArgsConstructor
public class S3RepositoryImpl implements S3Repository {

    private final S3Client s3Client;

    @Override
    public Asset upload(Asset asset, String bucketName) {
        String key = asset.getFolder() + "/" + asset.getFileName();

        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(asset.getContentType())
                .build(),
            RequestBody.fromBytes(asset.getContent())
        );

        return asset;
    }

    // Reemplaza el contenido de un archivo existente en S3 con la misma key
    @Override
    public Asset update(Asset asset, String bucketName) {
        String key = asset.getFolder() + "/" + asset.getFileName();

        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(asset.getContentType())
                .build(),
            RequestBody.fromBytes(asset.getContent())
        );

        return asset;
    }

    @Override
    public Asset download(String folder, String fileName, String bucketName) {
        String key = folder + "/" + fileName;

        byte[] content = s3Client.getObject(
            GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build(),
            ResponseTransformer.toBytes()
        ).asByteArray();

        Asset asset = new Asset();
        asset.setFolder(folder);
        asset.setFileName(fileName);
        asset.setContent(content);
        return asset;
    }

    @Override
    public Asset move(String folder, String oldFileName, String newFileName, String bucketName) {
        String oldKey = folder + "/" + oldFileName;
        String newKey = folder + "/" + newFileName;

        s3Client.copyObject(
            CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(oldKey)
                .destinationBucket(bucketName)
                .destinationKey(newKey)
                .build()
        );

        s3Client.deleteObject(
            DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(oldKey)
                .build()
        );

        Asset asset = new Asset();
        asset.setFolder(folder);
        asset.setFileName(newFileName);
        return asset;
    }

    @Override
    public void delete(String folder, String fileName, String bucketName) {
        String key = folder + "/" + fileName;

        s3Client.deleteObject(
            DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build()
        );
    }
}