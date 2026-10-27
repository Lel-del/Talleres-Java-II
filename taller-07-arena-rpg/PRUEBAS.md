# Resultados reales — Talleres 7 y 8

Verificación final realizada con Microsoft OpenJDK 21.0.12.1.
Se compilaron 13 clases principales y 2 ejecutables de prueba con UTF-8 y -Xlint:all,
sin errores ni advertencias. La compilación de verificación usa una carpeta nueva:
target/verificacion-taller8; no depende de los .class del taller anterior.

**61 comprobaciones aprobadas: 24 de PruebasArena y 37 de PruebasRelaciones.**
Además se ejecutó Main en un proceso separado con las opciones 5 y 6: mostró
el reporte y cerró correctamente (comprobación adicional a las 61).

Las 24 pruebas anteriores se conservaron y ejecutaron sin cambiar sus expectativas.
Cubren combate normal/furia/hechizo, maná insuficiente, pociones, movimientos,
inventario vacío, caídos, condiciones finales, entradas inválidas y partidas completas
hasta victoria y derrota. Las pruebas nuevas también ejecutan el menú de gestión
y una batalla completa con retorno al menú.

## Tabla de comprobaciones ejecutadas

| N.º | Comprobación | Resultado |
|---:|---|---|
| 1 | Escudo reduce dano | Aprobada |
| 2 | Escudo evita dano negativo | Aprobada |
| 3 | Sobrecarga ataque normal | Aprobada |
| 4 | Sobrecarga furia | Aprobada |
| 5 | Hechizo consume mana y aplica defensa | Aprobada |
| 6 | Sin mana usa ataque basico | Aprobada |
| 7 | Objetivo null no consume mana | Aprobada |
| 8 | Vida minima y objetivo derrotado | Aprobada |
| 9 | Curacion limitada a vida maxima | Aprobada |
| 10 | Pocion cura y se consume | Aprobada |
| 11 | Inventario vacio no falla | Aprobada |
| 12 | Limites del tablero | Aprobada |
| 13 | No saltos ni diagonales | Aprobada |
| 14 | Movimiento sincroniza tablero | Aprobada |
| 15 | Casilla ocupada por aliado | Aprobada |
| 16 | Casilla ocupada por enemigo | Aprobada |
| 17 | Bajas eliminadas; falta cofre | Aprobada |
| 18 | Victoria requiere enemigos y cofre | Aprobada |
| 19 | Cofre con enemigos no da victoria | Aprobada |
| 20 | Caida del principal termina mision | Aprobada |
| 21 | Derrota escuadron vacio | Aprobada |
| 22 | Entradas invalidas y fin de entrada | Aprobada |
| 23 | Partida completa por menu hasta victoria | Aprobada |
| 24 | Partida completa por menu hasta derrota | Aprobada |
| 25 | Desarmado: nivel por cinco | Aprobada |
| 26 | Equipamiento conserva referencia y suma una vez | Aprobada |
| 27 | Arma influye en ataque normal | Aprobada |
| 28 | Furia duplica ataque equipado | Aprobada |
| 29 | Arma null no reemplaza equipamiento | Aprobada |
| 30 | Desequipar dos veces es seguro | Aprobada |
| 31 | Hechizo usa arma, defensa y corazon | Aprobada |
| 32 | Alta desde personaje sincroniza | Aprobada |
| 33 | Duplicados y null no alteran gremio | Aprobada |
| 34 | Cambio desde gremio retira anterior | Aprobada |
| 35 | Cambio desde personaje retira anterior | Aprobada |
| 36 | Retiro desde gremio ajeno no rompe relacion | Aprobada |
| 37 | Baja desde gremio sincroniza | Aprobada |
| 38 | Baja desde personaje sincroniza sin recursion | Aprobada |
| 39 | Lista gremio protegida | Aprobada |
| 40 | Recluta objetos existentes sin null ni duplicados | Aprobada |
| 41 | Expulsion conserva estado, arma y gremio | Aprobada |
| 42 | Disolver no destruye personajes ni sus atributos | Aprobada |
| 43 | Lista escuadron protegida | Aprobada |
| 44 | Corazon privado final y exclusivo | Aprobada |
| 45 | Agregacion conserva identidad del corazon | Aprobada |
| 46 | API publica no expone ni sustituye corazon | Aprobada |
| 47 | Dano y curacion tienen una sola fuente | Aprobada |
| 48 | Mana almacenado solo en corazon | Aprobada |
| 49 | Reportes completos sin recursion circular | Aprobada |
| 50 | Bajas actualizan escuadron y mapa sin borrar gremio | Aprobada |
| 51 | Reclutamiento incorpora el mismo personaje al combate sin duplicados | Aprobada |
| 52 | Recluta recibe casilla libre | Aprobada |
| 53 | Tablero lleno rechaza reclutas sin sobrescribir | Aprobada |
| 54 | Menu completo crea, asocia, recluta, reporta y retira | Aprobada |
| 55 | Menu maneja lista de gremios vacia | Aprobada |
| 56 | Principal expulsado bloquea batalla y se puede reclutar otra vez | Aprobada |
| 57 | Menu integra batalla ganadora, resultado persistente y regreso sin reiniciar | Aprobada |
| 58 | Arma equipada por menu influye en combate real | Aprobada |
| 59 | Fin de entrada cancela creacion incompleta | Aprobada |
| 60 | Posicion publica invalida rechazada | Aprobada |
| 61 | Pocion null no altera otros personajes | Aprobada |

## Evidencia y límites

- Registros completos: target/verificacion-taller8/PruebasArena.txt,
  PruebasRelaciones.txt, Main.txt y compilacion.txt.
- Las pruebas de composición inspeccionan mediante reflexión solo en el código de prueba
  que corazon es privado, final, distinto para cada personaje y no se expone por la API pública.
  También verifican el daño, curación y maná reales; no afirman observar recolección de basura.
- La prueba de tablero lleno recluta hasta ocupar las 21 casillas disponibles para héroes,
  respetando tres enemigos y un cofre; el siguiente recluta es rechazado.
- Se probó que equipar un arma desde el menú cambia el daño en la batalla real.
- No se ejecutó Maven. Estos ejecutables main no usan JUnit y mvn test no los invoca automáticamente.
- Pendiente de práctica en la interfaz de IntelliJ: breakpoints, Variables, Call Stack,
  F7/F8 y evaluación de expresiones. La guía está en DEPURACION.md; no se presentan como realizados.
- No se publicaron cambios ni se ejecutó una subida a GitHub.
