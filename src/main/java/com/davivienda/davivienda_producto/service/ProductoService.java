package com.davivienda.davivienda_producto.service;

import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.entitie.Producto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {
    List<Producto> findAll();

    Producto save(ProductoRequest producto);

    Producto getByCodigo(String codigo);

    String deleteProduct(String codigo);

    Producto updatePrecioProducto(int id, BigDecimal producto);
}
