package com.davivienda.davivienda_producto.utilities;

public enum MensajesSistema {

    NO_ENCONTRADO("error-davivienda","Producto no encontrado"),
    LISTA_VACIA("mensaje-davivienda","Lista vacia"),
    BAD_REQUEST("error-davivienda","Bad Request, ver logging"),

    ERROR_PARSING("error-davivienda","Problemas con el parsing"),
    NO_HAY_EXISTENCIA("error-davivienda","No hay existencia de este producto")
    ;

    private final String tipo;
    private final String mensaje;


    MensajesSistema(String tipo, String mensaje) {
        this.tipo = tipo;
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }
}