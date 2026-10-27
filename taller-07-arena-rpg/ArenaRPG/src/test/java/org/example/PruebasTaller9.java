package org.example;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.example.engine.Bestiario;
import org.example.engine.Estadisticas;
import org.example.exception.CapacidadExcedidaException;
import org.example.exception.JuegoException;
import org.example.exception.SinManaException;
import org.example.exception.VidaInsuficienteException;
import org.example.model.enums.EstadoPersonaje;
import org.example.model.enums.RaridadItem;
import org.example.model.enums.TipoPersonaje;

public class PruebasTaller9 {
    private static int aprobadas;
    private static void verificar(boolean condicion, String nombre) {
        if (!condicion) throw new AssertionError(nombre);
        aprobadas++;
        System.out.println("OK: " + nombre);
    }
    private static Guerrero guerrero() {
        return new Guerrero("Guerrero", 120, 15, 0, 0, 10, TipoPersonaje.GUERRERO);
    }
    private static Mago mago() { return new Mago("Mago", 80, 25, 1, 1, 50, TipoPersonaje.MAGO); }
    private static String menu(String acciones) {
        PrintStream salidaOriginal = System.out, errorOriginal = System.err;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(salida)); System.setErr(new PrintStream(salida));
            new AplicacionArenaRPG().ejecutar(new Scanner(acciones));
        } finally { System.setOut(salidaOriginal); System.setErr(errorOriginal); }
        return salida.toString();
    }
    public static void main(String[] args) throws Exception {
        Guerrero g = guerrero(); Mago m = mago();
        verificar(g.getTipoPersonaje() == TipoPersonaje.GUERRERO && g.getEstadoPersonaje() == EstadoPersonaje.VIVO,
            "Constructor exige tipo y estado inicial VIVO");
        boolean rechazo = false;
        try { new Guerrero("Error", 100, 10, 0, 0, 5, TipoPersonaje.MAGO); }
        catch (IllegalArgumentException e) { rechazo = true; }
        verificar(rechazo, "Tipo incompatible rechazado");
        verificar(g.calcularAtaqueTotal() == 16 && m.calcularAtaqueTotal() == 27,
            "Bonificadores fijos de ataque aplicados");
        g.defender(20);
        verificar(g.getVida() == 111, "Defensa de tipo y escudo se suman una vez");
        Arquero a = new Arquero("Arquero", 100, 15, 0, 1, TipoPersonaje.ARQUERO);
        verificar(a.calcularAtaqueTotal() == 17 && a.getDefensaTipo() == 2, "Arquero redondea bonificadores 1.5 a 2");
        a.realizarAtaqueEspecial(m);
        verificar(m.getVida() == 58, "Especial del arquero aplica total mas cinco");
        Arma rara = new Arma("Arco", 7, RaridadItem.RARO);
        Arma legendaria = new Arma("Espada", 7, RaridadItem.LEGENDARIO);
        verificar(rara.getDanioBase() == 7 && rara.getDanioEfectivo() == 11 && legendaria.getDanioEfectivo() == 18,
            "Rareza multiplica dano base sin alterarlo");
        g.equiparArma(rara);
        verificar(g.calcularAtaqueTotal() == 27 && g.calcularAtaqueTotal() == 27,
            "Consultas repetidas no acumulan rareza ni bonificador");
        Pocion pocion = new Pocion(7, RaridadItem.RARO);
        g.recibirDano(20); int antes = g.getVida(); pocion.usar(g);
        verificar(pocion.getCuracionBase() == 7 && pocion.getCuracion() == 11 && g.getVida() == antes + 11,
            "Pocion rara cura su valor efectivo una sola vez");
        verificar(new Pocion(10, RaridadItem.LEGENDARIO).getCuracion() == 25,
            "Pocion legendaria utiliza factor 2.5");
        verificar(g.aprenderHabilidad(" Bola   de Fuego ") && !g.aprenderHabilidad("bola de fuego")
            && g.obtenerHabilidades().size() == 1, "HashSet rechaza duplicado normalizando espacios y mayusculas");
        for (int i = 0; i < 4; i++) g.aprenderHabilidad("H" + i);
        verificar(!g.aprenderHabilidad("H0"), "Duplicado en libro lleno retorna false");
        rechazo = false;
        try { g.aprenderHabilidad("Sexta"); } catch (CapacidadExcedidaException e) { rechazo = true; }
        verificar(rechazo && g.obtenerHabilidades().size() == 5, "Libro lleno lanza checked sin insertar");
        rechazo = false;
        try { g.aprenderHabilidad(null); } catch (IllegalArgumentException e) { rechazo = true; }
        verificar(rechazo && g.obtenerHabilidades().size() == 5, "Habilidad null rechazada sin efectos");
        rechazo = false;
        try { g.obtenerHabilidades().clear(); } catch (UnsupportedOperationException e) { rechazo = true; }
        verificar(rechazo, "Vista de habilidades protegida");
        PerfilJugador perfil = new PerfilJugador();
        perfil.registrarLogro("Zorro"); perfil.registrarLogro("Arena"); perfil.registrarLogro("Mago");
        verificar(!perfil.registrarLogro("arena") && new ArrayList<>(perfil.listarLogrosOrdenados())
            .equals(List.of("Arena", "Mago", "Zorro")), "TreeSet unico y orden natural");
        rechazo = false;
        try { perfil.listarLogrosOrdenados().add("Trampa"); } catch (UnsupportedOperationException e) { rechazo = true; }
        verificar(rechazo, "Vista de logros protegida");
        Bestiario bestiario = new Bestiario(perfil);
        verificar(bestiario.consultar("Orco") == 0, "getOrDefault devuelve cero para enemigo desconocido");
        bestiario.registrarVictoria(" Orco "); bestiario.registrarVictoria("ORCO");
        verificar(bestiario.consultar("orco") == 2 && perfil.listarLogrosOrdenados().size() == 4,
            "HashMap incrementa y registra un solo logro automatico por enemigo");
        rechazo = false;
        try { bestiario.registrarVictoria(null); } catch (IllegalArgumentException e) { rechazo = true; }
        verificar(rechazo && bestiario.obtenerDerrotas().size() == 1, "Mapa rechaza clave null sin cambios");
        rechazo = false;
        try { bestiario.obtenerDerrotas().put("Trampa", 10); } catch (UnsupportedOperationException e) { rechazo = true; }
        verificar(rechazo, "Bestiario protegido de escrituras externas");
        Estadisticas estadisticas = new Estadisticas();
        estadisticas.incrementar("Z"); estadisticas.incrementar("A"); estadisticas.incrementar("Z");
        verificar(new ArrayList<>(estadisticas.obtener().keySet()).equals(List.of("A", "Z"))
            && estadisticas.obtener().get("Z") == 2.0, "TreeMap ordena claves y actualiza contadores double");
        Inventario<Pocion> inventario = new Inventario<>(1); inventario.agregar(pocion);
        rechazo = false;
        try { inventario.agregar(new Pocion(10)); } catch (CapacidadExcedidaException e) { rechazo = true; }
        verificar(rechazo && inventario.obtener(0) == pocion && inventario.getItems().size() == 1,
            "Inventario lleno lanza checked sin sustitucion parcial");
        rechazo = false;
        try { inventario.getItems().add(pocion); } catch (UnsupportedOperationException e) { rechazo = true; }
        verificar(rechazo, "Vista del inventario no permite saltarse capacidad");
        inventario.retirar(0); inventario.agregar(pocion);
        verificar(inventario.getItems().size() == 1, "Retirar libera capacidad");
        Mago lanzador = mago(); Guerrero objetivo = guerrero();
        int vidaObjetivo = objetivo.getVida();
        rechazo = false;
        try { lanzador.lanzarHechizo(objetivo, 51); } catch (SinManaException e) { rechazo = true; }
        verificar(rechazo && lanzador.getPuntosMana() == 50 && objetivo.getVida() == vidaObjetivo,
            "Sin mana no consume recursos ni causa dano");
        lanzador.lanzarHechizo(objetivo, 50);
        verificar(lanzador.getPuntosMana() == 0 && objetivo.getVida() == vidaObjetivo - 26,
            "Costo exactamente igual al mana disponible permitido");
        rechazo = false; vidaObjetivo = objetivo.getVida();
        try { lanzador.lanzarHechizo(objetivo, -1); } catch (IllegalArgumentException e) { rechazo = true; }
        verificar(rechazo && objetivo.getVida() == vidaObjetivo && lanzador.getPuntosMana() == 0,
            "Costo de mana negativo rechazado sin cambios");
        rechazo = false;
        try { lanzador.lanzarHechizo(null, 0); } catch (IllegalArgumentException e) { rechazo = true; }
        verificar(rechazo && lanzador.getPuntosMana() == 0, "Objetivo invalido no consume mana");
        rechazo = false; int vida = g.getVida();
        try { g.realizarRitual(vida); } catch (VidaInsuficienteException e) { rechazo = true; }
        verificar(rechazo && g.getVida() == vida, "Costo igual a vida lanza unchecked sin consumir");
        rechazo = false;
        try { g.realizarRitual(vida + 1); } catch (VidaInsuficienteException e) { rechazo = true; }
        verificar(rechazo && g.getVida() == vida, "Costo mayor a vida lanza unchecked sin consumir");
        rechazo = false;
        try { g.realizarRitual(-1); } catch (IllegalArgumentException e) { rechazo = true; }
        verificar(rechazo && g.getVida() == vida, "Costo de vida negativo rechazado");
        g.realizarRitual(vida - 1);
        verificar(g.getVida() == 1 && g.estaVivo(), "Ritual puede dejar exactamente uno de vida");
        Exception causa = new Exception("origen");
        verificar(new SinManaException("mana", causa).getCause() == causa
            && new CapacidadExcedidaException("capacidad", causa).getCause() == causa
            && new VidaInsuficienteException("vida", causa).getCause() == causa,
            "Constructores preservan causa raiz");
        verificar(JuegoException.class.isAssignableFrom(SinManaException.class)
            && JuegoException.class.isAssignableFrom(CapacidadExcedidaException.class)
            && RuntimeException.class.isAssignableFrom(VidaInsuficienteException.class), "Jerarquias checked y unchecked correctas");
        Guerrero envenenado = guerrero(); envenenado.aplicarEstado(EstadoPersonaje.ENVENENADO);
        verificar(envenenado.estaVivo() && envenenado.actualizarTurno() && envenenado.getVida() == 115
            && envenenado.getEstadoPersonaje() == EstadoPersonaje.ENVENENADO, "Veneno resta cinco y permite actuar");
        envenenado.actualizarTurno(); envenenado.actualizarTurno();
        verificar(envenenado.getVida() == 110 && envenenado.getEstadoPersonaje() == EstadoPersonaje.VIVO,
            "Veneno dura dos turnos y no se reaplica al expirar");
        envenenado.aplicarEstado(EstadoPersonaje.ATURDIDO);
        rechazo = false;
        try { envenenado.atacar(objetivo); } catch (IllegalStateException e) { rechazo = true; }
        verificar(rechazo && !envenenado.actualizarTurno() && envenenado.actualizarTurno(),
            "Aturdido no ataca, pierde un turno y recupera accion");
        envenenado.recibirDano(109); envenenado.aplicarEstado(EstadoPersonaje.ENVENENADO);
        verificar(!envenenado.actualizarTurno() && envenenado.getVida() == 0
            && envenenado.getEstadoPersonaje() == EstadoPersonaje.DERROTADO, "Veneno letal sincroniza estado y vida");
        envenenado.curar(100);
        verificar(!envenenado.estaVivo() && envenenado.getEstadoPersonaje() == EstadoPersonaje.DERROTADO,
            "Pociones y curacion no resucitan derrotados");
        rechazo = false;
        try { envenenado.aplicarEstado(EstadoPersonaje.VIVO); } catch (IllegalStateException e) { rechazo = true; }
        verificar(rechazo, "Cambio de estado no resucita");
        rechazo = false;
        try { mago().aplicarEstado(EstadoPersonaje.DERROTADO); } catch (IllegalArgumentException e) { rechazo = true; }
        verificar(rechazo, "No se marca derrotado a un personaje con vida");
        Arena arena = new Arena();
        Personaje orco = arena.getEnemigos().get(0); orco.recibirDano(999);
        arena.actualizarTablero(); arena.actualizarTablero(); arena.limpiarBajas(); arena.limpiarBajas();
        verificar(arena.getBestiario().consultar("Orco") == 1 && arena.getPerfil().listarLogrosOrdenados().size() == 1
            && arena.getEstadisticas().obtener().get("Enemigos derrotados") == 1.0,
            "Baja real contada una sola vez pese a multiples actualizaciones");
        arena.getBestiario().registrarVictoria("Orco");
        verificar(arena.getEnemigos().size() == 2 && !arena.hayVictoria(),
            "Registro manual no elimina enemigos ni gana la batalla");
        arena.reclutar(a); arena.actualizarTablero();
        verificar(arena.getCelda(a.getPosicionX(), a.getPosicionY()) == 'A', "Arquero se representa con A en tablero");
        Arena venenoLetal = new Arena();
        venenoLetal.getHeroes().get(0).recibirDano(119);
        venenoLetal.getHeroes().get(0).aplicarEstado(EstadoPersonaje.ENVENENADO);
        PrintStream out = System.out; ByteArrayOutputStream registro = new ByteArrayOutputStream();
        try { System.setOut(new PrintStream(registro)); venenoLetal.jugar(new Scanner("")); }
        finally { System.setOut(out); }
        verificar(registro.toString().contains("DERROTA:") && venenoLetal.getCelda(0, 0) == '.',
            "Veneno se integra en bucle real y puede provocar derrota");
        String salida = menu("1\nRobin\n3\n3\n3\n3\n5\n6\n");
        verificar(salida.contains("ARQUERO") && salida.contains("Robin") && salida.contains("Personaje reclutado"),
            "Menu crea arquero desde values del enum y lo recluta");
        salida = menu("12\n1\nFuego\n12\n1\nfuego\n14\n6\n");
        verificar(salida.contains("Habilidad aprendida") && salida.contains("ya esta aprendida") && salida.contains("Cerrando"),
            "Menu informa duplicado y continua");
        StringBuilder libroLleno = new StringBuilder();
        for (int i = 0; i < 6; i++) libroLleno.append("12\n1\nH").append(i).append("\n");
        salida = menu(libroLleno + "5\n6\n");
        verificar(salida.contains("Libro lleno") && salida.contains("Cerrando"), "Menu captura capacidad del libro y continua");
        salida = menu("16\n10\n1\n".repeat(4) + "5\n6\n");
        verificar(salida.contains("Inventario lleno") && salida.contains("Pociones restantes: 6"),
            "Menu captura inventario lleno sin exceder capacidad");
        salida = menu("15\n2\n1\n51\n5\n6\n");
        verificar(salida.contains("Mana insuficiente") && salida.contains("Cerrando") && salida.contains("50"),
            "Menu captura SinManaException y conserva recursos");
        salida = menu("17\n1\n120\n5\n6\n");
        verificar(salida.contains("al menos 1 de vida") && salida.contains("Cerrando"),
            "Menu captura VidaInsuficienteException y continua");
        salida = menu("13\nDragon\n13\ndragon\n14\n6\n");
        verificar(salida.contains("Registro manual guardado") && salida.contains("Primera victoria: dragon")
            && salida.contains("Victorias manuales"), "Menu presenta HashMap, logros y TreeMap");
        salida = menu("18\n1\n3\n11\n");
        verificar(salida.contains("pierde su turno por aturdimiento"), "Menu aplica estado y combate omite turno real");
        salida = menu("abc\n9999999999999999999\n1.5\n5 basura\n\n5\n6\n");
        verificar(salida.contains("numero entero valido") && salida.contains("Corazon") && salida.contains("Cerrando"),
            "InputMismatch texto overflow decimal linea mixta y vacia recuperan lectura");
        verificar(menu("").contains("MENU"), "Fin de entrada inicial no crea bucle infinito");
        salida = menu("1\nRobin\n3\n");
        verificar(!salida.contains("Personaje creado libre"), "EOF cancela creacion incompleta sin insertar");
        salida = menu("4\n1\nEspada\n10\n");
        verificar(!salida.contains("equipa Espada"), "EOF en rareza no equipa parcialmente");
        Arena sinManaArena = new Arena();
        Personaje magoArena = sinManaArena.getHeroes().get(1);
        magoArena.lanzarHechizo(guerrero(), 50);
        PrintStream error = System.err;
        registro.reset();
        try {
            System.setOut(new PrintStream(registro)); System.setErr(new PrintStream(registro));
            sinManaArena.jugar(new Scanner("1\n1\n1\n1\n"));
        } finally { System.setOut(out); System.setErr(error); }
        verificar(registro.toString().contains("Mana insuficiente")
            && registro.toString().contains("controlador elige un ataque basico") && magoArena.getPuntosMana() == 0,
            "Controlador captura fallo real de mana y ejecuta fallback explicito");
        Arena victoriaVeneno = new Arena();
        victoriaVeneno.getHeroes().get(0).cambiarPosicion(2, 2);
        for (Personaje enemigo : victoriaVeneno.getEnemigos()) {
            enemigo.recibirDano(enemigo.getVida() - 1);
            enemigo.aplicarEstado(EstadoPersonaje.ENVENENADO);
        }
        registro.reset();
        try { System.setOut(new PrintStream(registro)); victoriaVeneno.jugar(new Scanner("2\n2\n")); }
        finally { System.setOut(out); }
        boolean contadoresUnicos = true;
        for (Integer contador : victoriaVeneno.getBestiario().obtenerDerrotas().values())
            if (contador != 1) contadoresUnicos = false;
        verificar(registro.toString().contains("VICTORIA:") && victoriaVeneno.getBestiario().obtenerDerrotas().size() == 3
            && contadoresUnicos
            && victoriaVeneno.getPerfil().listarLogrosOrdenados().contains("Arena conquistada"),
            "Veneno enemigo registra bajas reales, logro final y victoria");
        int logrosAntes = victoriaVeneno.getPerfil().listarLogrosOrdenados().size();
        victoriaVeneno.jugar(new Scanner(""));
        verificar(victoriaVeneno.getEstadisticas().obtener().get("Victorias de arena") == 1.0
            && victoriaVeneno.getPerfil().listarLogrosOrdenados().size() == logrosAntes,
            "Arena ya ganada no duplica estadisticas ni logros");
        System.out.println("RESULTADO: " + aprobadas + " comprobaciones del taller 9 aprobadas.");
    }
}
