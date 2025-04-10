package com.davivienda.davivienda_producto.controller;


import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.dto.ProductoResponse;
import com.davivienda.davivienda_producto.dto.ProductoVenta;
import com.davivienda.davivienda_producto.entitie.Producto;
import com.davivienda.davivienda_producto.service.ProductoService;
import com.davivienda.davivienda_producto.utilities.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@Validated
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("all/familia/{familia}")
    @Operation(summary = "Obtener lista de productos por familia")
    ResponseEntity<List<ProductoResponse>> allProductsByFamily(
        @PathVariable String familia
    ){
        List<ProductoResponse> all = productoService.findAllByFamilia(familia.trim())
                .stream()
                .map(p-> MapeadoProducto.fromProductoToProductoResponse(p,log))
                .toList();
        if(all.isEmpty()){
            return ResponseEntity.ok()
                    .header(MensajesSistema.LISTA_VACIA.getTipo(),MensajesSistema.LISTA_VACIA.getMensaje())
                    .body(all);
        }
        return ResponseEntity.ok(all);
    }


    @GetMapping("/all/page")
    @Operation(summary = "Obtener lista de productos paginado")
    ResponseEntity<Page<ProductoResponse>> allProductsPage(
            @RequestParam(required = true,defaultValue = "1")@Min(1) Integer page,
            @RequestParam(required = true,defaultValue = "5")@Min(1) Integer size
    ){

        Page<ProductoResponse> all = productoService.findAllPage(page,size)
                .map(p->MapeadoProducto.fromProductoToProductoResponse(p,log));
        if(all.isEmpty()){
            return ResponseEntity.ok()
                    .header(MensajesSistema.LISTA_VACIA.getTipo(),MensajesSistema.LISTA_VACIA.getMensaje())
                    .body(all);
        }
        return ResponseEntity.ok(all);
    }

    @GetMapping("/all/disponibles")
    @Operation(summary = "Obtener lista de productos con existencia mayor a 0")
    ResponseEntity<List<ProductoResponse>> allProductosDisponibles(
    ){
        List<ProductoResponse> all =   productoService.findAllDisponibles()
                .stream()
                .map(p->MapeadoProducto.fromProductoToProductoResponse(p,log))
                .toList();

        if(all.isEmpty()){
            return ResponseEntity.ok()
                    .header(MensajesSistema.LISTA_VACIA.getTipo(),MensajesSistema.LISTA_VACIA.getMensaje())
                    .body(all);
        }
        return ResponseEntity.ok(all);
    }


    @GetMapping("/all")
    @Operation(summary = "Obtener lista de productos")
    ResponseEntity<List<ProductoResponse>> allProductsList(
    ){
        List<ProductoResponse> all = productoService.findAll()
                .stream()
                .map(p->MapeadoProducto.fromProductoToProductoResponse(p,log))
                .toList();

        if(all.isEmpty()){
            return ResponseEntity.ok()
                    .header(MensajesSistema.LISTA_VACIA.getTipo(),MensajesSistema.LISTA_VACIA.getMensaje())
                    .body(all);
        }
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener producto por codigo",responses = {
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "200", description = "Producto encontrado")})
    ResponseEntity<ProductoResponse> getProduct(
            @PathVariable String codigo
    ){
        try{
            ProductoResponse byCodigo = productoService.getByCodigo(codigo);
            return ResponseEntity.ok(byCodigo);
        }catch (DaviviendaNotFoundException e){
            log.error(e.getMessage());
            return ResponseEntity.status(404)
                    .header(MensajesSistema.NO_ENCONTRADO.getTipo(),MensajesSistema.NO_ENCONTRADO.getMensaje())
                    .build();
        }
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar producto por codigo",
            responses = {
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "200", description = "Producto encontrado")
            }
    )
    ResponseEntity<String> deleteProducto(
            @PathVariable String codigo
    ){
        try{
            String s = productoService.deleteProduct(codigo);
            return ResponseEntity.ok(s);
        }catch (DaviviendaNotFoundException e){
            return ResponseEntity.status(404)
                    .header(MensajesSistema.NO_ENCONTRADO.getTipo(),MensajesSistema.NO_ENCONTRADO.getMensaje())
                    .build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar precio de producto")
    ResponseEntity<ProductoResponse> updatePrecioProducto(
            @PathVariable int id,
            @RequestParam BigDecimal precio
            ){
        try{
            Producto producto = productoService.updatePrecioProducto(id, precio);
            return ResponseEntity.ok(MapeadoProducto.fromProductoToProductoResponse(producto,log));
        }catch (DaviviendaNotFoundException e){
            return ResponseEntity.status(404)
                    .header(MensajesSistema.NO_ENCONTRADO.getTipo(),MensajesSistema.NO_ENCONTRADO.getMensaje())
                    .build();
        }
    }

    @PutMapping("codigo/{codigo}/cantidad/{cantidad}")
    @Operation(summary = "Venta de producto")
    ResponseEntity<ProductoVenta> ventaProducto(
            @PathVariable String codigo,
            @PathVariable Integer cantidad
    ){
        try{
            Producto producto = productoService.ventaProducto(codigo,cantidad);
            ProductoVenta productoVenta = new ProductoVenta(producto.getId(), producto.getCodigo(), cantidad, producto.getPrecio());
            return ResponseEntity.ok(productoVenta);
        }catch (DaviviendaInsuficienteException e){
            return ResponseEntity.status(404)
                    .header(MensajesSistema.NO_HAY_EXISTENCIA.getTipo(),MensajesSistema.NO_HAY_EXISTENCIA.getMensaje())
                    .build();
        }catch (DaviviendaNotFoundException e){
            return ResponseEntity.status(404)
                    .header(MensajesSistema.NO_ENCONTRADO.getTipo(),MensajesSistema.NO_ENCONTRADO.getMensaje())
                    .build();
        }
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar producto",description = "No actualiza ni el Id ni el Codigo del producto")
    ResponseEntity<ProductoResponse> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductoRequest producto,
            BindingResult result
    ){
        if(result.hasErrors()){
            log.info(MetodosGenerales.mensajeDeValidacion(result));
            return ResponseEntity.status(400)
                    .header(MensajesSistema.BAD_REQUEST.getTipo(),MensajesSistema.BAD_REQUEST.getMensaje())
                    .build();
        }
        try{
            ProductoResponse productoActualizado = MapeadoProducto.fromProductoToProductoResponse(productoService.updateProducto(id, producto),log);
            return ResponseEntity.ok(productoActualizado);
        }catch (DaviviendaNotFoundException e){
            return ResponseEntity.status(404)
                    .header(MensajesSistema.NO_ENCONTRADO.getTipo(),MensajesSistema.NO_ENCONTRADO.getMensaje())
                    .build();
        }catch (DaviviendaDuplicateException e){
            return ResponseEntity.status(404)
                    .header(MensajesSistema.ELEMENDO_DUPLICADO.getTipo(),MensajesSistema.ELEMENDO_DUPLICADO.getMensaje())
                    .build();
        }
    }

    @PostMapping("/save")
    @Operation(summary = "Almacenar producto nuevo",responses = {
            @ApiResponse(responseCode = "200", description = "Producto almacenado con exito"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    ResponseEntity<ProductoResponse> saveProduct(
            @Valid @RequestBody ProductoRequest producto,
            BindingResult result
    ){
        if(result.hasErrors()){
            log.info(MetodosGenerales.mensajeDeValidacion(result));
            return ResponseEntity.status(400)
                    .header(MensajesSistema.BAD_REQUEST.getTipo(),MensajesSistema.BAD_REQUEST.getMensaje())
                    .build();
        }

        try{
            ProductoResponse saved = productoService.save(producto);
            return  ResponseEntity.ok(saved);
        }catch (DaviviendaDuplicateException e){
            return ResponseEntity.status(404)
                    .header(MensajesSistema.ELEMENDO_DUPLICADO.getTipo(),MensajesSistema.ELEMENDO_DUPLICADO.getMensaje())
                    .build();
        }

    }


}
