package com.cnpm.test;

import com.cnpm.entity.*;
import com.cnpm.repository.CustomerRepository;
import com.cnpm.repository.ProductRepository;
import com.cnpm.repository.RoleRepository;
import com.cnpm.repository.UserRepository;
import com.google.gson.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class initdata {
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;


    @PostConstruct
    public void init() {
//        nhớ thêm vào bảng role bằng tay
//        1	SHOP_OWNER
//        2	EMPLOYEE
//        3	CUSTOMER

//        chỉ chạy 1 lần bên dưới, CHẠY XONG CMT LẠI
//        User user = new Customer();
//        Account account = new Account();
//        account.setUsername("customer");
//        account.setPassword(new BCryptPasswordEncoder().encode("123"));
////        lúc đăng nhập thì nhập 123 vào password
//        account.setRole(roleRepository.findByRoleName("CUSTOMER"));
//        account.setUser(user);
//        user.setAccount(account);
//        userRepository.save( user);
//
//        User user2 = new Employee();
//        Account account2 = new Account();
//        account2.setUsername("employee");
//        account2.setPassword(new BCryptPasswordEncoder().encode("123"));
//        account2.setRole(roleRepository.findByRoleName("EMPLOYEE"));
//        account2.setUser(user2);
//        user2.setAccount(account2);
//        userRepository.save( user2);
//
//        User user3 = new ShopOwner();
//        Account account3 = new Account();
//        account3.setUsername("shopowner");
//        account3.setPassword(new BCryptPasswordEncoder().encode("123"));
//        account3.setRole(roleRepository.findByRoleName("SHOP_OWNER"));
//        account3.setUser(user3);
//        user3.setAccount(account3);
//        userRepository.save(user3);






        try {
            // Đọc JSON từ file
//            JsonElement root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\BodyVietnamese.json"), StandardCharsets.UTF_8));
//            parseJson(root);
//            root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\WellnessVietnamese.json"), StandardCharsets.UTF_8));
//            parseJson(root);
//            root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\SkinVietnamese.json"), StandardCharsets.UTF_8));
//            parseJson(root);
//            root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\MakeUpVietnamese.json"), StandardCharsets.UTF_8));
//            parseJson(root);
//            root = JsonParser.parseReader(new InputStreamReader(new FileInputStream("D:\\HairVietnamese.json"), StandardCharsets.UTF_8));
//            parseJson(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseJson(JsonElement element) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Product> products = new ArrayList<>();
        JsonArray array = element.getAsJsonArray();

        for (JsonElement arrayElement : array) {
            JsonObject product = arrayElement.getAsJsonObject();
            Product product1 = new Product();
            product1.setProductCode(product.get("productCode").getAsString());
            product1.setProductName(product.get("name").getAsString());
            product1.setCategory(product.get("category").getAsString());
            product1.setCost(product.get("cost").getAsDouble());
            product1.setDescription(product.get("description").getAsString());
            product1.setBrand(product.get("brand").getAsString());
            product1.setIngredient(product.get("ingredient").getAsString());
            product1.setHow_to_use(product.get("how_to_use").getAsString());
            product1.setVolume(product.get("volume").getAsString());
            product1.setOrigin(product.get("origin").getAsString());
            product1.setImage(product.get("image").getAsString());
            product1.setManufactureDate(LocalDate.parse(product.get("manufactureDate").getAsString(), formatter));
            product1.setExpirationDate(LocalDate.parse(product.get("expirationDate").getAsString(), formatter));
            products.add(product1);
        }
        List<Product> lst = new ArrayList<>();

        products.forEach(product -> {
           for (int i = 0; i < 30; i++) {
               Product product1 = new Product();
               product1=product;
               lst.add(product1);

           }
        });
        // Lưu tất cả sản phẩm vào cơ sở dữ liệu
        productRepository.saveAll(lst);


    }
}
