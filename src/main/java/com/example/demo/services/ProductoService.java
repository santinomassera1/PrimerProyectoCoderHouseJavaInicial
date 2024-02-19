package com.example.demo.services;

import com.example.demo.models.Producto;
import com.example.demo.repository.RepositoryProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private RepositoryProducto repositoryProduct;

    public List<Producto> obtenerProductos() {
        return repositoryProduct.findAll();
    }

    public Producto obtenerProductoPorId(Long id) {
        Optional<Producto> productoOptional = repositoryProduct.findById(id);
        return productoOptional.orElse(null);
    }


    public ResponseEntity<String> agregarProducto(Producto producto) {
        repositoryProduct.save(producto);
        return new ResponseEntity<>("Guardado", HttpStatus.CREATED);
    }

    public ResponseEntity<String> actualizarProducto(Long id, Producto producto) {
        Producto productoExistente = repositoryProduct.findById(id).orElse(null);

        if (productoExistente != null) {
            productoExistente.setNombre(producto.getNombre());
            productoExistente.setDescripcion(producto.getDescripcion());
            productoExistente.setPrecio(producto.getPrecio());
            productoExistente.setStock(producto.getStock());

            repositoryProduct.save(productoExistente);
            return new ResponseEntity<>("Modificado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }
    public ResponseEntity<String> actualizarPrecioProducto(Long id, double nuevoPrecio) {
        Producto productoExistente = repositoryProduct.findById(id).orElse(null);

        if (productoExistente != null) {
            productoExistente.setPrecio(nuevoPrecio);

            repositoryProduct.save(productoExistente);
            return new ResponseEntity<>("Precio del producto actualizado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> eliminarProducto(Long id) {
        Producto productoExistente = repositoryProduct.findById(id).orElse(null);

        if (productoExistente != null) {
            repositoryProduct.delete(productoExistente);
            return new ResponseEntity<>("Eliminado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    public void guardarProducto(Producto producto) {
        repositoryProduct.save(producto);
    }
    public void agregarStockAProducto(Producto producto, int cantidad) {
        if (cantidad > 0) {
            producto.setStock(producto.getStock() + cantidad);
            repositoryProduct.save(producto);
        }
    }
}