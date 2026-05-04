package com.example.warehouse.controller;

import com.example.warehouse.model.InventoryEntry;
import com.example.warehouse.model.Product;
import com.example.warehouse.model.Supplier;
import com.example.warehouse.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/entries")
public class EntryController {

    private final WarehouseService warehouseService;

    public EntryController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public String listEntries(Model model) {
        model.addAttribute("entries", warehouseService.listEntries());
        model.addAttribute("products", warehouseService.findProducts("", "", org.springframework.data.domain.PageRequest.of(0, 50)).getContent());
        model.addAttribute("suppliers", warehouseService.listSuppliers());
        model.addAttribute("entry", new InventoryEntry());
        return "entries";
    }

    @PostMapping("/save")
    public String saveEntry(@ModelAttribute InventoryEntry entry, Long productId, Long supplierId) {
        Product product = warehouseService.findProduct(productId);
        Supplier supplier = warehouseService.findSupplier(supplierId);
        entry.setProduct(product);
        entry.setSupplier(supplier);
        warehouseService.saveEntry(entry);
        return "redirect:/entries";
    }
}
