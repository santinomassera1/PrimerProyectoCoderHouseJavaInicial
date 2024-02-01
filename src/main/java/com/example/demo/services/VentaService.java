package com.example.demo.services;

import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repository.RepositoryVenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private RepositoryVenta repositoryVenta;

    @Autowired
    private ProductoService productoService;

    public List<Venta> obtenerTodasLasVentas() {
        return repositoryVenta.findAll();
    }

    public ResponseEntity<String> agregarVenta(Venta venta) {
        try {
            repositoryVenta.save(venta);
            return new ResponseEntity<>("Venta guardada", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al guardar la venta", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> modificarVenta(Long id, Venta venta) {
        try {
            Optional<Venta> ventaExistenteOptional = repositoryVenta.findById(id);

            if (ventaExistenteOptional.isPresent()) {
                Venta ventaExistente = ventaExistenteOptional.get();

                // Actualizar la venta de los productos
                ventaExistente.setProductos(venta.getProductos());
                ventaExistente.setTotal(calcularTotal(venta.getProductos()));

                // Guardar la venta ya actualizada
                repositoryVenta.save(ventaExistente);

                return new ResponseEntity<>("Venta modificada", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Venta no encontrada", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al modificar la venta", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private double calcularTotal(List<Producto> productos) {
        double total = 0.0;
        for (Producto producto : productos) {
            total += producto.getPrecio();
        }
        return total;
    }
}
