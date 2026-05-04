package com.example.warehouse.controller;

import com.example.warehouse.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final WarehouseService warehouseService;

    public DashboardController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalProducts", warehouseService.totalProducts());
        model.addAttribute("monthlyEntries", warehouseService.monthlyEntries());
        model.addAttribute("monthlyExits", warehouseService.monthlyExits());
        model.addAttribute("inventoryValue", warehouseService.inventoryValue());
        model.addAttribute("productsByCategory", warehouseService.productsByCategory());
        return "dashboard";
    }
}
