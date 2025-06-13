package ra.edu.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.ProductDTO;
import ra.edu.entity.Product;
import ra.edu.service.product.ProductServiceImp;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceImp productService;
    @Autowired
    private Cloudinary cloudinary;

    @GetMapping
    public String listProducts(HttpSession session
            , @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(required = false) String brand,
                               Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Product> products;
        long totalItems;

        if (brand != null && !brand.trim().isEmpty()) {
            products = productService.find_by_brand(brand, page, size);
            totalItems = productService.count_by_brand(brand);
            model.addAttribute("brand", brand);
        } else {
            products = productService.find_all(page, size);
            totalItems = productService.count();
        }

        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("page", "product");

        return "layout";
    }


    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
                             BindingResult result,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "5") int size,
                             Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            prepareProductList(model, page, size);
            model.addAttribute("page", "product");
            model.addAttribute("showModal", true);
            return "layout";
        }

        if (productDTO.getImageFile() == null || productDTO.getImageFile().isEmpty()) {
            result.rejectValue("image", "error.image", "Ảnh đại diện không được để trống");
        }
        String imageUrl = null;
        try {
            if (productDTO.getImageFile() != null && !productDTO.getImageFile().isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(productDTO.getImageFile().getBytes(), ObjectUtils.emptyMap());
                imageUrl = uploadResult.get("secure_url").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.rejectValue("imageFile", "error.productDTO", "Lỗi khi upload ảnh");
            return "product";
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImage(imageUrl);

        productService.save(product);

        return "redirect:/products";
    }
@GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        Product product = productService.find_by_id(id);
        if (product == null) {
            return "redirect:/products";
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setBrand(product.getBrand());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setImage(product.getImage());

        model.addAttribute("productDTO", productDTO);
        model.addAttribute("page", "product");
        model.addAttribute("showModal", true);
        return "layout";
    }
    @PostMapping("/edit")
    public String updateProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
                                BindingResult result,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "5") int size,
                                Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            prepareProductList(model, page, size);
            model.addAttribute("page", "product");
            model.addAttribute("showModal", true);
            return "layout";
        }

        String imageUrl = null;
        try {
            if (productDTO.getImageFile() != null && !productDTO.getImageFile().isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(productDTO.getImageFile().getBytes(), ObjectUtils.emptyMap());
                imageUrl = uploadResult.get("secure_url").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.rejectValue("imageFile", "error.productDTO", "Lỗi khi upload ảnh");
            return "product";
        }

        Product product = productService.find_by_id(productDTO.getId());
        if (product == null) {
            return "redirect:/products";
        }

        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImage(imageUrl);

        productService.update(product);

        return "redirect:/products";
    }
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.delete(id);
        return "redirect:/products";
    }

    private void prepareProductList(Model model, int page, int size) {
        List<Product> products = productService.find_all(page, size);
        long totalItems = productService.count();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
    }
}