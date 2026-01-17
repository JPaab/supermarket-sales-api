package com.supermercados.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //aqui estara la relacion de --> Cada venta es de UNA sucursal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private Sucursal sucursal;

    private LocalDateTime fechaVenta;

    private BigDecimal total;

    private Boolean activa = false;

    // aqui estara la relacion de --> Una venta TIENE MUCHOS productos ( a traves de VentaProducto)
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VentaProducto> productos;

    // aqui en el @PrePersist lo que hace es que se ejecuta automaticamente antes de que lo guardes en la BD
    // Si no se especifica la fecha manualmente, pone la fecha y hora actual
    @PrePersist
    public void prePersist() {
        if (fechaVenta == null) {
            fechaVenta = LocalDateTime.now();
        }
    }
}
