package com.supermercados.api.services;

import com.supermercados.api.dtos.producto.ProductoEstadisticaDTO;
import com.supermercados.api.dtos.producto.ProductoMapper;
import com.supermercados.api.dtos.producto.ProductoResponseDTO;
import com.supermercados.api.dtos.sucursal.SucursalEstadisticaDTO;
import com.supermercados.api.exceptions.ProductoNotFoundException;
import com.supermercados.api.models.Producto;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.repositories.ProductoRepository;
import com.supermercados.api.repositories.VentaDetalleRepository;
import com.supermercados.api.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadisticasService {
    private final VentaDetalleRepository ventaDetalleRepository;
    private final ProductoRepository productoRepository;


    public Producto productoMasVendido() {

        List<Object[]> resultado = ventaDetalleRepository.findProductosMasVendidos();

        if (resultado.isEmpty()) {
            throw new RuntimeException("No existen ventas registradas");
        }

        Long productoId = (Long) resultado.get(0)[0];

        return productoRepository.findById(productoId)
                .orElseThrow(() ->
                        new ProductoNotFoundException("Producto no encontrado con id " + productoId));
    }

    private final VentaRepository ventaRepository;

    public BigDecimal ingresosTotales(Long sucursalId) {
        if (sucursalId == null) return ventaRepository.sumTotalAndAnuladaFalse();
        return ventaRepository.sumTotalBySucursalIdAndAnuladaFalse(sucursalId);
    }

//top 5
    public List<ProductoResponseDTO> top5ProductosMasVendidos() {

        List<Object[]> resultados =
                ventaDetalleRepository.findProductosMasVendidos(PageRequest.of(0, 5));

        if (resultados.isEmpty()) {
            throw new RuntimeException("No existen ventas registradas");
        }

        return resultados.stream()
                .map(r -> (Long) r[0]) // productoId
                .map(id -> productoRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductoNotFoundException(
                                        "Producto no encontrado con id " + id)))
                .map(ProductoMapper::toDTO)
                .toList();
    }
//por producto
public ProductoEstadisticaDTO estadisticasProducto(Long productoId) {

    Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() ->
                    new ProductoNotFoundException(
                            "Producto no encontrado con id " + productoId));

    Object[] result = ventaDetalleRepository.estadisticasProducto(productoId);

    Integer cantidadVendida = (Integer) result[0];
    BigDecimal ingresosGenerados = (BigDecimal) result[1];

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
                        new RuntimeException(
                                "Sucursal no encontrada con id " + sucursalId));

        Object[] result = ventaRepository.estadisticasSucursal(sucursalId);

        Long cantidadVentas = (Long) result[0];
        BigDecimal ingresosTotales = (BigDecimal) result[1];

        return new SucursalEstadisticaDTO(
                SucursalMapper.toDTO(sucursal),
                cantidadVentas,
                ingresosTotales
        );
    }

}
