package com.davivienda.davivienda_producto.service;

import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.entitie.Producto;
import com.davivienda.davivienda_producto.repository.ProductRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
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
    public Producto save(ProductoRequest producto) {

        Producto p = fromProductoRecord(producto);
        return productRepository.save(p);

    }

    @Override
    public Producto getByCodigo(String codigo) {
        return productRepository.findByCodigo(codigo);
    }

    @Override
    public String deleteProduct(String codigo) {
        Producto byCodigo = productRepository.findByCodigo(codigo);
        if(isNull(byCodigo)){
            return "Producto no encontrado";
        }
        productRepository.delete(byCodigo);
        return "Producto eliminado";
    }

    @Override
    @Transactional
    public Producto updatePrecioProducto(int id, BigDecimal producto) {
        Optional<Producto> byId = productRepository.findById(id);
        if(!byId.isPresent()){
            return null;
        }
        Producto productoDB = byId.get();
        productoDB.setPrecio(producto);
        return productoDB;
    }

    private Producto fromProductoRecord(ProductoRequest producto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        Producto productoGenerado = null;
        try {
            productoGenerado = mapper.readValue(mapper.writeValueAsString(producto), Producto.class);
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return productoGenerado;
    }
}
