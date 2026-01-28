package com.supermercados.api;

import com.supermercados.api.models.Producto;
import com.supermercados.api.repositories.ProductoRepository;
import com.supermercados.api.services.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductoIntegracionTest {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ProductoRepository productoRepository;
    @Test
    void creaProductoSeGuardaYSePuedeBuscarPorId() {
        Producto p = new Producto();
        p.setNombre("ProductoTest-" + System.currentTimeMillis());
        p.setPrecio(new BigDecimal("1.50"));
        p.setCategoria("TEST");
        p.setStock(10);
        p.setActivo(true);
        Producto creado = productoService.crear(p);
        assertNotNull(creado.getId());
        assertTrue(productoRepository.findById(creado.getId()).isPresent());
    }
}

