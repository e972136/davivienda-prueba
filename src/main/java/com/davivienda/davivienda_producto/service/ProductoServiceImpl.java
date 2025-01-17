package com.davivienda.davivienda_producto.service;

import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.dto.ProductoResponse;
import com.davivienda.davivienda_producto.entitie.Producto;
import com.davivienda.davivienda_producto.repository.ProductRepository;
import com.davivienda.davivienda_producto.utilities.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

/**
 *  Implementacion del servicio de producto
 */
@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService{


    private final ProductRepository productRepository;

    public ProductoServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Producto> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Producto> findAllPage(Integer page, Integer size) {
        return productRepository.findAll(PageRequest.of(page-1,size));
    }

    @Override
    public Producto updateProducto(int id, ProductoRequest producto) {
        Optional<Producto> byId = productRepository.findById(id);
        if(byId.isEmpty()){
            log.info("Id {} no encontrado",id);
            throw new DaviviendaNotFoundException(MensajesSistema.NO_ENCONTRADO.getMensaje());
        }

        Producto productoBD = byId.get();
        Producto productoNuevo = MapeadoProducto.fromProductoRequestToProducto(producto,log);
        if(isNull(productoNuevo)){
            log.info(MensajesSistema.ERROR_PARSING.getMensaje());
            throw new DaviviendaErrorParseoException(MensajesSistema.ERROR_PARSING.getMensaje());
        }
        productoNuevo.setId(productoBD.getId());
        productoNuevo.setCodigo(productoBD.getCodigo());
        productoNuevo.setCreatedAt(productoBD.getCreatedAt());
        productoNuevo.setUpdatedAt(LocalDate.now());
        return productRepository.save(productoNuevo);
    }

    @Override
    public List<Producto> findAllByFamilia(String familia) {
        return productRepository.findAllByFamilia(familia);
    }

    @Override
    public List<Producto> findAllDisponibles() {
        return productRepository.findAll()
                .stream()
                .filter(e->e.getExistencia()>0)
                .toList();
    }

    @Override
    @Transactional
    public Producto ventaProducto(String codigo, Integer cantidad)
            throws DaviviendaNotFoundException,DaviviendaInsuficienteException{
        Optional<Producto> byCodigo = productRepository.findByCodigo(codigo);
        if(byCodigo.isEmpty()){
            log.info("Codigo {} no encontrado",codigo);
            throw new DaviviendaNotFoundException(MensajesSistema.NO_ENCONTRADO.getMensaje());
        }

        Producto producto = byCodigo.get();
        if(producto.getExistencia()<cantidad){
            log.info(MensajesSistema.CODIGO_NO_ENCONTRADO.getMensaje(),codigo);
            throw new DaviviendaInsuficienteException(MensajesSistema.NO_ENCONTRADO.getMensaje());
        }

        producto.setExistencia(producto.getExistencia()-cantidad);

        return producto;
    }

    @Override
    public ProductoResponse save(ProductoRequest producto){

        Optional<Producto> byCodigo = productRepository.findByCodigo(producto.codigo());
        if(byCodigo.isPresent()){
            log.info("Codigo {} duplicado",producto.codigo());
            throw new DaviviendaDuplicateException(MensajesSistema.ELEMENDO_DUPLICADO.getMensaje());
        }

        Producto p = MapeadoProducto.fromProductoRequestToProducto(producto,log);
        if(isNull(p)){
            log.info(MensajesSistema.ERROR_PARSING.getMensaje());
            throw new DaviviendaErrorParseoException(MensajesSistema.ERROR_PARSING.getMensaje());
        }
        p.setCreatedAt(LocalDate.now());
        p.setUpdatedAt(LocalDate.now());

        Producto saveDb = productRepository.save(p);

        return MapeadoProducto.fromProductoToProductoResponse(saveDb,log);
    }


    @Override
    public ProductoResponse getByCodigo(String codigo) {
        Optional<Producto> byCodigo = productRepository.findByCodigo(codigo);
        if(byCodigo.isPresent()){
            return MapeadoProducto.fromProductoToProductoResponse(byCodigo.get(),log);
        }else {
            log.info(MensajesSistema.CODIGO_NO_ENCONTRADO.getMensaje(),codigo);
            throw new DaviviendaNotFoundException(MensajesSistema.NO_ENCONTRADO.getMensaje());
        }
    }

    @Override
    public String deleteProduct(String codigo) {
        Optional<Producto> byCodigo = productRepository.findByCodigo(codigo);

        if(byCodigo.isPresent()){
            productRepository.delete(byCodigo.get());
            return "Producto eliminado";
        }
        log.info(MensajesSistema.CODIGO_NO_ENCONTRADO.getMensaje(),codigo);
        throw new DaviviendaNotFoundException(MensajesSistema.NO_ENCONTRADO.getMensaje());

    }


    @Override
    @Transactional
    public Producto updatePrecioProducto(int id, BigDecimal precioProducto) {
        Optional<Producto> byId = productRepository.findById(id);
        if(!byId.isPresent()){
            log.info("Id {} no encontrado",id);
            throw new DaviviendaNotFoundException(MensajesSistema.NO_ENCONTRADO.getMensaje());
        }
        Producto productoDB = byId.get();
        productoDB.setPrecio(precioProducto);
        productoDB.setUpdatedAt(LocalDate.now());
        return productoDB;
    }

}
