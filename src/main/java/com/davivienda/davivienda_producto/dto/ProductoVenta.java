package com.davivienda.davivienda_producto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoVenta {


    int id;
    String codigo;
    int cantidadVendida;
    BigDecimal precio;

    public BigDecimal getSubTotal() {
        return precio.multiply(BigDecimal.valueOf(cantidadVendida));
    }
}
