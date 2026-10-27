package com.taller.poo;

/*
 * Los primitivos almacenan directamente valores y no admiten null. Los Wrappers
 * son referencias a objetos, admiten null y pueden implicar memoria adicional
 * para la referencia, la cabecera y la alineación del objeto. El tamaño concreto
 * depende de la JVM y de si el objeto se comparte; no existe un tamaño universal.
 * Aquí todos los datos numéricos y el estado son conocidos: no necesitamos null.
 */
public class Vehiculo {
    private String marca;
    private String modelo;
    private double kilometraje; // double: cantidad decimal conocida, sin null.
    private int velocidadMaxima; // int: cantidad entera conocida, sin boxing.
    private boolean encendido; // boolean: solo dos estados; no hay estado desconocido.

    public Vehiculo() {
        this("Genérico", "Sin Modelo", 0.0, 120, false);
    }

    public Vehiculo(String marca, String modelo, double kilometraje,
                    int velocidadMaxima, boolean encendido) {
        this.marca = marca;
        this.modelo = modelo;
        this.kilometraje = kilometraje;
        this.velocidadMaxima = velocidadMaxima;
        this.encendido = encendido;
    }

    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public double getKilometraje() { return kilometraje; }
    public int getVelocidadMaxima() { return velocidadMaxima; }
    public boolean isEncendido() { return encendido; }
}
