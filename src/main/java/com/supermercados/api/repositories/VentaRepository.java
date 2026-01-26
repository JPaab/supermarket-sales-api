package com.supermercados.api.repositories;

import com.supermercados.api.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // aqui - Buscar ventas por sucursal
    List<Venta> findBySucursalId(Long sucursalId);

    List<Venta> findByFecha(LocalDate fecha);

    // aqui - Buscar ventas por sucursal y fecha
    List<Venta> findBySucursalIdAndFecha(Long sucursalId,
                                         LocalDate fecha);

    List<Venta> findByFechaAndAnuladaFalse(LocalDate fecha);

    List<Venta> findBySucursalIdAndFechaAndAnuladaFalse(Long sucursalId, LocalDate fecha);

    // aqui - Buscar ventas por fecha (sin que importa la sucursal)
    List<Venta> findByAnuladaFalse();

    // aqui - Buscar ventas por sucursal no anuladas
    List<Venta> findBySucursalIdAndAnuladaFalse(Long sucursalId);

    // aqui - Contar ventas por sucursal
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.sucursal.id = :sucursalId AND v.anulada = false")
    long countBySucursalIdAndAnuladaFalse(@Param("sucursalId") Long sucursalId);

    // aqui - La suma de los totales por sucursal
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.sucursal.id = :sucursalId AND v.anulada = false")
    BigDecimal sumTotalBySucursalIdAndAnuladaFalse(@Param("sucursalId") Long sucursalId); /// aqui otro cambio sumTotalBySucursalIdAndAnuladaFalse ahora devuelve BigDecimal
    ///(para que sea coherente  con Venta.total)

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.anulada = false")
    BigDecimal sumTotalAndAnuladaFalse();

    /*Añadir esta query
    *@Query("
    SELECT COUNT(v.id), COALESCE(SUM(v.total),0)
    FROM Venta v
    WHERE v.sucursal.id = :sucursalId AND v.anulada = false
")
Object[] estadisticasSucursal(@Param("sucursalId") Long sucursalId);
    *  */
}
