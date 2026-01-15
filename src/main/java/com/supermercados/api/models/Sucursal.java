package com.supermercados.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 200)
    private String direccion;

    private Boolean activa = true;

    // aqui estaria tambien la relacion de --> Una sucursal tiene muchas ventas
    @OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL)
    private List<Venta> ventas;


}
