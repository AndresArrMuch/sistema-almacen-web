package com.example.warehouse.service;

import com.example.warehouse.model.*;
import com.example.warehouse.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryEntryRepository entryRepository;
    private final InventoryExitRepository exitRepository;
    private final CategoryRepository categoryRepository;

    public WarehouseService(ProductRepository productRepository,
                            SupplierRepository supplierRepository,
                            InventoryEntryRepository entryRepository,
                            InventoryExitRepository exitRepository,
                            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.entryRepository = entryRepository;
        this.exitRepository = exitRepository;
        this.categoryRepository = categoryRepository;
    }

    public long totalProducts() {
        return productRepository.count();
    }

    public BigDecimal inventoryValue() {
        return productRepository.findAll().stream()
                .map(product -> product.getCostPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int monthlyEntries() {
        YearMonth month = YearMonth.now();
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        return entryRepository.findByDateBetween(start, end).stream().mapToInt(InventoryEntry::getQuantity).sum();
    }

    public int monthlyExits() {
        YearMonth month = YearMonth.now();
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        return exitRepository.findByDateBetween(start, end).stream().mapToInt(InventoryExit::getQuantity).sum();
    }

    public Map<String, Long> productsByCategory() {
        return productRepository.findAll().stream()
                .filter(p -> p.getCategory() != null)
                .collect(Collectors.groupingBy(p -> p.getCategory().getName(), Collectors.counting()));
    }

    public Page<Product> findProducts(String search, String category, Pageable pageable) {
        if (category == null || category.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(search == null ? "" : search, pageable);
        }
        return productRepository.findByNameContainingIgnoreCaseAndCategory_NameContainingIgnoreCase(search == null ? "" : search, category, pageable);
    }

    public Product findProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Supplier> listSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier findSupplier(Long id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }

    public List<InventoryEntry> listEntries() {
        return entryRepository.findAll();
    }

    public List<InventoryExit> listExits() {
        return exitRepository.findAll();
    }

    @Transactional
    public void saveEntry(InventoryEntry entry) {
        Product product = entry.getProduct();
        product.setQuantity(product.getQuantity() + entry.getQuantity());
        productRepository.save(product);
        entryRepository.save(entry);
    }

    @Transactional
    public void saveExit(InventoryExit exit) {
        Product product = exit.getProduct();
        product.setQuantity(Math.max(0, product.getQuantity() - exit.getQuantity()));
        productRepository.save(product);
        exitRepository.save(exit);
    }

    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> lowStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getQuantity() < 5)
                .toList();
    }

    public List<InventoryEntry> listEntriesByRange(LocalDate start, LocalDate end) {
        return entryRepository.findByDateBetween(start, end);
    }

    public List<InventoryExit> listExitsByRange(LocalDate start, LocalDate end) {
        return exitRepository.findByDateBetween(start, end);
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategory(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
