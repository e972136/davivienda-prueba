package com.davivienda.davivienda_producto.entitie;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="PRODUCTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    int id;

    @Column(name = "CODIGO")
    String codigo;

    @Column(name = "DESCRIPCION")
    String descripcion;

    @Column(name = "FAMILIA")
    String familia;

    @Column(name = "PRECIO")
    BigDecimal precio;

    @Column(name = "EXISTENCIA")
    int existencia;

    @Column(name = "CREATED_AT")
    LocalDate createdAt;

    @Column(name = "UPDATED_AT")
    LocalDate updatedAt;

}
