package com.example.demo.controllers;

import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class ControllerVentas {

    @Autowired
    private VentaService ventaService;

    @GetMapping("/todas")
    public List<Venta> obtenerTodasLasVentas() {
        return ventaService.obtenerTodasLasVentas();
    }

    @PostMapping("/alta")
    public ResponseEntity<String> agregarVenta(@RequestBody Venta venta) {
        return ventaService.agregarVenta(venta);
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<String> modificarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.modificarVenta(id, venta);
    }
}
