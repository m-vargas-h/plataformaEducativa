package com.duoc.PlataformaEducativa.service;

import com.duoc.PlataformaEducativa.model.Asset;
import com.duoc.PlataformaEducativa.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwsServiceImpl implements AwsService {

    private final S3Repository s3Repository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public Asset upload(Asset asset) {
        return s3Repository.upload(asset, bucketName);
    }

    @Override
    public Asset update(Asset asset) {
        return s3Repository.update(asset, bucketName);
    }

    @Override
    public Asset download(String folder, String fileName) {
        return s3Repository.download(folder, fileName, bucketName);
    }

    @Override
    public Asset move(String folder, String oldFileName, String newFileName) {
        return s3Repository.move(folder, oldFileName, newFileName, bucketName);
    }

    @Override
    public void delete(String folder, String fileName) {
        s3Repository.delete(folder, fileName, bucketName);
    }
}