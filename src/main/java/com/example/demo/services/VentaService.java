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
    @Autowired
    private ExternalWebService externalWebService;


    public List<Venta> obtenerTodasLasVentas() {
        return repositoryVenta.findAll();
    }

    public ResponseEntity<Venta> obtenerVentaPorId(Long id) {
        Optional<Venta> ventaOptional = repositoryVenta.findById(id);
        return ventaOptional.map(venta -> new ResponseEntity<>(venta, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

                // Restar la cantidad vendida del stock de cada producto
                restarStockDeProductos(ventaExistente.getProductos());

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

    private void restarStockDeProductos(List<Producto> productos) {
        for (Producto producto : productos) {
            Producto productoExistente = productoService.obtenerProductoPorId(producto.getId());
            if (productoExistente != null) {
                int cantidadVendida = producto.getCantVendida();
                productoService.restarStockDeProducto(productoExistente, cantidadVendida);
            }
        }
    }

    public ResponseEntity<String> generarComprobanteVenta(Long id, Venta venta) {
        try {
            Optional<Venta> ventaExistenteOptional = repositoryVenta.findById(id);

            if (ventaExistenteOptional.isPresent()) {
                Venta ventaExistente = ventaExistenteOptional.get();

                // Obtener la fecha del servicio externo
                String fecha = externalWebService.obtenerFecha();

                // Otros detalles del comprobante
                String cliente = ventaExistente.getCliente().getNombre(); // Asume que hay un atributo nombre en Cliente
                List<Producto> productos = ventaExistente.getProductos();
                double totalVenta = calcularTotal(productos);

                // Mostrar el comprobante
                mostrarComprobante(fecha, cliente, productos, totalVenta);

                // Mostrar el stock después de la venta
                mostrarStockDespuesDeVenta(ventaExistente.getProductos());

                return new ResponseEntity<>("Comprobante generado", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Venta no encontrada", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al generar el comprobante", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void mostrarStockDespuesDeVenta(List<Producto> productos) {
        System.out.println("Stock después de la venta:");

        for (Producto producto : productos) {
            Producto productoConStock = productoService.obtenerProductoPorId(producto.getId());

            if (productoConStock != null) {
                System.out.println(productoConStock.getNombre() + ": " + productoConStock.getStock());
            }
        }
    }

    private void mostrarComprobante(String fecha, String cliente, List<Producto> productos, double totalVenta) {
        System.out.println("Fecha: " + fecha);
        System.out.println("Cliente: " + cliente);

        System.out.println("Listado de productos vendidos:");
        for (Producto producto : productos) {
            System.out.println("   - " + producto.getNombre() + ": " + producto.getPrecio());
        }

        System.out.println("Total de la venta: " + totalVenta);
    }

    private double calcularTotal(List<Producto> productos) {
        double total = 0.0;
        for (Producto producto : productos) {
            total += producto.getPrecio();
        }
        return total;
    }
}

