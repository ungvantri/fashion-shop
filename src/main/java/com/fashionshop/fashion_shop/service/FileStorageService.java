package com.fashionshop.fashion_shop.service;

import java.io.InputStream;

public interface FileStorageService {
    String generateUploadUrl(String fileName);

    InputStream getFileInputStream(String fileKey);
}
