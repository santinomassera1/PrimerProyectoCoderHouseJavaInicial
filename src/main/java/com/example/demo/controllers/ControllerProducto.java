package com.example.demo.controllers;
import com.example.demo.models.Producto;
import com.example.demo.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ControllerProducto {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/todos")
    public List<Producto> getProductos() {
        return productoService.obtenerProductos();
    }

    @PostMapping("/alta")
    public ResponseEntity<String> agregarProducto(@RequestBody Producto producto) {
        return productoService.agregarProducto(producto);
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<String> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto);
    }
    @PutMapping("/actualizarPrecio/{id}")
    public ResponseEntity<String> actualizarPrecioProducto(@PathVariable Long id, @RequestParam double nuevoPrecio) {
        return productoService.actualizarPrecioProducto(id, nuevoPrecio);
    }

    @DeleteMapping("/baja/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
        return productoService.eliminarProducto(id);
    }
}