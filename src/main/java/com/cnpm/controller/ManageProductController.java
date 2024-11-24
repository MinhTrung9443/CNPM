package com.cnpm.controller;

import java.io.InputStream;
import java.nio.file.*;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.cnpm.entity.Product;
import com.cnpm.payload.response.ProductResponse;
import com.cnpm.service.IProductService;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;

@Controller
@MultipartConfig
@RequestMapping("/employee/products")
public class ManageProductController {

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
	public String createProduct(@Valid @ModelAttribute ProductDTO productdto, BindingResult result, Model model) {

		if (productdto.getImage().isEmpty()) {
			result.addError(new FieldError("productdto", "image", "The image is required"));
		}

		if (result.hasErrors()) {
			model.addAttribute("productdto", productdto);
			System.out.println("ProductDTO initialized: " + productdto); // Debug
			System.out.println("BindingResult Errors: " + result.getAllErrors()); // debug

			return "employee/createProduct";
		}

		

		

		for (long i = 0; i < productdto.getStock(); i++) {
			
			// save image file
			MultipartFile image = productdto.getImage();
			String randString = RandomStringUtils.randomAlphanumeric(10);
			String storageFileName = randString+ "_" + image.getOriginalFilename();

			try {
				String uploadDir = "src/main/resources/static/assets/img/product/";
				Path uploadPath = Paths.get(uploadDir);

				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				try (InputStream inputStream = image.getInputStream()) {
					Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (Exception ex) {
				System.out.println("Exception: " + ex.getMessage());
			}
			
			
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
			productdto.setVolume(product.getHow_to_use());
			productdto.setOrigin(product.getOrigin());
			model.addAttribute("productdto", productdto);
			System.out.println("Exception: " + product.getManufactureDate());

		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/employee/products";
		}

		return "employee/editProduct";
	}

	@PostMapping("/edit")
	public String updateProduct(Model model, @RequestParam long id, @Valid @ModelAttribute ProductDTO productdto,
			BindingResult result) {
		try {
			Product product1 = productServ.findById(id).get();
			String productCode = product1.getProductCode();
			List<Product> productsFiltered = productServ.findAllProductsByProductCode(productCode);
			if (result.hasErrors()) {
				return "employee/editProduct";
			}
			
			for (Product product : productsFiltered) {
				if (!productdto.getImage().isEmpty()) {
					// delete old image
					String uploadDir = "src/main/resources/static/assets/img/product/";
					Path oldImagePath = Paths.get(uploadDir + product.getImage());

					try {
						Files.delete(oldImagePath);
					} catch (Exception ex) {
						System.out.println("Exception: " + ex.getMessage());
					}
					// save new image
					MultipartFile image = productdto.getImage();
					String randString = RandomStringUtils.randomAlphanumeric(10);
					String storageFileName = randString+ "_" + image.getOriginalFilename();

					try (InputStream inputStream = image.getInputStream()) {
						Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
								StandardCopyOption.REPLACE_EXISTING);
					}
					product.setImage(storageFileName);
				}
				//insert vao model
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
	public String deleteProduct(@RequestParam long id)
	{
		try {
			Product product1 = productServ.findById(id).get();
			String productCode = product1.getProductCode();
			List<Product> productsFiltered = productServ.findAllProductsByProductCode(productCode);
			
			for (Product product : productsFiltered) {
				
				if(product.getIsUsed()==0)
				{
					//delete product image
					Path imagePath = Paths.get("src/main/resources/static/assets/img/product/" + product.getImage());
					try {
						Files.delete(imagePath);
					}catch(Exception ex)
					{
						System.out.println("Exception: "+ex.getMessage());
					}
					productServ.delete(product);
					
				}
				
			}
			
		}catch(Exception ex)
		{
			System.out.println("Exception: "+ex.getMessage());
		}
		return "redirect:/employee/products";
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
	
}
