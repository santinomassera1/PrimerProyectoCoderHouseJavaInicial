package com.example.demo.services;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

public class ExternalWebService {

    public String obtenerFecha(){
        RestTemplate restTemplate = new RestTemplate();
        String fecha = restTemplate.getForObject("http://localhost:8080/", String.class);
        return fecha;
    }
}
