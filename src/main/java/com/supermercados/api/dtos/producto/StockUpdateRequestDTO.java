package com.supermercados.api.dtos.producto;

public class StockUpdateRequestDTO {
    private Integer cantidad;
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
