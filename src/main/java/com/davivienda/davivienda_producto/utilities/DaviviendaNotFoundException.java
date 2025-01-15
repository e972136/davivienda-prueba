package com.davivienda.davivienda_producto.utilities;

/**
 *  Exception de producto no encotrado
 *
 */
public class DaviviendaNotFoundException extends RuntimeException{

    public DaviviendaNotFoundException(String message) {
        super(message);
    }
}
