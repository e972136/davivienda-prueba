package com.davivienda.davivienda_producto.utilities;

/**
 *  Exception de elementos de producto duplicado
 */
public class DaviviendaDuplicateException extends RuntimeException{
    public DaviviendaDuplicateException(String message) {
        super(message);
    }
}
