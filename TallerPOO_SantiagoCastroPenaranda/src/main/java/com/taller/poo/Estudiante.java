package com.taller.poo;

public class Estudiante {
    // El estado se asigna exclusivamente en el constructor.
    private String identificacion;
    private String nombreCompleto;
    private double promedioCalificaciones;
    private boolean estaMatriculado;

    public Estudiante(String identificacion, String nombreCompleto,
                      double promedioCalificaciones, boolean estaMatriculado) {
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.promedioCalificaciones = promedioCalificaciones;
        this.estaMatriculado = estaMatriculado;
    }

    // Conservamos el nombre indicado en el taller. Retorna texto, no lo imprime.
    public String obtenerReporteAcademicos() {
        StringBuilder reporte = new StringBuilder();
        reporte.append(String.format("Identificación: %s | Nombre: %s",
                identificacion, nombreCompleto));
        reporte.append(String.format(" | Promedio: %.2f | Matriculado: %b",
                promedioCalificaciones, estaMatriculado));
        return reporte.toString();
    }

    public String getIdentificacion() { return identificacion; }
    public String getNombreCompleto() { return nombreCompleto; }
    public double getPromedioCalificaciones() { return promedioCalificaciones; }
    public boolean isEstaMatriculado() { return estaMatriculado; }
}
