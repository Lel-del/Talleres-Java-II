package org.example;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Scanner;

public class PruebasRelaciones {
    private static int aprobadas;
    private static void verificar(boolean condicion, String nombre) {
        if (!condicion) throw new AssertionError(nombre);
        aprobadas++;
        System.out.println("OK: " + nombre);
    }
    private static String ejecutarMenu(String acciones) {
        PrintStream original = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(salida));
            new AplicacionArenaRPG().ejecutar(new Scanner(acciones));
        } finally { System.setOut(original); }
        return salida.toString();
    }
    public static void main(String[] args) throws ReflectiveOperationException {
        Guerrero p = new Guerrero("Heroe", 120, 15, 0, 0, 10);
        Mago m = new Mago("Maga", 80, 25, 1, 1, 50);
        verificar(p.getArmaEquipada() == null && p.calcularAtaqueTotal() == 15, "Desarmado: nivel por cinco");
        Arma espada = new Arma("Espada", 7);
        p.equiparArma(espada);
        verificar(p.getArmaEquipada() == espada && p.calcularAtaqueTotal() == 22, "Equipamiento conserva referencia y suma una vez");
        p.atacar(m);
        verificar(m.getVida() == 58, "Arma influye en ataque normal");
        p.realizarAtaqueEspecial(m);
        verificar(m.getVida() == 14, "Furia duplica ataque equipado");
        p.equiparArma(null);
        verificar(p.getArmaEquipada() == espada, "Arma null no reemplaza equipamiento");
        p.desequiparArma(); p.desequiparArma();
        verificar(p.calcularAtaqueTotal() == 15, "Desequipar dos veces es seguro");
        m.equiparArma(espada);
        m.realizarAtaqueEspecial(p);
        verificar(p.getVida() == 88 && m.getPuntosMana() == 40, "Hechizo usa arma, defensa y corazon");
        Gremio a = new Gremio("Sol"), b = new Gremio("Luna");
        p.unirseAGremio(a);
        verificar(p.getGremio() == a && a.getMiembros().contains(p), "Alta desde personaje sincroniza");
        a.agregarMiembro(p); p.unirseAGremio(a); a.agregarMiembro(null); p.unirseAGremio(null);
        verificar(a.getMiembros().size() == 1 && p.getGremio() == a, "Duplicados y null no alteran gremio");
        b.agregarMiembro(p);
        verificar(p.getGremio() == b && b.getMiembros().contains(p) && a.getMiembros().isEmpty(), "Cambio desde gremio retira anterior");
        p.unirseAGremio(a);
        verificar(p.getGremio() == a && b.getMiembros().isEmpty(), "Cambio desde personaje retira anterior");
        b.retirarMiembro(p);
        verificar(p.getGremio() == a && a.getMiembros().contains(p), "Retiro desde gremio ajeno no rompe relacion");
        a.retirarMiembro(p);
        verificar(p.getGremio() == null && a.getMiembros().isEmpty(), "Baja desde gremio sincroniza");
        a.agregarMiembro(p); p.salirDeGremio(); p.salirDeGremio(); a.retirarMiembro(null);
        verificar(p.getGremio() == null && a.getMiembros().isEmpty(), "Baja desde personaje sincroniza sin recursion");
        boolean protegida = false;
        try { a.getMiembros().add(p); } catch (UnsupportedOperationException e) { protegida = true; }
        verificar(protegida && p.getGremio() == null, "Lista gremio protegida");
        a.agregarMiembro(p); p.equiparArma(espada);
        Escuadron escuadron = new Escuadron("Equipo");
        verificar(escuadron.reclutarPersonaje(p) && !escuadron.reclutarPersonaje(p)
            && !escuadron.reclutarPersonaje(null), "Recluta objetos existentes sin null ni duplicados");
        int vida = p.getVida();
        escuadron.expulsarPersonaje(p);
        verificar(p.getVida() == vida && p.getArmaEquipada() == espada && p.getGremio() == a,
            "Expulsion conserva estado, arma y gremio");
        escuadron.reclutarPersonaje(p); escuadron.reclutarPersonaje(m); escuadron.disolver();
        verificar(escuadron.getMiembros().isEmpty() && p.estaVivo() && m.getPuntosMana() == 40,
            "Disolver no destruye personajes ni sus atributos");
        protegida = false;
        try { escuadron.getMiembros().add(p); } catch (UnsupportedOperationException e) { protegida = true; }
        verificar(protegida, "Lista escuadron protegida");
        Field corazon = Personaje.class.getDeclaredField("corazon");
        corazon.setAccessible(true); // Solo inspeccion de prueba, no es una API del juego.
        verificar(Modifier.isPrivate(corazon.getModifiers()) && Modifier.isFinal(corazon.getModifiers())
            && corazon.get(p) != null && corazon.get(p) != corazon.get(m), "Corazon privado final y exclusivo");
        Object original = corazon.get(p);
        escuadron.reclutarPersonaje(p); escuadron.expulsarPersonaje(p);
        verificar(corazon.get(p) == original, "Agregacion conserva identidad del corazon");
        boolean expuesto = false;
        for (var metodo : Personaje.class.getMethods()) {
            if (metodo.getReturnType() == CorazonRPG.class) expuesto = true;
            for (Class<?> tipo : metodo.getParameterTypes()) if (tipo == CorazonRPG.class) expuesto = true;
        }
        verificar(!expuesto, "API publica no expone ni sustituye corazon");
        CorazonRPG interno = (CorazonRPG) corazon.get(p);
        p.recibirDano(20); p.curar(5);
        verificar(p.getVida() == interno.getSaludBase() && p.getVida() == vida - 15,
            "Dano y curacion tienen una sola fuente");
        verificar(m.getPuntosMana() == ((CorazonRPG) corazon.get(m)).getManaBase(), "Mana almacenado solo en corazon");
        String reporte = p.toString() + a + escuadron + m;
        verificar(reporte.contains("Sol") && reporte.contains("Corazon") && reporte.contains("Espada"),
            "Reportes completos sin recursion circular");
        Arena arena = new Arena();
        Personaje heroe = arena.getHeroes().get(1);
        heroe.unirseAGremio(a); heroe.recibirDano(999);
        arena.limpiarBajas();
        verificar(!arena.getHeroes().contains(heroe) && !arena.getEscuadron().getMiembros().contains(heroe)
            && heroe.getGremio() == a && arena.getCelda(1, 1) == '.', "Bajas actualizan escuadron y mapa sin borrar gremio");
        verificar(arena.reclutar(m) && arena.getHeroes().contains(m) && !arena.reclutar(m),
            "Reclutamiento incorpora el mismo personaje al combate sin duplicados");
        arena.actualizarTablero();
        verificar(arena.getCelda(m.getPosicionX(), m.getPosicionY()) == 'M', "Recluta recibe casilla libre");
        Arena lleno = new Arena();
        int cantidad = 0;
        while (lleno.reclutar(new Guerrero("R" + cantidad, 100, 10, 0, 0, 1))) cantidad++;
        verificar(cantidad == 19 && lleno.getHeroes().size() == 21, "Tablero lleno rechaza reclutas sin sobrescribir");
        String gestion = "hola\n99\n1\n\nNova\n2\n0\n3\n2\nSol\n7\n3\n1\n3\n3\n"
            + "4\n3\nBaston\n7\n5\n9\n3\n8\n3\n10\n3\n5\n6\n";
        String salida = ejecutarMenu(gestion);
        verificar(salida.contains("Personaje creado libre") && salida.contains("Gremio actualizado")
            && salida.contains("Personaje reclutado") && salida.contains("equipa Baston")
            && salida.contains("retira su arma") && salida.contains("Personaje sin gremio")
            && salida.contains("Expulsado") && salida.contains("Corazon") && salida.contains("Cerrando"),
            "Menu completo crea, asocia, recluta, reporta y retira");
        salida = ejecutarMenu("7\n1\n6\n");
        verificar(salida.contains("No hay gremios"), "Menu maneja lista de gremios vacia");
        salida = ejecutarMenu("10\n1\n11\n3\n1\n6\n");
        verificar(salida.contains("Recluta a Thorin vivo") && salida.contains("Personaje reclutado"),
            "Principal expulsado bloquea batalla y se puede reclutar otra vez");
        String partida = "1\n1\n1\n1\n1\n1\n1\n1\n2\n1\n1\n1\n1\n1\n1\n1\n1\n3\n1\n2\n3\n1\n0\n3\n1\n3\n3\n2\n0\n3\n1\n4\n3\n2\n1\n3\n0\n4\n3\n2\n2\n";
        salida = ejecutarMenu("11\n" + partida + "5\n11\n6\n");
        verificar(salida.contains("VICTORIA:") && salida.contains("Regreso a gestion")
            && salida.contains("Pociones restantes: 2") && salida.contains("La batalla ya fue iniciada"),
            "Menu integra batalla ganadora, resultado persistente y regreso sin reiniciar");
        salida = ejecutarMenu("4\n1\nEspada poderosa\n100\n11\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n");
        verificar(salida.contains("Orco ha caido") && salida.contains("Guardian ha caido"),
            "Arma equipada por menu influye en combate real");
        salida = ejecutarMenu("1\nNombre\n");
        verificar(!salida.contains("Personaje creado libre"), "Fin de entrada cancela creacion incompleta");
        boolean valida = false;
        try { p.cambiarPosicion(5, 0); } catch (IllegalArgumentException e) { valida = true; }
        verificar(valida, "Posicion publica invalida rechazada");
        new Pocion(10).usar(null);
        verificar(p.getVida() == interno.getSaludBase(), "Pocion null no altera otros personajes");
        System.out.println("RESULTADO: " + aprobadas + " comprobaciones de relaciones aprobadas.");
    }
}

