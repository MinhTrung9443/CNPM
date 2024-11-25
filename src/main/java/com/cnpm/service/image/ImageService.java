package com.cnpm.service.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {
    // Đường dẫn thư mục lưu ảnh, bạn có thể cấu hình đường dẫn này trong file application.properties
    @Value("${image.upload-dir}")
    private String uploadDir;

    public String saveImage(MultipartFile imageFile) throws IOException {
        // Kiểm tra thư mục đã tồn tại chưa, nếu chưa thì tạo mới
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Tạo tên file duy nhất để tránh trùng lặp
        String fileExtension = getFileExtension(Objects.requireNonNull(imageFile.getOriginalFilename()));
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

        // Lưu file vào thư mục
        Path path = Paths.get(uploadDir + File.separator + uniqueFileName);
        Files.write(path, imageFile.getBytes());

        // Trả về đường dẫn tới file vừa lưu (có thể là URL hoặc tên file)
        return uniqueFileName;  // Hoặc URL, tùy vào cách bạn cấu hình truy cập ảnh
    }

    // Hàm lấy đuôi file (ví dụ: jpg, png, ...)
    private String getFileExtension(String filename) {
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i + 1);
        }
        return extension;
    }
}
