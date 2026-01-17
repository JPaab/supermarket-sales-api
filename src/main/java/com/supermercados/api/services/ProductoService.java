package com.supermercados.api.services;

import com.supermercados.api.dtos.requests.ProductoRequestDTO;
import com.supermercados.api.dtos.responses.ProductoResponseDTO;
import com.supermercados.api.exceptions.ProductoNotFoundException;
import com.supermercados.api.models.Producto;
import com.supermercados.api.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    public List<ProductoResponseDTO> listar() {
        return productoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        Producto p = Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .categoria(dto.getCategoria())
                .stock(dto.getStock())
                .activo(true)
                .build();
        return toResponse(productoRepository.save(p));
    }

    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto p = productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException(id));
        p.setNombre(dto.getNombre());
        p.setPrecio(dto.getPrecio());
        p.setCategoria(dto.getCategoria());
        p.setStock(dto.getStock());
        p.setActivo(true);
        return toResponse(productoRepository.save(p));
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) throw new ProductoNotFoundException(id);
        productoRepository.deleteById(id);
    }

    private ProductoResponseDTO toResponse(Producto p) {
        return ProductoResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .categoria(p.getCategoria())
                .stock(p.getStock())
                .activo(p.getActivo())
                .build();
    }


}
