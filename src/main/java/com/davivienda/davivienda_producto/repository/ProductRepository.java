package com.davivienda.davivienda_producto.repository;

import com.davivienda.davivienda_producto.entitie.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *  Repositorio entidad producto
 */
public interface ProductRepository extends JpaRepository<Producto,Integer> {
    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findAllByFamilia(String familia);
}
