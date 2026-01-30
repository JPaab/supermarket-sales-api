package com.supermercados.api.services;


import com.supermercados.api.dtos.producto.ProductoEstadisticaDTO;
import com.supermercados.api.dtos.producto.ProductoMapper;
import com.supermercados.api.dtos.producto.ProductoResponseDTO;
import com.supermercados.api.dtos.sucursal.SucursalEstadisticaDTO;
import com.supermercados.api.dtos.sucursal.SucursalMapper;
import com.supermercados.api.exceptions.NotFoundException;
import com.supermercados.api.exceptions.ProductoNotFoundException;
import com.supermercados.api.exceptions.SucursalNotFoundException;
import com.supermercados.api.exceptions.VentaNotFoundException;
import com.supermercados.api.models.Producto;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.repositories.ProductoRepository;
import com.supermercados.api.repositories.SucursalRepository;
import com.supermercados.api.repositories.VentaDetalleRepository;
import com.supermercados.api.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EstadisticasService {
    private final VentaDetalleRepository ventaDetalleRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaRepository ventaRepository;

    public Producto productoMasVendido() {

        List<Object[]> resultado = ventaDetalleRepository.findProductosMasVendidos();

        if (resultado.isEmpty()) {
            throw new NotFoundException("No existen ventas registradas");
        }

        Long productoId = (Long) resultado.get(0)[0];

        return productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new ProductoNotFoundException("Producto no encontrado con id " + productoId));
    }


    public BigDecimal ingresosTotales(Long sucursalId) {
        if (sucursalId == null) return ventaRepository.sumTotalAndAnuladaFalse();
        return ventaRepository.sumTotalBySucursalIdAndAnuladaFalse(sucursalId);
    }

    //top 5
    public List<ProductoResponseDTO> top5ProductosMasVendidos() {

        List<Object[]> resultados =
                ventaDetalleRepository.findProductosMasVendidos(PageRequest.of(0, 5));

        if (resultados.isEmpty()) {
            throw new NotFoundException("No existen ventas registradas");
        }

        return resultados.stream()
                .map(r -> (Long) r[0]) // productoId
                .map(id -> productoRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .map(ProductoMapper::toDTO)
                .toList();
    }

    //por producto
    public ProductoEstadisticaDTO estadisticasProducto(Long productoId) {

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new ProductoNotFoundException(
                                "Producto no encontrado con id " + productoId));

        List<Object[]> resultados = ventaDetalleRepository.estadisticasProducto(productoId);

        if (resultados.isEmpty()) {
            throw new VentaNotFoundException("No hay ventas para este producto");
        }

        Object[] result = resultados.get(0);

        Number cantidad = (Number) result[0];
        Number ingresos = (Number) result[1];

        Integer cantidadVendida = cantidad != null ? cantidad.intValue() : 0;
        BigDecimal ingresosGenerados = ingresos != null ? new BigDecimal(ingresos.toString()) : BigDecimal.ZERO;

        return new ProductoEstadisticaDTO(
                producto.getId(),
                producto.getNombre(),
                cantidadVendida,
                ingresosGenerados
        );
    }

//por sucursal

    public SucursalEstadisticaDTO estadisticasSucursal(Long sucursalId) {

        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() ->
                        new SucursalNotFoundException(
                                "Sucursal no encontrada con id " + sucursalId));


        List<Object[]> resultados = ventaRepository.estadisticasSucursal(sucursalId);
        if (resultados.isEmpty()) {
            return new SucursalEstadisticaDTO(
                    SucursalMapper.toDTO(sucursal),
                    0L,
                    BigDecimal.ZERO
            );
        }

        Object[] result = resultados.get(0);

        Number cantidadVentasNum = (Number) result[0];
        Number ingresosNum = (Number) result[1];

        Long cantidadVentas = cantidadVentasNum != null ? cantidadVentasNum.longValue() : 0L;
        BigDecimal ingresosTotales = ingresosNum != null ? new BigDecimal(ingresosNum.toString()) : BigDecimal.ZERO;

        return new SucursalEstadisticaDTO(
                SucursalMapper.toDTO(sucursal),
                cantidadVentas,
                ingresosTotales
        );
    }

}
