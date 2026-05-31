package com.duoc.PlataformaEducativa.repository;

import com.duoc.PlataformaEducativa.model.Asset;

public interface S3Repository {

    Asset upload(Asset asset, String bucketName);
    Asset download(String folder, String fileName, String bucketName);
    Asset move(String folder, String oldFileName, String newFileName, String bucketName);
    void delete(String folder, String fileName, String bucketName);
}