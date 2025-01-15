package com.davivienda.davivienda_producto.utilities;

import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.dto.ProductoResponse;
import com.davivienda.davivienda_producto.entitie.Producto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;

/**
 *  Metodos para mapeados de la entidad producto
 *
 */
public class MapeadoProducto {
    private MapeadoProducto() {
    }

    public static ProductoResponse fromProductoToProductoResponse(Producto saveDb, Logger log) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ProductoResponse response = null;
        try{
            response = mapper.readValue(mapper.writeValueAsString(saveDb),ProductoResponse.class);
        }catch (Exception e){
            log.info(e.toString());
        }
        return response;
    }


    public static  Producto fromProductoRequestToProducto(ProductoRequest producto, Logger log) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        Producto productoGenerado = null;
        try {
            productoGenerado = mapper.readValue(mapper.writeValueAsString(producto), Producto.class);
        }catch (Exception e){
            log.info(e.toString());
        }
        return productoGenerado;
    }
}
