package org.example.engine;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public final class Estadisticas {
    private final Map<String, Double> valores = new TreeMap<>();
    public void incrementar(String clave) {
        if (clave == null || clave.isBlank()) throw new IllegalArgumentException("Estadistica vacia.");
        if (!valores.containsKey(clave)) valores.put(clave, 0.0);
        valores.put(clave, valores.getOrDefault(clave, 0.0) + 1.0);
    }
    public Map<String, Double> obtener() { return Collections.unmodifiableMap(valores); }
    public void mostrar() {
        for (Map.Entry<String, Double> entry : valores.entrySet())
            System.out.printf("%-25s %8.0f%n", entry.getKey(), entry.getValue());
    }
}
