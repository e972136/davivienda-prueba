package com.davivienda.davivienda_producto.dto;

import java.math.BigDecimal;

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
