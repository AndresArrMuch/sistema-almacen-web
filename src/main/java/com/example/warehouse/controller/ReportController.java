package com.example.warehouse.controller;

import com.example.warehouse.model.InventoryEntry;
import com.example.warehouse.model.InventoryExit;
import com.example.warehouse.model.Product;
import com.example.warehouse.service.WarehouseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    private final WarehouseService warehouseService;

    public ReportController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/reports")
    public String reports(Model model,
                          @RequestParam(required = false) String startDate,
                          @RequestParam(required = false) String endDate) {
        YearMonth month = YearMonth.now();
        LocalDate start = startDate == null || startDate.isBlank() ? month.atDay(1) : LocalDate.parse(startDate);
        LocalDate end = endDate == null || endDate.isBlank() ? month.atEndOfMonth() : LocalDate.parse(endDate);

        List<InventoryEntry> entries = warehouseService.listEntriesByRange(start, end);
        List<InventoryExit> exits = warehouseService.listExitsByRange(start, end);
        List<Product> lowStock = warehouseService.lowStockProducts();

        model.addAttribute("totalProducts", warehouseService.totalProducts());
        model.addAttribute("monthlyEntries", warehouseService.monthlyEntries());
        model.addAttribute("monthlyExits", warehouseService.monthlyExits());
        model.addAttribute("inventoryValue", warehouseService.inventoryValue());
        model.addAttribute("lowStock", lowStock);
        model.addAttribute("recentEntries", entries.stream().limit(8).collect(Collectors.toList()));
        model.addAttribute("recentExits", exits.stream().limit(8).collect(Collectors.toList()));
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        return "reports";
    }

    @GetMapping("/reports/export/products")
    public ResponseEntity<byte[]> exportProductsCsv() {
        List<Product> products = warehouseService.listAllProducts();
        String csv = "Nombre,Categoría,Proveedor,Cantidad,Estado,Precio\n" +
                products.stream()
                        .map(product -> String.format("%s,%s,%s,%d,%s,%s",
                                escapeCsv(product.getName()),
                                escapeCsv(product.getCategory() != null ? product.getCategory().getName() : ""),
                                escapeCsv(product.getSupplier() != null ? product.getSupplier().getName() : ""),
                                product.getQuantity(),
                                escapeCsv(product.getStatus()),
                                product.getSalePrice()))
                        .collect(Collectors.joining("\n"));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos.csv")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(csv.getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping("/reports/export/movements")
    public ResponseEntity<byte[]> exportMovementsCsv(@RequestParam(defaultValue = "entries") String type) {
        String csv;
        String filename;
        if ("exits".equalsIgnoreCase(type)) {
            List<InventoryExit> exits = warehouseService.listExitsByRange(YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth());
            filename = "salidas.csv";
            csv = "Fecha,Producto,Cantidad,Destino\n" +
                    exits.stream()
                            .map(exit -> String.format("%s,%s,%d,%s",
                                    exit.getDate(),
                                    escapeCsv(exit.getProduct() != null ? exit.getProduct().getName() : ""),
                                    exit.getQuantity(),
                                    escapeCsv(exit.getDestination())))
                            .collect(Collectors.joining("\n"));
        } else {
            List<InventoryEntry> entries = warehouseService.listEntriesByRange(YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth());
            filename = "entradas.csv";
            csv = "Fecha,Producto,Proveedor,Cantidad,Nota\n" +
                    entries.stream()
                            .map(entry -> String.format("%s,%s,%s,%d,%s",
                                    entry.getDate(),
                                    escapeCsv(entry.getProduct() != null ? entry.getProduct().getName() : ""),
                                    escapeCsv(entry.getSupplier() != null ? entry.getSupplier().getName() : ""),
                                    entry.getQuantity(),
                                    escapeCsv(entry.getNote())))
                            .collect(Collectors.joining("\n"));
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(csv.getBytes(StandardCharsets.UTF_8));
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"").contains(",") || value.contains("\n") ? "\"" + value.replace("\"", "\"\"") + "\"" : value;
    }
}
