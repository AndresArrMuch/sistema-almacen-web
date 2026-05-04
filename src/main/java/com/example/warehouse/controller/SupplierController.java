package com.example.warehouse.controller;

import com.example.warehouse.model.Supplier;
import com.example.warehouse.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    private final WarehouseService warehouseService;

    public SupplierController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public String listSuppliers(Model model) {
        model.addAttribute("suppliers", warehouseService.listSuppliers());
        model.addAttribute("supplier", new Supplier());
        return "suppliers";
    }

    @PostMapping("/save")
    public String saveSupplier(@ModelAttribute Supplier supplier) {
        warehouseService.saveSupplier(supplier);
        return "redirect:/suppliers";
    }

    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        warehouseService.deleteSupplier(id);
        return "redirect:/suppliers";
    }
}
