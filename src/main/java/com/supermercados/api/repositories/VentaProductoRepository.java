package com.supermercados.api.repositories;

import com.supermercados.api.models.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaProductoRepository extends JpaRepository<VentaDetalle, Long> {

    // aqui - Buscar todos los productos de una venta
    List<VentaDetalle> findByVentaId(Long ventaId);

    //aqui - Buscar todas las ventas donde aparece un producto
    List<VentaDetalle> findByProductoId(Long productoId);

    // aqui - Agrupar por producto y sumar cantidades (para el producto mas vendido)
    @Query("SELECT vp.producto.id, SUM(vp.cantidad) " +
            "FROM VentaDetalle vp " + /// JPQL usa el nombre de la entidad (VentaDetalle), no el nombre de tabla
            "WHERE vp.venta.anulada = false " + /// espacio al final para que no quede falseGROUP
            "GROUP BY vp.producto.id " +
            "ORDER BY SUM(vp.cantidad) DESC")
    List<Object[]> findProductosMasVendidos();

    // aqui - Sumar cantidad vendida de un producto en especifico
    @Query("SELECT COALESCE(SUM(vp.cantidad), 0) " + /// espacio al final por consistencia
            "FROM VentaDetalle vp " + /// JPQL usa el nombre de la entidad
            "WHERE vp.producto.id = :productoId AND vp.venta.anulada = false")
    Integer sumCantidadByProductoId(@Param("productoId") Long productoId);
}