package com.supermercados.api.exceptions;

public class ProductoNotFoundException extends NotFoundException {
    public ProductoNotFoundException(String message) {
        super(message);
    }
}
