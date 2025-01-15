package com.davivienda.davivienda_producto.service;


import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.dto.ProductoResponse;
import com.davivienda.davivienda_producto.entitie.Producto;
import com.davivienda.davivienda_producto.repository.ProductRepository;
import com.davivienda.davivienda_producto.utilities.DaviviendaDuplicateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @InjectMocks
    ProductoServiceImpl productoServiceService;

    @Mock
    ProductRepository productRepository;

    @Test
    @DisplayName(value = "guardar producto test service OK")
    final void guardarProductoOK()  {

        ProductoRequest producto=new ProductoRequest("0001","name","desc",new BigDecimal(100),1);

        Mockito.when(productRepository.findByCodigo(anyString())).thenReturn(Optional.empty());
        Mockito.when(productRepository.save(any() )).thenReturn( new Producto(1,"","name","desc",new BigDecimal(100),1, LocalDate.now(),LocalDate.now()));

        ProductoResponse response=productoServiceService.save(producto);

        Assertions.assertEquals(1,response.id());
    }
    @Test
    @DisplayName(value = "guardar producto test service Error de existente")
    final void guardarProductoFalloExistente()  {

        ProductoRequest producto=new ProductoRequest("0001","name","desc",new BigDecimal(100),1);
        Producto producto1=new Producto(1,"0001","name","desc",new BigDecimal(100),1, LocalDate.now(),LocalDate.now());
        Mockito.when(productRepository.findByCodigo(anyString())).thenReturn(Optional.of(producto1));
        ProductoResponse  response=null;

        try {
            response=productoServiceService.save(producto);
        }catch (DaviviendaDuplicateException e){
              response=new ProductoResponse(0,"","","",new BigDecimal(0),1);
        }

        Assertions.assertEquals(0,response.id());
    }


    @Test
    @DisplayName(value = "getByCodigo test service OK")
    final void getByCodigoOK()  {

        Producto producto1=new Producto(1,"0001","name","desc",new BigDecimal(100),1, LocalDate.now(),LocalDate.now());
        Mockito.when(productRepository.findByCodigo(anyString())).thenReturn(Optional.of(producto1));

        ProductoResponse response=productoServiceService.getByCodigo("001");

        Assertions.assertEquals(1,response.id());
    }
}
