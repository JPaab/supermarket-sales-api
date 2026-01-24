package com.supermercados.api.services;

import com.supermercados.api.dtos.venta.VentaDetalleRequestDTO;
import com.supermercados.api.dtos.venta.VentaMapper;
import com.supermercados.api.dtos.venta.VentaRequestDTO;
import com.supermercados.api.dtos.venta.VentaResponseDTO;
import com.supermercados.api.exceptions.BadRequestException;
import com.supermercados.api.exceptions.VentaNotFoundException;
import com.supermercados.api.models.Producto;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.models.Venta;
import com.supermercados.api.models.VentaDetalle;
import com.supermercados.api.repositories.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; /// (para calcular total)
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final SucursalService sucursalService;
    private final ProductoService productoService;

    public VentaService(VentaRepository ventaRepository, SucursalService sucursalService, ProductoService productoService) {
        this.ventaRepository = ventaRepository;
        this.sucursalService = sucursalService;
        this.productoService = productoService;
    }

    // POST /api/ventas
    @Transactional
    // (recomendado porque  hace que registrar/anular sea consistente (si falla algo, no deja stock modificado a medias)
    public Venta registrar(VentaRequestDTO dto) {
        if (dto == null) throw new BadRequestException("Datos incorrectos");
        if (dto.getDetalle() == null || dto.getDetalle().isEmpty())
            throw new BadRequestException("La venta debe tener al menos un producto");

        Sucursal sucursal = sucursalService.obtenerPorId(dto.getSucursalId());

        Venta venta = new Venta();
        venta.setSucursal(sucursal);
        venta.setFecha(LocalDate.now());
        venta.setAnulada(false);

        BigDecimal total = BigDecimal.ZERO;

        // Creamos detalles (TODO meter el control del stock)
        for (VentaDetalleRequestDTO item : dto.getDetalle()) {

            Producto p = productoService.obtenerActivoPorId(item.getProductoId());

            // control de stock (mínimo)
            if (item.getCantidad() == null || item.getCantidad() <= 0)
                throw new BadRequestException("La cantidad debe ser mayor a 0");

            productoService.descontarStock(p.getId(), item.getCantidad());

            VentaDetalle vp = new VentaDetalle();
            vp.setVenta(venta);
            vp.setProducto(p);
            vp.setCantidad(item.getCantidad());
            vp.setPrecioUnitario(p.getPrecio());
            venta.getDetalle().add(vp);
            total = total.add(p.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        venta.setTotal(total);

        return ventaRepository.save(venta);
    }

    @Transactional
    public VentaResponseDTO registrarDTO(VentaRequestDTO dto) {
        return VentaMapper.toDTO(registrar(dto));
    }

    // GET /api/ventas?sucursalId=&fecha=
    public List<Venta> buscar(Long sucursalId, LocalDate fecha) {
        if (sucursalId != null && fecha != null)
            return ventaRepository.findBySucursalIdAndFechaAndAnuladaFalse(sucursalId, fecha);
        if (sucursalId != null)
            return ventaRepository.findBySucursalIdAndAnuladaFalse(sucursalId);
        if (fecha != null)
            return ventaRepository.findByFechaAndAnuladaFalse(fecha);
        return ventaRepository.findByAnuladaFalse();
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> buscarDTO(Long sucursalId, LocalDate fecha) {
        return buscar(sucursalId, fecha).stream()
                .map(VentaMapper::toDTO)
                .toList();
    }


    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrada"));
    }

    // DELETE LOGICO /api/ventas/{id}
    @Transactional
    public Venta anular(Long id) {
        Venta v = obtenerPorId(id);
        if (v.isAnulada())
            return v;

        // devolver stock (mínimo)
        if (v.getDetalle() != null) {
            for (VentaDetalle d : v.getDetalle()) {
                if (d == null || d.getProducto() == null || d.getCantidad() == null) continue;
                Producto p = d.getProducto();
                if (p.getStock() == null) p.setStock(0);
                productoService.reponerStock(d.getProducto().getId(), d.getCantidad());
            }
        }

        v.setAnulada(true);
        v.setAnuladaEn(LocalDateTime.now());
        return ventaRepository.save(v);
    }

    @Transactional
    public VentaResponseDTO anularDTO(Long id) {
        return VentaMapper.toDTO(anular(id));
    }
}