package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.ProductDTO;
import ra.edu.entity.Product;
import ra.edu.service.product.ProductServiceImp;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceImp productService;

    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model) {
        List<Product> products = productService.findAll(page, size);
        long totalItems = productService.count();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("page", "product");
        return "layout";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("page", "addProduct");
        return "layout";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
                             BindingResult result,
                             Model model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "5") int size) {
        if (result.hasErrors()) {
            List<Product> products = productService.findAll(page, size);
            long totalItems = productService.count();
            int totalPages = (int) Math.ceil((double) totalItems / size);

            model.addAttribute("products", products);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("page", "addProduct");
            model.addAttribute("showModal", true);
            return "layout";
        }
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") int id, Model model) {
        Product product = productService.findById(id);
        if (product == null) {
            return "redirect:/products";
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setBrand(product.getBrand());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("page", "editProduct");
        return "layout";
    }

    @PostMapping("/edit")
    public String editProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("page", "editProduct");
            return "layout";
        }
        Product product = productService.findById(productDTO.getId());
        if (product == null) {
            return "redirect:/products";
        }
        product.setName(productDTO.getName());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        productService.update(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.delete(id);
        return "redirect:/products";
    }
}