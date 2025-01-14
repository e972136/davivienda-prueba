package com.davivienda.davivienda_producto.controller;


import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.entitie.Producto;
import com.davivienda.davivienda_producto.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/all")
    ResponseEntity<List<Producto>> allProducts(

    ){
        return ResponseEntity.ok(productoService.findAll());
    }

    @PostMapping("/save")
    ResponseEntity<Producto> saveProduct(
            @RequestBody ProductoRequest producto
    ){
        return  ResponseEntity.ok(productoService.save(producto));
    }

    @GetMapping("/{codigo}")
    ResponseEntity<Producto> getProduct(
            @PathVariable String codigo
    ){
        return ResponseEntity.ok(productoService.getByCodigo(codigo));
    }

    @DeleteMapping("/{codigo}")
    ResponseEntity<String> deleteProducto(
            @PathVariable String codigo
    ){
        return ResponseEntity.ok(productoService.deleteProduct(codigo));
    }

    @PutMapping("/{id}")
    ResponseEntity<Producto> updatePrecioProducto(
            @PathVariable int id,
            @RequestParam BigDecimal precio
            ){
        return ResponseEntity.ok(productoService.updatePrecioProducto(id,precio));
    }


}
