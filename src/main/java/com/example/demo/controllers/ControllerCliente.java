package com.example.demo.controllers;

import com.example.demo.models.Cliente;
import com.example.demo.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ControllerCliente {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/todos")
    public List<Cliente> getClientes() {
        return clienteService.obtenerClientes();
    }
    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable Long Id) {
        return clienteService.obtenerClientePorId(Id);
    }

    @PostMapping("/alta")
    public String post(@RequestBody Cliente cliente) {
        return clienteService.agregarCliente(cliente);
    }

    @PutMapping("/modificar/{id}")
    public String update(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.actualizarCliente(id, cliente);
    }

    @DeleteMapping("/baja/{id}")
    public String delete(@PathVariable Long id) {
        return clienteService.eliminarCliente(id);
    }
}
