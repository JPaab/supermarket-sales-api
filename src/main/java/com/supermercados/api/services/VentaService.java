package com.supermercados.api.services;

import com.supermercados.api.dtos.venta.VentaDetalleRequestDTO;
import com.supermercados.api.dtos.venta.VentaRequestDTO;
import com.supermercados.api.exceptions.BadRequestException;
import com.supermercados.api.exceptions.VentaNotFoundException;
import com.supermercados.api.models.Producto;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.models.Venta;
import com.supermercados.api.models.VentaDetalle;
import com.supermercados.api.repositories.VentaRepository;
import org.springframework.stereotype.Service;

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
    public Venta registrar(VentaRequestDTO dto) {
        if (dto == null) throw new BadRequestException("Datos incorrectos");

        Sucursal sucursal = sucursalService.obtenerPorId(dto.getSucursalId());

        Venta venta = new Venta();
        venta.setSucursal(sucursal);
        venta.setFecha(LocalDate.now());
        venta.setAnulada(false);

        // Creamos detalles (TODO meter el control del stock)

        for (VentaDetalleRequestDTO item : dto.getDetalle()) {
            Producto p = productoService.obtenerPorId(item.getProductoId());

            VentaDetalle vp = new VentaDetalle();
            vp.setVenta(venta);
            vp.setProducto(p);
            vp.setCantidad(item.getCantidad());
        }

        return ventaRepository.save(venta);
    }

    // GET /api/ventas?sucursalId=&fecha=
    public List<Venta> buscar (Long sucursalId, LocalDate fecha) {
        if (sucursalId != null && fecha != null)
            return ventaRepository.findBySucursalIdAndFechaVenta(sucursalId, fecha);
        if (sucursalId != null)
            return ventaRepository.findBySucursalId(sucursalId);
        if (fecha != null)
            return ventaRepository.findByFecha(fecha);
        return ventaRepository.findAll();
    }

    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrada"));
    }

    // DELETE LOGICO /api/ventas/{id}
    public Venta anular(Long id) {
        Venta v = obtenerPorId(id);
        if (v.isAnulada())
            return v;

        v.setAnulada(true);
        v.setAnuladaEn(LocalDateTime.now());
        return ventaRepository.save(v);
    }
}
