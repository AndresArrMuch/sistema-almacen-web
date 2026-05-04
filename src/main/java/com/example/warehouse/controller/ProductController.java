package com.example.warehouse.controller;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.Supplier;
import com.example.warehouse.model.Category;
import com.example.warehouse.service.WarehouseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final WarehouseService warehouseService;

    public ProductController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) Long categoryId,
                               @RequestParam(defaultValue = "0") int page) {
        
        // Convertimos el ID a String para que sea compatible con tu WarehouseService actual
        String catParam = (categoryId != null) ? categoryId.toString() : null;
        
        Page<Product> products = warehouseService.findProducts(search, catParam, PageRequest.of(page, 7));
        
        model.addAttribute("products", products);
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("product", new Product());
        model.addAttribute("suppliers", warehouseService.listSuppliers());
        model.addAttribute("categories", warehouseService.listCategories());
        return "products";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, 
                               @RequestParam Long supplierId, 
                               @RequestParam Long categoryId, 
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        
        product.setSupplier(warehouseService.findSupplier(supplierId));
        product.setCategory(warehouseService.findCategory(categoryId));
        
        product.setCostPrice(product.getCostPrice() == null ? BigDecimal.ZERO : product.getCostPrice());
        product.setSalePrice(product.getSalePrice() == null ? BigDecimal.ZERO : product.getSalePrice());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Files.write(uploadPath.resolve(fileName), imageFile.getBytes());
                product.setImageUrl("/images/" + fileName);
            } catch (IOException e) { e.printStackTrace(); }
        }

        warehouseService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
public String deleteProduct(@PathVariable("id") Long id) { // Asegúrate de incluir ("id")
    try {
        warehouseService.deleteProduct(id);
    } catch (Exception e) {
        // Esto evitará que la aplicación estalle (Error 500) y te mostrará el error en consola
        System.out.println("Error al eliminar: " + e.getMessage());
        return "redirect:/products?error=foreignkey"; 
    }
    return "redirect:/products";
}

    // --- GESTIÓN DE CATEGORÍAS ---
    @PostMapping("/categories/save")
    public String saveCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        warehouseService.saveCategory(category);
        return "redirect:/products";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        warehouseService.deleteCategory(id);
        return "redirect:/products";
    }

    // --- GESTIÓN DE PROVEEDORES ---
    @PostMapping("/suppliers/save")
    public String saveSupplier(@RequestParam String name) {
        Supplier supplier = new Supplier();
        supplier.setName(name);
        warehouseService.saveSupplier(supplier);
        return "redirect:/products";
    }

    @GetMapping("/suppliers/delete/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        warehouseService.deleteSupplier(id);
        return "redirect:/products";
    }
}