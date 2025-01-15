package com.davivienda.davivienda_producto.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest{
        @NotNull
        @Size(min=4,max = 8)
        String codigo;

        @NotNull
        @NotBlank
        String descripcion;

        @NotNull
        @NotBlank
        String familia;

        @NotNull
        @Min(value=0)
        BigDecimal precio;

        @NotNull
        @Min(value=0)
        int existencia;
}
