package com.example.demo.services;

import com.example.demo.models.Producto;
import com.example.demo.repository.RepositoryProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private RepositoryProducto repositoryProduct;

    public List<Producto> obtenerProductos() {
        return repositoryProduct.findAll();
    }

    public String agregarProducto(Producto producto) {
        repositoryProduct.save(producto);
        return "Guardado";
    }

    public String actualizarProducto(Long id, Producto producto) {
        Producto productoExistente = repositoryProduct.findById(id).orElse(null);

        if (productoExistente != null) {
            productoExistente.setNombre(producto.getNombre());
            productoExistente.setDescripcion(producto.getDescripcion());
            productoExistente.setPrecio(producto.getPrecio());
            productoExistente.setStockMaximo(producto.getStockMaximo());
            productoExistente.setStockMinimo(producto.getStockMinimo());

            repositoryProduct.save(productoExistente);
            return "Modificado";
        } else {
            return "Producto no encontrado";
        }
    }

    public String eliminarProducto(Long id) {
        Producto productoExistente = repositoryProduct.findById(id).orElse(null);

        if (productoExistente != null) {
            repositoryProduct.delete(productoExistente);
            return "Eliminado";
        } else {
            return "Producto no encontrado";
        }
    }
}
