package com.supermercados.api.services;

import com.supermercados.api.exceptions.ProductoNotFoundException;
import com.supermercados.api.models.Producto;
import com.supermercados.api.repositories.ProductoRepository;
import com.supermercados.api.repositories.VentaDetalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
