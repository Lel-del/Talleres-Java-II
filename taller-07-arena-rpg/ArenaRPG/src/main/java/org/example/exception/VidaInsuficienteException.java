package org.example.exception;

public class VidaInsuficienteException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public VidaInsuficienteException(String mensaje) { super(mensaje); }
    public VidaInsuficienteException(String mensaje, Throwable causa) { super(mensaje, causa); }
}
