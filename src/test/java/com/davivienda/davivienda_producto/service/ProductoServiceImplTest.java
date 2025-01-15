package com.davivienda.davivienda_producto.service;

import com.davivienda.davivienda_producto.entitie.Producto;
import com.davivienda.davivienda_producto.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceImplTest {

    @Mock
    ProductRepository repository;

    @InjectMocks
    ProductoServiceImpl candidatoImp;

    @Test
    void findAll() {
        System.out.println(candidatoImp);
        System.out.println(repository);
    }

    @Test
    void findAllPage() {
    }

    @Test
    void updateProducto() {
    }

    @Test
    void findAllByFamilia() {
    }

    @Test
    void findAllDisponibles() {
    }

    @Test
    void ventaProducto() {
    }

    @Test
    void save() {
    }

    @Test
    void getByCodigo() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    void updatePrecioProducto() {
    }
}