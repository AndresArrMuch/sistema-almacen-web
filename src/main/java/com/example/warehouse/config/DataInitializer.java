package com.example.warehouse.config;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.Supplier;
import com.example.warehouse.model.User;
import com.example.warehouse.model.Category;
import com.example.warehouse.repository.ProductRepository;
import com.example.warehouse.repository.SupplierRepository;
import com.example.warehouse.repository.UserRepository;
import com.example.warehouse.repository.CategoryRepository;
import com.example.warehouse.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserService userService,
                                          SupplierRepository supplierRepository,
                                          ProductRepository productRepository,
                                          CategoryRepository categoryRepository) {
        return args -> {
            userService.initializeAdmin();

            if (supplierRepository.count() == 0) {
                Supplier supplier1 = supplierRepository.save(new Supplier("Almacen Central", "+52 55 1234 5678", "contacto@almacencentral.com"));
                Supplier supplier2 = supplierRepository.save(new Supplier("Proveedora Global", "+52 55 9876 5432", "ventas@proveedorglobal.com"));

                Category empaque = categoryRepository.save(new Category("Empaque"));
                Category herramientas = categoryRepository.save(new Category("Herramientas"));
                Category logistica = categoryRepository.save(new Category("Logística"));

                productRepository.save(new Product("Cajas industrial", empaque, 36, new BigDecimal("18.50"), new BigDecimal("28.00"), supplier1));
                productRepository.save(new Product("Taladros eléctricos", herramientas, 8, new BigDecimal("250.00"), new BigDecimal("320.00"), supplier2));
                productRepository.save(new Product("Flejes metálicos", logistica, 3, new BigDecimal("12.00"), new BigDecimal("18.50"), supplier1));
            }
        };
    }
}
