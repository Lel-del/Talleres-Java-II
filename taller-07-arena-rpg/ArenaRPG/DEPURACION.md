# Guía de depuración — Talleres 7, 8 y 9

Los pasos siguientes están preparados para realizar la práctica en IntelliJ IDEA.
No se ha utilizado su interfaz ni su depurador durante esta entrega. Las comprobaciones
automáticas y su evidencia se describen en PRUEBAS.md.

## HashSet y duplicados

1. Abre Main y pon un breakpoint en la llamada a aprenderHabilidad dentro de
   AplicacionArenaRPG.ejecutar, opción 12. Arranca Debug con Shift+F9.
2. Selecciona Thorin y escribe «Bola de Fuego». Repite con «bola de fuego».
   En Variables despliega aprendiz y habilidades: size debe seguir siendo 1.
3. Dentro de Personaje.aprenderHabilidad, usa Alt+F8 (Evaluate Expression):
   habilidades.size(), obtenerHabilidades().contains("Bola de Fuego").
   El contains directo de String es sensible a mayúsculas; la normalización del método
   es la que rechaza duplicados ignorando mayúsculas.
4. Despliega la estructura del HashSet para ver su mapa interno y, si el IDE lo permite,
   la tabla de buckets. No asumas que el orden de iteración representa el de aprendizaje.

## Excepciones de maná, capacidad y vida

1. Pon un breakpoint en el throw new SinManaException de Personaje.lanzarHechizo.
   Configura la condición manaActual < costo. En el menú usa 15, Gandalf, un enemigo y costo 51.
2. Examina corazon.manaBase (50), costo (51) y la vida del objetivo. Con F8 avanza hasta
   propagar la excepción y observa el catch específico de AplicacionArenaRPG.
3. En Call Stack debe verse lanzarHechizo → hechizoDePractica → ejecutar → Main.main.
   El menú continúa y los recursos permanecen sin cambios. Un Exception Breakpoint de
   SinManaException permite detenerse al lanzarla aunque sea capturada después.
4. Para la batalla normal, Mago.realizarAtaqueEspecial solicita costo 10. Al agotarlo,
   Arena.ejecutarEspecial captura SinManaException y decide el ataque básico: el dominio
   no oculta el fallo. Usa F7 para entrar y F8 para avanzar sin entrar.
5. Llena el libro con cinco habilidades y prueba una sexta; inspecciona el tamaño antes y
   después de CapacidadExcedidaException. Un duplicado debe retornar false incluso lleno.
6. El inventario empieza con tres pociones y admite seis. Usa 16 cuatro veces para provocar
   el límite. En el catch verifica que la séptima no fue insertada.
7. Usa 17 con Thorin y costo 120: VidaInsuficienteException debe impedir el consumo.
   Un costo 119 deja 1 de vida. No introduzcas costos negativos desde la API.

## HashMap, TreeMap y logros

1. Usa 13 para registrar «Orco» dos veces y 14 para consultar. En Variables despliega
   arena.bestiario.derrotas: clave «orco», valor 2. Solo un logro para ese nombre.
2. Con Alt+F8 dentro de Bestiario, evalúa derrotas.containsKey("orco"),
   derrotas.getOrDefault("desconocido", 0) y derrotas.size().
3. Inspecciona arena.perfil.logros y arena.estadisticas.valores: TreeSet y TreeMap
   se recorren por orden natural. Los contadores manuales y bajas reales son diferentes.
4. Pon un breakpoint en Arena.registrarBajasEnemigas, derrota un enemigo y comprueba que
   se incrementa antes de retirarlo. Repetir la limpieza ya no vuelve a contar esa baja.

## Estados, rareza y arquero

1. Crea un ARQUERO desde la opción 1 y reclútalo con 3. Inspecciona tipoPersonaje.
   En el tablero usa A y su ataque especial aplica total + 5.
2. Equipa un arma RARO de daño base 7 con 4: daño efectivo 11, sin cambiar danioBase.
   Evalúa calcularAtaqueTotal() varias veces; no debe acumular el multiplicador.
3. Usa 18 para aplicar ENVENENADO a Thorin y entra al combate con 11. Pon un breakpoint
   en actualizarTurno: la salud pierde 5 al comienzo de cada uno de dos turnos, luego VIVO.
4. Repite con ATURDIDO: omite un turno completo y después vuelve a VIVO.
   Si la salud llega a cero, estado queda DERROTADO; no debe resucitar al cambiar estado.

## Relaciones y combate conservados

- En Personaje.unirseAGremio y Gremio.agregarMiembro inspecciona ambos extremos.
  Tras cambiar de gremio, el anterior ya no contiene al personaje.
- En Personaje.toString revisa Call Stack: no debe alternar indefinidamente con
  Gremio.toString. Los reportes de gremio/escuadrón utilizan nombres, no toString completo.
- En Escuadron.expulsarPersonaje observa que el mismo objeto sigue en el registro del menú
  con su corazón, arma y gremio. La composición no se prueba forzando System.gc().
- En recibirDano/curar revisa corazon.saludBase; no hay otra vida independiente en Personaje.
- En Arena.jugar inspecciona heroes, que es una vista de la lista del escuadrón.
  La limpieza de bajas se realiza fuera de los for-each.
- Pon breakpoints en realizarAtaqueEspecial, defender y mover. Para un movimiento fuera
  del tablero usa la condición x < 0 || x >= 5 || y < 0 || y >= 5.

## Entrada resiliente

En EntradaConsola.leerEntero pon breakpoints en catch InputMismatchException y finally.
Prueba «hola», un número demasiado grande, «1.5», «5 texto» y una línea vacía; después
introduce 5 y un nombre cuando corresponda. El auxiliar se limpia/cierra, el Scanner principal
permanece abierto y el programa continúa. EOF termina sin bucle infinito.

Alt+F8 abre Evaluate Expression. Para conservar una expresión en Watches utiliza la acción
de añadir un Watch de la interfaz; no se confunden ambas funciones.
