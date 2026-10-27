package org.example.exception;

public class SinManaException extends JuegoException {
    private static final long serialVersionUID = 1L;
    public SinManaException(String mensaje) { super(mensaje); }
    public SinManaException(String mensaje, Throwable causa) { super(mensaje, causa); }
}
