package com.supermercados.api.services;

import com.supermercados.api.exceptions.ConflictException;
import com.supermercados.api.exceptions.ProductoNotFoundException;
import com.supermercados.api.models.Producto;
import com.supermercados.api.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));
    }

    public Producto crear(Long id, Producto p) {
        if (repository.existsByNombreIgnoreCase(p.getNombre())) {
            throw new ConflictException("El producto ya exite (nombre repetido)");
        }
        return repository.save(p);
    }

    public Producto actualizar(Long id, Producto nuevo) {
        Producto existente = obtenerPorId(id);

        // Añadir despues regla de duplicados pero que excluya el propio ID
        if (!existente.getNombre().equalsIgnoreCase(nuevo.getNombre())
                && repository.existsByNombreIgnoreCase(nuevo.getNombre())) {
            throw new ConflictException("Nombre ya usado por otro producto");
        }

        existente.setNombre(nuevo.getNombre());
        existente.setPrecio(nuevo.getPrecio());
        existente.setCategoria(nuevo.getCategoria());
        existente.setStock(nuevo.getStock());
        existente.setActivo(nuevo.getActivo());

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Producto p = obtenerPorId(id);
        repository.delete(p);
    }
}
