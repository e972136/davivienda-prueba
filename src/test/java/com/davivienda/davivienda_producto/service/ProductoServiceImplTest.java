package com.davivienda.davivienda_producto.service;

import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.dto.ProductoResponse;
import com.davivienda.davivienda_producto.entitie.Producto;
import com.davivienda.davivienda_producto.repository.ProductRepository;
import com.davivienda.davivienda_producto.utilities.DaviviendaDuplicateException;
import com.davivienda.davivienda_producto.utilities.DaviviendaInsuficienteException;
import com.davivienda.davivienda_producto.utilities.DaviviendaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class ProductoServiceImplTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductoServiceImpl candidatoImp;

    @BeforeEach
    void setUp() {
        ProductoServiceImpl candidatoImp = new ProductoServiceImpl(productRepository);
    }

    @Test
    void findAll() {
        int size = candidatoImp.findAll().size();
        assertThat(size).isEqualTo(11);
    }

    @Test
    void findAllPage() {
        Page<Producto> allPage = candidatoImp.findAllPage(1, 5);
        int size = allPage.getContent().size();
        assertThat(size).isEqualTo(5);
    }

    @Test
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
    void findAllByFamilia() {
        List<Producto> motor = candidatoImp.findAllByFamilia("ACEITE");
        assertThat(motor).hasSize(2);
    }

    @Test
    void findAllDisponibles() {
        int size = candidatoImp.findAllDisponibles().size();
        assertThat(size).isEqualTo(6);
    }

    @Test
    void ventaProducto() {
        ProductoResponse byCodigo = candidatoImp.getByCodigo("1001");
        Producto producto = candidatoImp.ventaProducto(byCodigo.codigo(), 1);
        assertThat(producto.getExistencia()).isEqualTo(byCodigo.existencia()-1);
    }

    @Test
    void ventaProducto_noExistente() {
        assertThatThrownBy(()->{
            candidatoImp.ventaProducto("5000", 1);
        }).isInstanceOf(DaviviendaNotFoundException.class);
    }

    @Test
    void ventaProducto_insuficiente() {
        assertThatThrownBy(()->{
            candidatoImp.ventaProducto("1010", 100);
        }).isInstanceOf(DaviviendaInsuficienteException.class);
    }



    @Test
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
    void getByCodigo() {
        ProductoResponse byCodigo = candidatoImp.getByCodigo("1001");
        assertThat(byCodigo.codigo()).isEqualTo("1001");
    }

    @Test
    void getByCodigo_noEncontrado() {

        assertThatThrownBy(()->{
            candidatoImp.getByCodigo("9999");
        }).isInstanceOf(DaviviendaNotFoundException.class);
    }

    @Test
    void updatePrecioProducto() {
        ProductoResponse byCodigo = candidatoImp.getByCodigo("1002");
        candidatoImp.updatePrecioProducto(byCodigo.id(), BigDecimal.TEN);
        byCodigo = candidatoImp.getByCodigo("1002");
        assertThat(byCodigo.precio().intValue()).isEqualTo(BigDecimal.TEN.intValue());
    }
    @Test
    void updatePrecioProducto_noEcontrado() {
        assertThatThrownBy(()->{
            candidatoImp.updatePrecioProducto(9999, BigDecimal.TEN);
        }).isInstanceOf(DaviviendaNotFoundException.class);
    }

    @Test
    void deleteProduct() {
        List<Producto> all = candidatoImp.findAll();
        int sizeOriginal = all.size();
        String s = candidatoImp.deleteProduct("1010");
        List<Producto> despues = candidatoImp.findAll();
        assertThat(sizeOriginal-1).isEqualTo(despues.size());
    }

    @Test
    void deleteProduct_noEcontrado() {
        assertThatThrownBy(()->{
            candidatoImp.deleteProduct("9999");
        }).isInstanceOf(DaviviendaNotFoundException.class);


    }
}