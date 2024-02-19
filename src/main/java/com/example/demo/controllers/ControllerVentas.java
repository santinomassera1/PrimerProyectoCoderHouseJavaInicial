package com.example.demo.controllers;

import com.example.demo.models.Venta;
import com.example.demo.services.VentaService;
import jakarta.persistence.Id;
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


    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long id) {
        return ventaService.obtenerVentaPorId(id);
    }

    @GetMapping("/comprobante/{id}")
    public ResponseEntity<String> obtenerComprobanteVenta(@PathVariable Long id) {
        return ventaService.generarComprobanteVenta(id);
    }

    @PostMapping("/alta")
    public ResponseEntity<String> agregarVenta(@RequestBody Venta venta) {
        return ventaService.agregarVenta(venta);
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<String> modificarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.modificarVenta(id, venta);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarVenta(@PathVariable Long id) {
        return ventaService.eliminarVentaPorId(id);
    }
}