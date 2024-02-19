package com.example.demo.services;

import com.example.demo.models.Cliente;
import com.example.demo.repository.RepositoryCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private RepositoryCliente repo;

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
    public ResponseEntity<String> actualizarCliente(Long id, Cliente cliente) {
        Cliente clienteExistente = repo.findById(id).orElse(null);

        if (clienteExistente != null) {
            clienteExistente.setNombre(cliente.getNombre());
            clienteExistente.setApellido(cliente.getApellido());
            clienteExistente.setEmail(cliente.getEmail());
            repo.save(clienteExistente);
            return new ResponseEntity<>("Modificado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> eliminarCliente(Long id) {
        Cliente clienteExistente = repo.findById(id).orElse(null);

        if (clienteExistente != null) {
            repo.delete(clienteExistente);
            return new ResponseEntity<>("Eliminado", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }
    }
}