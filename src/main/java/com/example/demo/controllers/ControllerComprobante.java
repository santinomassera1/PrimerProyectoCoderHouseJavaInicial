package com.example.demo.controllers;

import com.example.demo.models.Venta;
import com.example.demo.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comprobantes")
public class ControllerComprobante {
    @Autowired
    private VentaService ventaService;
    @PostMapping("/generar/{idVenta}")
    public ResponseEntity<String> generarComprobante(@PathVariable Long idVenta) {
        ResponseEntity<Venta> responseVenta = ventaService.obtenerVentaPorId(idVenta);

        if (responseVenta.getStatusCode().is2xxSuccessful()) {

            Venta venta = responseVenta.getBody();
            return ventaService.generarComprobanteVenta(idVenta, venta);
        } else {

            return new ResponseEntity<>("No se pudo obtener la venta", responseVenta.getStatusCode());
        }
    }
}
