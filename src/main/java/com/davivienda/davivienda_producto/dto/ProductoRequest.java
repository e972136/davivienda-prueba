package com.davivienda.davivienda_producto.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest{
        String codigo;
        String descripcion;
        BigDecimal precio;
        int existencia;
}
