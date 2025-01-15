package com.davivienda.davivienda_producto.dto;

import java.math.BigDecimal;

/**
 *
 * @param id Identificador unico
 * @param codigo Codigo alfanumerico
 * @param descripcion Descripcion del producto
 * @param familia Familia al que pertenece [ELECTRICO, MOTOR...]
 * @param precio valor actual del producto
 * @param existencia Existencia actual
 */
public record ProductoResponse(
        int id,
        String codigo,
        String descripcion,
        String familia,
        BigDecimal precio,
        int existencia
)
{

}
