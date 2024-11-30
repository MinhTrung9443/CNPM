package com.cnpm.controller.employee;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cnpm.dto.ProductDTO;
import com.cnpm.dto.ProductResponse;
import com.cnpm.entity.Product;
import com.cnpm.service.IProductService;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;

@Controller
@MultipartConfig
@RequestMapping("/employee/products")
public class ManageProductController {

	@Value("${app.upload.directory}")
    private String uploadDirectory;
	@Autowired
	private IProductService productServ;

	@GetMapping({ "", "/" })
	public String showProductList(Model model) {
		return findPaginated(1, model);
	}

	@GetMapping({ "/create" })
	public String showCreatePage(Model model) {
		ProductDTO productdto = new ProductDTO();
		model.addAttribute("productdto", productdto);
		System.out.println("ProductDTO initialized: " + productdto); // Debug
		return "employee/createProduct";
	}

	@PostMapping({ "/create" })
	public String createProduct(@Valid @ModelAttribute("productdto") ProductDTO productdto, BindingResult result,
			Model model) {

		if (productdto.getImage().isEmpty()) {
			result.addError(new FieldError("productdto", "image", "The image is required"));
		}

		if (result.hasErrors()) {
			// model.addAttribute("productdto", productdto);
			System.out.println("ProductDTO initialized: " + productdto); // Debug
			System.out.println("BindingResult Errors: " + result.getAllErrors()); // debug

			return "employee/createProduct";
		}

		// save image file
		MultipartFile image = productdto.getImage();
		String randString = RandomStringUtils.randomAlphanumeric(10);
		String storageFileName = randString + "_" + image.getOriginalFilename();

		try {
			Path uploadPath = Paths.get(uploadDirectory);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
				System.out.println("Directory created: " + uploadPath);
			}

			try (InputStream inputStream = image.getInputStream()) {
				Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}

		for (long i = 0; i < productdto.getStock(); i++) {

			Product product = new Product();
			product.setProductCode(productdto.getProductCode());
			product.setProductName(productdto.getProductName());
			product.setCategory(productdto.getCategory());
			product.setCost(productdto.getCost());
			product.setDescription(productdto.getDescription());
			product.setBrand(productdto.getBrand());
			product.setManufactureDate(productdto.getManufactureDate());
			product.setExpirationDate(productdto.getExpirationDate());
			product.setIngredient(productdto.getIngredient());
			product.setHow_to_use(productdto.getHow_to_use());
			product.setVolume(productdto.getVolume());
			product.setOrigin(productdto.getOrigin());
			product.setImage(storageFileName);

			productServ.save(product);
		}

		return "redirect:/employee/products";
	}

	@GetMapping("/edit")
	public String showEditPage(Model model, @RequestParam long id) {
		try {
			Product product = productServ.findById(id).get();
			model.addAttribute("product", product);
			ProductDTO productdto = new ProductDTO();

			productdto.setProductCode(product.getProductCode());
			productdto.setProductName(product.getProductName());
			productdto.setCategory(product.getCategory());
			productdto.setCost(product.getCost());
			productdto.setDescription(product.getDescription());
			productdto.setBrand(product.getBrand());
			productdto.setManufactureDate(product.getManufactureDate());
			productdto.setExpirationDate(product.getExpirationDate());
			productdto.setIngredient(product.getIngredient());
			productdto.setHow_to_use(product.getHow_to_use());
			productdto.setVolume(product.getVolume());
			productdto.setOrigin(product.getOrigin());
			model.addAttribute("productdto", productdto);
			System.out.println("ID: " + product.getProductId());
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/employee/products";
		}

		return "employee/editProduct";
	}

