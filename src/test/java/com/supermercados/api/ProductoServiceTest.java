package com.supermercados.api;
import com.supermercados.api.exceptions.BadRequestException;
import com.supermercados.api.exceptions.ConflictException;
import com.supermercados.api.exceptions.ProductoNotFoundException;
import com.supermercados.api.models.Producto;
import com.supermercados.api.repositories.ProductoRepository;
import com.supermercados.api.services.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // aqui - habilita mocks con Mockito
class siProductoServiceTest {

    @Mock // aqui - mock del repositorio (no toca base de datos)
    private ProductoRepository productoRepository;

    @InjectMocks // aqui - crea ProductoService inyectando el mock del repo
    private ProductoService productoService;

    private Producto productoActivo;

    @BeforeEach
    void setUp() {
        // aqui - armamos un producto base activo para reutilizar
        productoActivo = new Producto();
        productoActivo.setId(1L);
        productoActivo.setNombre("Coca-Cola");
        productoActivo.setPrecio(new BigDecimal("2.50"));
        productoActivo.setStock(10);
        productoActivo.setActivo(true);
    }

    @Test
    void descontarStockConStockSuficienteDeberiaDescontarYGuardar() {
        // aqui - Given: el repo encuentra el producto y save devuelve el mismo objeto
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoActivo));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        // aqui - When: descontamos 3 unidades
        Producto actualizado = productoService.descontarStock(1L, 3);

        // aqui - Then: el stock queda en 7 y se llamo a save
        assertEquals(7, actualizado.getStock());
        verify(productoRepository).save(productoActivo);
    }

    @Test
    void crearConNombreRepetidoDeberiaLanzarConflictException() {
        // aqui - Given: existe un producto con el mismo nombre (case insensitive)
        when(productoRepository.existsByNombreIgnoreCase("Coca-Cola")).thenReturn(true);

        // aqui - When y Then: crear debe fallar por nombre repetido
        assertThrows(ConflictException.class, () -> productoService.crear(productoActivo));

        // aqui - Then: no debe guardar nada
        verify(productoRepository, never()).save(any());
    }

    @Test
    void obtenerPorIdInexistenteDeberiaLanzarProductoNotFoundException() {
        // aqui - Given: el repo no encuentra el producto
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        // aqui - When y Then: debe lanzar excepcion
        assertThrows(ProductoNotFoundException.class, () -> productoService.obtenerPorId(999L));
    }

    @Test
    void reactivarSiYaEstaActivoDeberiaLanzarConflictException() {
        // aqui - Given: el producto esta activo
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoActivo));

        // aqui - When y Then: reactivar un activo debe fallar
        assertThrows(ConflictException.class, () -> productoService.reactivar(1L));

        // aqui - Then: no debe guardar cambios
        verify(productoRepository, never()).save(any());
    }

    @Test
    void reponerStockProductoInactivoDeberiaLanzarBadRequestException() {
        // aqui - Given: el producto existe pero esta inactivo
        productoActivo.setActivo(false);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoActivo));

        // aqui - When y Then: no se puede reponer stock a inactivo
        assertThrows(BadRequestException.class, () -> productoService.reponerStock(1L, 5));

        // aqui - Then: no debe guardar cambios
        verify(productoRepository, never()).save(any());
    }
}