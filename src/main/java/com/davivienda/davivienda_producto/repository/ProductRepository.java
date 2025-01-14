package com.davivienda.davivienda_producto.repository;

import com.davivienda.davivienda_producto.entitie.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Producto,Integer> {
    Producto findByCodigo(String codigo);
}
