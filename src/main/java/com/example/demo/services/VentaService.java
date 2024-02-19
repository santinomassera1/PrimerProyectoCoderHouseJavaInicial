package com.example.demo.services;


import com.example.demo.models.Producto;
import com.example.demo.models.Venta;
import com.example.demo.repository.RepositoryVenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
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

                ventaExistente.setProductos(venta.getProductos());
                ventaExistente.setTotal(calcularTotal(venta.getProductos()));

                restarStockDeProductos(ventaExistente.getProductos());

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

                if (cantidadVendida <= productoExistente.getStock()) {
                    int nuevoStock = productoExistente.getStock() - cantidadVendida;
                    productoExistente.setStock(nuevoStock);

                    productoExistente.setCantVendida(productoExistente.getCantVendida() + cantidadVendida);

                    productoService.guardarProducto(productoExistente);
                } else {
                    System.out.println("No hay suficiente stock para el producto: " + productoExistente.getNombre());
                }
            }
        }
    }
    public ResponseEntity<String> generarComprobanteVenta(Long id) {
        try {
            Optional<Venta> ventaExistenteOptional = repositoryVenta.findById(id);

            if (ventaExistenteOptional.isPresent()) {
                Venta ventaExistente = ventaExistenteOptional.get();

                String fecha = obtenerFecha();

                String cliente = ventaExistente.getCliente().getNombre();
                List<Producto> productos = ventaExistente.getProductos();
                double totalVenta = calcularTotal(productos);

                String comprobante = crearComprobante(fecha, cliente, productos, totalVenta);

                mostrarComprobante(fecha, cliente, productos, totalVenta);

                mostrarStockDespuesDeVenta(ventaExistente.getProductos());

                return new ResponseEntity<>(comprobante, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Venta no encontrada", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al generar el comprobante", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private String crearComprobante(String fecha, String cliente, List<Producto> productos, double totalVenta) {
        StringBuilder comprobanteBuilder = new StringBuilder();
        comprobanteBuilder.append("Fecha: ").append(fecha).append("\n");
        comprobanteBuilder.append("Cliente: ").append(cliente).append("\n");
        comprobanteBuilder.append("Listado de productos vendidos:\n");
        for (Producto producto : productos) {
            comprobanteBuilder.append("   - ").append(producto.getNombre()).append(": ").append(producto.getPrecio()).append("\n");
        }
        comprobanteBuilder.append("Total de la venta: ").append(totalVenta).append("\n");

        return comprobanteBuilder.toString();
    }

    private void mostrarComprobante(String comprobante, String cliente, List<Producto> productoList, Double totalVenta) {
        System.out.println("Comprobante de venta:\n" + comprobante);
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


    private String obtenerFecha() {
        try {
            return externalWebService.obtenerFecha();
        } catch (Exception e) {
            return new Date().toString();
        }
    }

    private double calcularTotal(List<Producto> productos) {
        double total = 0.0;
        for (Producto producto : productos) {
            total += producto.getPrecio();
        }
        return total;
    }
    public ResponseEntity<String> eliminarVentaPorId(Long id) {
        try {
            Optional<Venta> ventaExistenteOptional = repositoryVenta.findById(id);

            if (ventaExistenteOptional.isPresent()) {

                Venta ventaExistente = ventaExistenteOptional.get();

                agregarStockDeProductos(ventaExistente.getProductos());

                repositoryVenta.deleteById(id);

                return new ResponseEntity<>("Venta eliminada", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Venta no encontrada", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la venta", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void agregarStockDeProductos(List<Producto> productos) {
        for (Producto producto : productos) {
            Producto productoExistente = productoService.obtenerProductoPorId(producto.getId());
            if (productoExistente != null) {
                int cantidadVendida = producto.getCantVendida();
                productoService.agregarStockAProducto(productoExistente, cantidadVendida);
            }
        }
    }
}
