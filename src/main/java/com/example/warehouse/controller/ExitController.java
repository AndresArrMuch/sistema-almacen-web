package com.example.warehouse.controller;

import com.example.warehouse.model.InventoryExit;
import com.example.warehouse.model.Product;
import com.example.warehouse.service.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exits")
public class ExitController {

    private final WarehouseService warehouseService;

    public ExitController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public String listExits(Model model) {
        model.addAttribute("exits", warehouseService.listExits());
        model.addAttribute("products", warehouseService.findProducts("", "", org.springframework.data.domain.PageRequest.of(0, 50)).getContent());
        model.addAttribute("exit", new InventoryExit());
        return "exits";
    }

    @PostMapping("/save")
    public String saveExit(@ModelAttribute InventoryExit exit, Long productId) {
        Product product = warehouseService.findProduct(productId);
        exit.setProduct(product);
        warehouseService.saveExit(exit);
        return "redirect:/exits";
    }
}
