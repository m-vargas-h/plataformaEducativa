package com.duoc.PlataformaEducativa.service;

import com.duoc.PlataformaEducativa.model.Asset;

public interface AwsService {

    Asset upload(Asset asset);
    Asset update(Asset asset);
    Asset download(String folder, String fileName);
    Asset move(String folder, String oldFileName, String newFileName);
    void delete(String folder, String fileName);
}