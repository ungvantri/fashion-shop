package com.fashionshop.fashion_shop.service;

import java.io.InputStream;

public interface FileStorageService {
    // 1. Sinh link tải lên tạm thời
    String generateUploadUrl(String fileName);

    // 2. Kéo luồng InputStream để đọc file
    InputStream getFileInputStream(String fileKey);
}
