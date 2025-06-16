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
    public String listProducts(HttpSession session,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(required = false) String brand,
                               @RequestParam(required = false) Double minPrice,
                               @RequestParam(required = false) Double maxPrice,
                               @RequestParam(required = false) Integer minStock,
                               @RequestParam(required = false) Integer maxStock,
                               Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }

        List<Product> products = productService.findByFilter(brand, minPrice, maxPrice, minStock, maxStock, page, size);
        long totalItems = productService.countByFilter(brand, minPrice, maxPrice, minStock, maxStock);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("page", "product");

        model.addAttribute("brand", brand);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minStock", minStock);
        model.addAttribute("maxStock", maxStock);

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

        if (productService.existsByName(productDTO.getName())) {
            result.rejectValue("name", "error.name", "Tên sản phẩm đã tồn tại");
            prepareProductList(model, page, size);
            model.addAttribute("page", "product");
            model.addAttribute("showModal", true);
            return "layout";
        }

        if (productDTO.getImageFile() == null || productDTO.getImageFile().isEmpty()) {
            result.rejectValue("imageFile", "error.imageFile", "Ảnh đại diện không được để trống");
            prepareProductList(model, page, size);
            model.addAttribute("page", "product");
            model.addAttribute("showModal", true);
            return "layout";
        }

        String imageUrl;
        try {
            Map uploadResult = cloudinary.uploader().upload(productDTO.getImageFile().getBytes(), ObjectUtils.emptyMap());
            imageUrl = uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            e.printStackTrace();
            result.rejectValue("imageFile", "error.imageFile", "Lỗi khi upload ảnh");
            prepareProductList(model, page, size);
            model.addAttribute("page", "product");
            model.addAttribute("showModal", true);
            return "layout";
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
        model.addAttribute("showEditModal", true);
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
            model.addAttribute("showEditModal", true);

            return "layout";
        }

        Product existingProduct = productService.find_by_id(productDTO.getId());
        if (existingProduct == null) {
            return "redirect:/products";
        }

        if (!productDTO.getName().equalsIgnoreCase(existingProduct.getName())
                && productService.existsByName(productDTO.getName())) {
            result.rejectValue("name", "error.name", "Tên sản phẩm đã tồn tại");
            prepareProductList(model, page, size);
            model.addAttribute("page", "product");
            model.addAttribute("showEditModal", true);
            return "layout";
        }

        String imageUrl = existingProduct.getImage();
        if (productDTO.getImageFile() != null && !productDTO.getImageFile().isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(productDTO.getImageFile().getBytes(), ObjectUtils.emptyMap());
                imageUrl = uploadResult.get("secure_url").toString();
            } catch (Exception e) {
                e.printStackTrace();
                result.rejectValue("imageFile", "error.imageFile", "Lỗi khi upload ảnh");
                prepareProductList(model, page, size);
                model.addAttribute("page", "product");
                model.addAttribute("showEditModal", true);
                return "layout";
            }
        }

        existingProduct.setName(productDTO.getName());
        existingProduct.setBrand(productDTO.getBrand());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStock(productDTO.getStock());
        existingProduct.setImage(imageUrl);

        productService.update(existingProduct);
        return "redirect:/products";
    }


   @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/login";
        }
        Product product = productService.find_by_id(id);
        if (product != null) {
            product.setStatus(!product.isStatus());
            productService.update(product);
        }
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