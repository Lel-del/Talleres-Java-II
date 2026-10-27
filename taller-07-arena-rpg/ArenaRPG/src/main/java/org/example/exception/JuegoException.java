package org.example.exception;

public abstract class JuegoException extends Exception {
    private static final long serialVersionUID = 1L;
    public JuegoException(String mensaje) { super(mensaje); }
    public JuegoException(String mensaje, Throwable causa) { super(mensaje, causa); }
}
