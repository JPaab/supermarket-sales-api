package com.supermercados.api.services;

import com.supermercados.api.dtos.requests.SucursalRequestDTO;
import com.supermercados.api.dtos.responses.SucursalResponseDTO;
import com.supermercados.api.exceptions.ProductoNotFoundException;
import com.supermercados.api.exceptions.SucursalNotFoundException;
import com.supermercados.api.models.Sucursal;
import com.supermercados.api.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SucursalService {
    private final SucursalRepository sucursalRepository;

    public List<SucursalResponseDTO> listar() {
        return sucursalRepository.findAll().stream().map(this::toResponse).toList();
    }

    public SucursalResponseDTO crear(SucursalRequestDTO dto) {
        Sucursal s = Sucursal.builder()
                .nombre(dto.getNombre())
                .direccion(dto.getDireccion())
                .activa(true)
                .build();
        return toResponse(sucursalRepository.save(s));
    }

    public SucursalResponseDTO actualizar(Long id, SucursalRequestDTO dto) {
        Sucursal s = sucursalRepository.findById(id).orElseThrow(() -> new SucursalNotFoundException(id));
        s.setNombre(dto.getNombre());
        s.setDireccion(dto.getDireccion());
        s.setActiva(true);
        return toResponse(sucursalRepository.save(s));
    }

    public void eliminar(Long id) {
        if (!sucursalRepository.existsById(id)) throw new SucursalNotFoundException(id);
        sucursalRepository.deleteById(id);
    }

    public Sucursal getEntity(Long id) {
        return sucursalRepository.findById(id).orElseThrow(() -> new SucursalNotFoundException(id));
    }

    private SucursalResponseDTO toResponse(Sucursal s) {
        return SucursalResponseDTO.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .direccion(s.getDireccion())
                .activa(s.getActiva())
                .build();
    }
}
