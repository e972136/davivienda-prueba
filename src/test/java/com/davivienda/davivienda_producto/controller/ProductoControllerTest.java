package com.davivienda.davivienda_producto.controller;

import com.davivienda.davivienda_producto.dto.ProductoRequest;
import com.davivienda.davivienda_producto.dto.ProductoResponse;
import com.davivienda.davivienda_producto.service.ProductoService;
import com.davivienda.davivienda_producto.utilities.DaviviendaDuplicateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(value = ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    final String URL_ALL="all/familia/{familia}";
    final String URL_SAVE="/save";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductoService productoServiceService;


    @Test
    @DisplayName(value = "Post Guardar -> Status 200 respuesta exitosa.")
    void testOkGuardarProducto() throws Exception {

        ProductoRequest producto=new ProductoRequest("0001","producto1","desc",new BigDecimal(100),1);
        ProductoResponse response=new ProductoResponse(1,"0001","producto1","desc",new BigDecimal(100),1);

        Mockito.when(productoServiceService.save(any(ProductoRequest.class))).thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();
        String request = mapper.writeValueAsString(producto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_SAVE)//.headers(http)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(request);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(200, result.getResponse().getStatus());

    }

    @Test
    @DisplayName(value = "Post Guardar -> Status 400 Test de BadRequest.")
    void testErrorGuardarProducto() throws Exception {

        ProductoRequest producto=new ProductoRequest("","","desc",new BigDecimal(100),1);
        ProductoResponse response=new ProductoResponse(1,"","","desc",new BigDecimal(100),1);

        Mockito.when(productoServiceService.save(any(ProductoRequest.class))).thenReturn(response);
        ObjectMapper mapper = new ObjectMapper();
        String request = mapper.writeValueAsString(producto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_SAVE)//.headers(http)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(request);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(400, result.getResponse().getStatus());

    }

    @Test
    @DisplayName(value = "Post Guardar -> Status 400 Test de BadRequest.")
    void testErrorGuardarProducto2() throws Exception {

        ProductoRequest producto=new ProductoRequest("0001","name","desc",new BigDecimal(100),1);
        ProductoResponse response=new ProductoResponse(1,"","name","desc",new BigDecimal(100),1);

        Mockito.when(productoServiceService.save(any(ProductoRequest.class))).thenThrow(DaviviendaDuplicateException.class);
        ObjectMapper mapper = new ObjectMapper();
        String request = mapper.writeValueAsString(producto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL_SAVE)//.headers(http)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(request);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertEquals(404, result.getResponse().getStatus());

    }
}
