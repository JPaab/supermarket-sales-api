package com.supermercados.api.services;

import com.supermercados.api.exceptions.ConflictException;
import com.supermercados.api.exceptions.SucursalNotFoundException;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.repositories.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SucursalService {

    private final SucursalRepository repository;

    public SucursalService(SucursalRepository repository) {
        this.repository = repository;
    }

    public List<Sucursal> listar() {
        return repository.findByActivaTrue();
    }

    public Sucursal obtenerPorId(Long id) {
        Sucursal s = repository.findById(id)
                .orElseThrow(() -> new SucursalNotFoundException("Sucursal no encontrada"));

        if (s.getActiva() != null && !s.getActiva()) { /// no devolver sucursal inactiva como si existiera
            throw new SucursalNotFoundException("Sucursal no encontrada");
        }

        return s;
    }


    public Sucursal crear(Sucursal s) {
        if (repository.existsByNombreIgnoreCase(s.getNombre())) {
            throw new ConflictException("La sucursal ya existe (Nombre repetido)");
        }
        return repository.save(s);
    }

    public Sucursal actualizar(Long id, Sucursal nuevo) {
        Sucursal existente = obtenerPorId(id);

        // Añadir despues regla de duplicados pero que excluya el propio ID
        if (!existente.getNombre().equalsIgnoreCase(nuevo.getNombre())
                && repository.existsByNombreIgnoreCase(nuevo.getNombre())) {
            throw new ConflictException("Nombre ya utilizado por otra sucursal");
        }

        existente.setNombre(nuevo.getNombre());
        existente.setDireccion(nuevo.getDireccion());

        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Sucursal s = obtenerPorId(id);
        s.setActiva(false);
        repository.save(s);
    }

    public Sucursal obtenerPorIdIncluyendoInactivas(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SucursalNotFoundException("Sucursal no encontrada"));
    }

    public Sucursal reactivar(Long id) {
        Sucursal s = obtenerPorIdIncluyendoInactivas(id);

        if (s.getActiva() != null && s.getActiva()) {
            throw new ConflictException("La sucursal ya está activa");
        }

        s.setActiva(true);
        return repository.save(s);
    }

    public List<Sucursal> listarTodas(){
        return repository.findAll();
    }
}
