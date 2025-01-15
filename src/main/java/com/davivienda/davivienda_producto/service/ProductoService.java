package com.davivienda.davivienda_producto.service;

import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.dto.ProductoResponse;
import com.davivienda.davivienda_producto.entitie.Producto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {

    /**
     *
     * @return listado de todos los productos
     */
    List<Producto> findAll();

    /**
     *
     * @param producto producto a guardar
     * @return producto guardado
     */
    ProductoResponse save(ProductoRequest producto);

    /**
     *
     * @param codigo codigo del producto
     * @return obtiene el producto por codigo
     */
    ProductoResponse getByCodigo(String codigo);

    /**
     *
     * @param codigo codigo del producto a eliminar
     * @return retorna mensaje "Producto eliminado" en caso positivo, o retorna mensaje de error
     */
    String deleteProduct(String codigo);

    /**
     *
     * @param id codigo de producto
     * @param precioProducto nuevo precio del producto
     * @return producto actualizado
     */
    Producto updatePrecioProducto(int id, BigDecimal precioProducto);

    /**
     *
     * @param page Numero de pagina del 1 al n
     * @param size Cantidad de elementos por pagina
     * @return pagina con los elementos requeridos
     */
    Page<Producto> findAllPage(Integer page, Integer size);

    /**
     *
     * @param id del producto
     * @param producto entidad con la data del producto
     * @return el producto actualizado
     */
    Producto updateProducto(int id, ProductoRequest producto);

    /**
     *
     * @param familia Busca los elementos por familia
     * @return listado de productos por familia
     */
    List<Producto> findAllByFamilia(String familia);

    /**
     *
     * @return productos con existencia mayor a 0
     */
    List<Producto> findAllDisponibles();

    /**
     *
     * @param codigo Codigo del producto
     * @param cantidad Cantidad solicitada, debe ser menor o igual a la existencia
     * @return el producto ya rebajado
     */
    Producto ventaProducto(String codigo, Integer cantidad);
}
