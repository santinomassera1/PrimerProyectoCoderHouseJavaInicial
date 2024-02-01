package com.example.demo.controllers;
import com.example.demo.models.Producto;
import com.example.demo.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ControllerProducto {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getProductos() {
        return productoService.obtenerProductos();
    }

    @PostMapping("/alta")
    public String agregarProducto(@RequestBody Producto producto) {
        return productoService.agregarProducto(producto);
    }

    @PutMapping("/modificar/{id}")
    public String actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto);
    }

    @DeleteMapping("/baja/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        return productoService.eliminarProducto(id);
    }
}
