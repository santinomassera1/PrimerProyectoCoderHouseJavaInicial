package com.example.demo.services;

import com.example.demo.models.Cliente;
import com.example.demo.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private Repository repo;

    public List<Cliente> obtenerClientes() {
        return repo.findAll();
    }

    public Cliente obtenerClientePorId(Long Id) {
        return repo.findById(Id).orElse(null);
    }
    public String agregarCliente(Cliente cliente) {
        repo.save(cliente);
        return "Guardado";
    }

    public String actualizarCliente(Long id, Cliente cliente) {
        Cliente clienteExistente = repo.findById(id).orElse(null);

        if (clienteExistente != null) {
            clienteExistente.setNombre(cliente.getNombre());
            clienteExistente.setApellido(cliente.getApellido());
            clienteExistente.setEmail(cliente.getEmail());
            repo.save(clienteExistente);
            return "Modificado";
        } else {
            return "Cliente no encontrado";
        }
    }

    public String eliminarCliente(Long id) {
        Cliente clienteExistente = repo.findById(id).orElse(null);

        if (clienteExistente != null) {
            repo.delete(clienteExistente);
            return "Eliminado";
        } else {
            return "Cliente no encontrado";
        }
    }
}
