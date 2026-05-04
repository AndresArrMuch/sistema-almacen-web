package com.example.warehouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("appName", "Gestión de Almacén");
        return "settings";
    }
}
