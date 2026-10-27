package org.example.exception;

public class CapacidadExcedidaException extends JuegoException {
    private static final long serialVersionUID = 1L;
    public CapacidadExcedidaException(String mensaje) { super(mensaje); }
    public CapacidadExcedidaException(String mensaje, Throwable causa) { super(mensaje, causa); }
}
