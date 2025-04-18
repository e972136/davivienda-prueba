package com.davivienda.davivienda_producto.utilities;

import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

/**
 * Metodos generales
 *
 */
public class MetodosGenerales {
    private MetodosGenerales() {
    }

    public static String mensajeDeValidacion(BindingResult result){
        String collect = result.getFieldErrors().stream().map(err ->
            err.getField() + "->" + err.getDefaultMessage()
        ).collect(Collectors.joining(","));
        return collect;
    }
}
