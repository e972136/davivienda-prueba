package com.davivienda.davivienda_producto.service;

import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.dto.ProductoResponse;
import com.davivienda.davivienda_producto.entitie.Producto;
import com.davivienda.davivienda_producto.repository.ProductRepository;
import com.davivienda.davivienda_producto.utilities.DaviviendaDuplicateException;
import com.davivienda.davivienda_producto.utilities.DaviviendaInsuficienteException;
import com.davivienda.davivienda_producto.utilities.DaviviendaNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 *  Se utiliza JUnit 5
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductoServiceImplTest {

    @Autowired
    ProductoServiceImpl candidatoImp;

     @Test
     @DisplayName(value = "Buscar todos")
     @Order(1)
    void findAll() {
        int size = candidatoImp.findAll().size();
        assertThat(size).isEqualTo(11);
    }

    @Test
    @DisplayName(value = "Buscar paginado")
    @Order(2)
    void findAllPage() {
        Page<Producto> allPage = candidatoImp.findAllPage(1, 5);
        int size = allPage.getContent().size();
        assertThat(size).isEqualTo(5);
    }

    @Test
    @DisplayName(value = "Actualizar producto")
    @Order(3)
    void updateProducto() {
        ProductoResponse p = candidatoImp.getByCodigo("1001");
        assertThat(p.familia()).isEqualTo("ELECTRICO");
        Producto actualizado = candidatoImp.updateProducto(p.id(), new ProductoRequest(
                p.codigo(),
                p.descripcion(),
                "patito",
                p.precio(),
                p.existencia()
        ));
        assertThat(actualizado.getFamilia()).isEqualTo("patito");
    }


    @Test()
    @DisplayName(value = "Actualizar producto no existente")
    @Order(4)
    void updateProducto_noExistente() {

        ProductoRequest productoRequest = new ProductoRequest(
                "9999",
                "xxx",
                "xxx",
                BigDecimal.ZERO,
                1
        );

        assertThatThrownBy(()->{
                    candidatoImp.updateProducto(100,productoRequest);
        }).isInstanceOf(DaviviendaNotFoundException.class);
        //https://www.baeldung.com/assertj-exception-assertion
    }



    @Test
    @DisplayName(value = "Listado por familia")
    @Order(5)
    void findAllByFamilia() {
        List<Producto> motor = candidatoImp.findAllByFamilia("ACEITE");
        assertThat(motor).hasSize(2);
    }

    @Test
    @DisplayName(value = "Buscar productos disponibles")
    @Order(6)
    void findAllDisponibles() {
        int size = candidatoImp.findAllDisponibles().size();
        assertThat(size).isEqualTo(6);
    }

    @Test
    @DisplayName(value = "Venta de producto")
    @Order(7)
    void ventaProducto() {
        ProductoResponse byCodigo = candidatoImp.getByCodigo("1001");
        Producto producto = candidatoImp.ventaProducto(byCodigo.codigo(), 1);
        assertThat(producto.getExistencia()).isEqualTo(byCodigo.existencia()-1);
    }

    @Test
    @DisplayName(value = "Venta de producto inexistente")
    @Order(8)
    void ventaProducto_noExistente() {
        assertThatThrownBy(()->{
            candidatoImp.ventaProducto("5000", 1);
        }).isInstanceOf(DaviviendaNotFoundException.class);
    }

    @Test
    @DisplayName(value = "Venta de producto sin existencia")
    @Order(9)
    void ventaProducto_insuficiente() {
        assertThatThrownBy(()->{
            candidatoImp.ventaProducto("1010", 100);
        }).isInstanceOf(DaviviendaInsuficienteException.class);
    }



    @Test
    @DisplayName(value = "Guardar producto")
    @Order(10)
    void save() {
        ProductoResponse save = candidatoImp.save(new ProductoRequest(
                "2020",
                "nuevo",
                "CAJA",
                BigDecimal.valueOf(1),
                0
        ));
        ProductoResponse byCodigo = candidatoImp.getByCodigo("2020");
        assertThat(save.codigo()).isEqualTo(byCodigo.codigo());
    }

    @Test
    @DisplayName(value = "Guardar duplicado")
    @Order(11)
    void save_duplicado() {

        ProductoRequest productoRequest = new ProductoRequest(
                "1001",
                "nuevo",
                "CAJA",
                BigDecimal.valueOf(1),
                0
        );


        assertThatThrownBy(()->{
            ProductoResponse save = candidatoImp.save(productoRequest);
        }).isInstanceOf(DaviviendaDuplicateException.class);

    }

    @Test
    @DisplayName(value = "Buscar por codigo")
    @Order(12)
    void getByCodigo() {
        ProductoResponse byCodigo = candidatoImp.getByCodigo("1001");
        assertThat(byCodigo.codigo()).isEqualTo("1001");
    }

    @Test
    @DisplayName(value = "Buscar codigo inexistente")
    @Order(13)
    void getByCodigo_noEncontrado() {

        assertThatThrownBy(()->{
            candidatoImp.getByCodigo("9999");
        }).isInstanceOf(DaviviendaNotFoundException.class);
    }

    @Test
    @DisplayName(value = "actualizar precio")
    @Order(14)
    void updatePrecioProducto() {
        ProductoResponse byCodigo = candidatoImp.getByCodigo("1002");
        candidatoImp.updatePrecioProducto(byCodigo.id(), BigDecimal.TEN);
        byCodigo = candidatoImp.getByCodigo("1002");
        assertThat(byCodigo.precio().intValue()).isEqualTo(BigDecimal.TEN.intValue());
    }
    @Test
    @DisplayName(value = "actualizar precio de producto inexistente")
    @Order(15)
    void updatePrecioProducto_noEcontrado() {
        assertThatThrownBy(()->{
            candidatoImp.updatePrecioProducto(9999, BigDecimal.TEN);
        }).isInstanceOf(DaviviendaNotFoundException.class);
    }

    @Test
    @DisplayName(value = "eliminar producto")
    @Order(16)
    void deleteProduct() {
        List<Producto> all = candidatoImp.findAll();
        int sizeOriginal = all.size();
        String s = candidatoImp.deleteProduct("1010");
        List<Producto> despues = candidatoImp.findAll();
        assertThat(sizeOriginal-1).isEqualTo(despues.size());
    }

    @Test
    @DisplayName(value = "eliminar producto inexistente")
    @Order(17)
    void deleteProduct_noEcontrado() {
        assertThatThrownBy(()->{
            candidatoImp.deleteProduct("9999");
        }).isInstanceOf(DaviviendaNotFoundException.class);


    }
}