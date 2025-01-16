package com.davivienda.davivienda_producto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *  Entidad para retornar la venta del producto
 */
public record ProductoVenta(
        int id,
        String codigo,
        int cantidadVendida,
        BigDecimal precio,
        BigDecimal subTotal
) {

    public ProductoVenta(int id, String codigo, int cantidadVendida, BigDecimal precio) {
        this(id, codigo, cantidadVendida, precio, getSubTotal(cantidadVendida,precio));
    }

    private static BigDecimal getSubTotal(
            int cantidadVendida,
            BigDecimal precio
    ) {
        return precio.multiply(BigDecimal.valueOf(cantidadVendida));
    }
}
