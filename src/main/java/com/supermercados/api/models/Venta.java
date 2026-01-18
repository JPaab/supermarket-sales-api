package com.supermercados.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //aqui estara la relacion de --> Cada venta es de UNA sucursal
    @ManyToOne(optional = false)
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private boolean anulada = false;

    private LocalDateTime anuladaEn;

    // aqui estara la relacion de --> Una venta TIENE MUCHOS productos ( a traves de VentaProducto)
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VentaDetalle> detalle = new ArrayList<>();

    // aqui en el @PrePersist lo que hace es que se ejecuta automaticamente antes de que lo guardes en la BD
    // Si no se especifica la fecha manualmente, pone la fecha y hora actual
    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDate.now();
        }
    }
}
