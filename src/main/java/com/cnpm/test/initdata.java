package com.cnpm.test;

import com.cnpm.entity.Product;
import com.cnpm.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

public class initdata {

    ProductRepository productRepository;

    initdata(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public static void main(String[] args) {
        try {
            // Đọc dữ liệu từ file JSON
            List<Product> products = readJsonFile("src/main/resources/BodyVietnamese.json");
            // Nhập dữ liệu vào SQL Server
            for (Product product : products) {
                System.out.println(product);
            }
            System.out.println("Data imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Product> readJsonFile(String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), new TypeReference<List<Product>>() {});
    }
}