	@PostMapping("/edit")
	public String updateProduct(Model model, @RequestParam long id,
			@Valid @ModelAttribute("productdto") ProductDTO productdto, BindingResult result) {
		try {
			Product product1 = productServ.findById(id).get();
			String productCode = product1.getProductCode();
			List<Product> productsFiltered = productServ.findAllProductsByProductCode(productCode);
			if (result.hasErrors()) {
				System.out.println("ProductDTO initialized: " + productdto); // Debug
				System.out.println("BindingResult Errors: " + result.getAllErrors()); // debug
				return "employee/editProduct";
			}
			
			if (!productdto.getImage().isEmpty()) {
				// delete old image
				Path oldImagePath = Paths.get(uploadDirectory, product1.getImage());

				try {
					Files.delete(oldImagePath);
				} catch (Exception ex) {
					System.out.println("Exception: " + ex.getMessage());
				}
				// save new image
				MultipartFile image = productdto.getImage();
				String randString = RandomStringUtils.randomAlphanumeric(10);
				String storageFileName = randString + "_" + image.getOriginalFilename();

				try (InputStream inputStream = image.getInputStream()) {
					 Path newImagePath = Paths.get(uploadDirectory, storageFileName);
					Files.copy(inputStream,  newImagePath,
							StandardCopyOption.REPLACE_EXISTING);
				}
				for (Product product : productsFiltered) {
					product.setImage(storageFileName);
				}
				
			}

			for (Product product : productsFiltered) {
				
				// insert vao model
				product.setProductCode(productdto.getProductCode());
				product.setProductName(productdto.getProductName());
				product.setCategory(productdto.getCategory());
				product.setCost(productdto.getCost());
				product.setDescription(productdto.getDescription());
				product.setBrand(productdto.getBrand());
				product.setManufactureDate(productdto.getManufactureDate());
				product.setExpirationDate(productdto.getExpirationDate());
				product.setIngredient(productdto.getIngredient());
				product.setHow_to_use(productdto.getHow_to_use());
				product.setVolume(productdto.getVolume());
				product.setOrigin(productdto.getOrigin());

				productServ.save(product);
			}

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		return "redirect:/employee/products";
	}

	@GetMapping("/delete")
	public String deleteProduct(@RequestParam long id) {
		try {
			Product product1 = productServ.findById(id).get();
			String productCode = product1.getProductCode();
			List<Product> productsFiltered = productServ.findAllProductsByProductCode(productCode);
			int flag = 0;
			for (Product product : productsFiltered) {
				if (product.getIsUsed() == 1)
					flag = flag + 1;
			}

			for (Product product : productsFiltered) {
				String imagePath = product.getImage();

				if (product.getIsUsed() == 0) {
					if (flag == 0) {
						if (isValidURL(imagePath)) {
							productServ.delete(product);
							continue;
						} else {
							// delete product image
							Path oldImagePath = Paths.get(uploadDirectory, product.getImage());

							try {
								Files.delete(oldImagePath);
							} catch (Exception ex) {
								System.out.println("Exception: " + ex.getMessage());
							}
						}

					}

					productServ.delete(product);

				}

			}

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		return "redirect:/employee/products";
	}
	
	private boolean isValidURL(String urlString) {
		try {
			// Create and validate the URI
	        URI uri = new URI(urlString);
	        // Convert the URI to URL to ensure it can be used as a URL
	        uri.toURL();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value="pageNo") int pageNo, Model model)
	{
		// Số lượng sản phẩm trên mỗi trang
	    int pageSize = 10;
	    // Tạo Pageable từ số trang và số sản phẩm trên mỗi trang
	    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
	    
	    List<ProductResponse> productResponses = productServ.findDistinctProduct(pageable);
	    //gui du liueu
	    model.addAttribute("currentPage", pageNo);
	    model.addAttribute("products", productResponses);
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("totalItems", productServ.count());
	    model.addAttribute("totalPages", (int) Math.ceil((double) productServ.count() / pageSize));
	    return "employee/manageProduct";
	    
	}
	
	 // Phương thức để tìm kiếm sản phẩm
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model,  @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
    	
    	// Số lượng sản phẩm trên mỗi trang
	    int pageSize = 10;
	    // Tạo Pageable từ số trang và số sản phẩm trên mỗi trang
	    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
    	
    	List<ProductResponse> productResponses = productServ.searchProductsWithName(query, pageable);
    	
    	 //gui du liueu
	    model.addAttribute("currentPage", pageNo);
	    model.addAttribute("products", productResponses);
	    model.addAttribute("pageSize", pageSize);
    	model.addAttribute("totalItems",productServ.countDistinctProducts(query));
	    model.addAttribute("totalPages", (int) Math.ceil((double) productServ.count() / pageSize));
        return "employee/manageProduct";  // trả về cùng một view để hiển thị kết quả tìm kiếm
    }
}
