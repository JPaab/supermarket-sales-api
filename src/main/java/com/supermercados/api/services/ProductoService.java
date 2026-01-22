package com.supermercados.api.services;

import com.supermercados.api.exceptions.BadRequestException;
import com.supermercados.api.exceptions.ConflictException;
import com.supermercados.api.exceptions.ProductoNotFoundException;
import com.supermercados.api.models.Producto;
import com.supermercados.api.repositories.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findByActivoTrue();
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));
    }

    public Producto obtenerActivoPorId(Long id) {
        Producto p = obtenerPorId(id);
        if (p.getActivo() != null && !p.getActivo()) {
            throw new ProductoNotFoundException("Producto no encontrado");
        }
        return p;
    }

    public Producto crear(Producto p) {
        if (repository.existsByNombreIgnoreCase(p.getNombre())) {
            throw new ConflictException("El producto ya existe (nombre repetido)");
        }
        if (p.getStock() == null) p.setStock(0);
        return repository.save(p);
    }

    public Producto actualizar(Long id, Producto nuevo) {
        Producto existente = obtenerPorId(id);

        if (!existente.getNombre().equalsIgnoreCase(nuevo.getNombre())
                && repository.existsByNombreIgnoreCase(nuevo.getNombre())) {
            throw new ConflictException("nombre ya utilizado por otro producto");
        }

        existente.setNombre(nuevo.getNombre());
        existente.setPrecio(nuevo.getPrecio());
        existente.setCategoria(nuevo.getCategoria());
        existente.setStock(nuevo.getStock() == null ? 0 : nuevo.getStock());

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Producto p = obtenerPorId(id);
        p.setActivo(false);
        repository.save(p);
    }

    // =========================
    // INVENTARIO (extra)
    // =========================

    public Producto descontarStock(Long productoId, int cantidad) {
        if (cantidad <= 0) throw new BadRequestException("cantidad invalida");

        Producto p = obtenerActivoPorId(productoId);
        if (p.getStock() == null) p.setStock(0);

        if (p.getStock() < cantidad) {
            throw new BadRequestException("Stock insuficiente para el producto: " + p.getNombre());
        }

        p.setStock(p.getStock() - cantidad);
        return repository.save(p);
    }

    public Producto reponerStock(Long productoId, int cantidad) {
        if (cantidad <= 0) throw new BadRequestException("cantidad invalida");

        Producto p = obtenerPorId(productoId);
        if (p.getActivo() != null && !p.getActivo()) {
            throw new BadRequestException("No se puede reponer stock a un producto inactivo: " + p.getNombre());
        }
        if (p.getStock() == null) p.setStock(0);

        p.setStock(p.getStock() + cantidad);
        return repository.save(p);
    }
}
