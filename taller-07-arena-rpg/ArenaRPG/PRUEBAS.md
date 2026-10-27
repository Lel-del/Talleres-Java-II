# Resultados reales — Talleres 7, 8 y 9

Verificación final: Microsoft OpenJDK 21.0.12.1. Compilación de 25 clases principales
y 3 ejecutables de prueba con UTF-8 y -Xlint:all, sin errores ni advertencias.
Se utilizó target/verificacion-taller9/classes, una carpeta distinta de las compilaciones
anteriores, para evitar depender de archivos .class antiguos.

**122 comprobaciones aprobadas:** 24 de PruebasArena, 37 de PruebasRelaciones y
61 de PruebasTaller9. Además se ejecutó Main en un proceso separado con texto inválido,
registro manual, consulta, reporte y salida correcta; System.err notificó el dato inválido.

## Adaptación de las pruebas de los talleres anteriores

Se conservaron las 24 + 37 comprobaciones, sin eliminarlas ni cambiar las reglas de
victoria, derrota, relaciones o conservación de objetos. Se adaptaron:

- Construcción de personajes: ahora todos los llamados indican TipoPersonaje obligatorio.
- Daños esperados: se añadieron los bonos fijos de tipo; por ejemplo defensa del guerrero
  con escudo 10 mitiga 11, ataque desarmado de nivel 3 pasa de 15 a 16 y arma de daño 7
  produce total 23. PruebasRelaciones espera vida 57/11 tras normal/furia sobre un mago
  de 80 de vida, y 87 tras un hechizo equipado contra un guerrero de escudo 10.
- Sin maná: la prueba exige SinManaException y luego decide explícitamente el ataque básico;
  no exige que el dominio oculte la excepción como antes.
- Scripts de menús: equipar requiere ahora elegir rareza; se utiliza COMUN en las regresiones.
- Partida ganadora: se ajustó el número de ataques porque los bonos cambian las bajas.
  Sigue incluyendo una poción, turnos enemigos y movimiento hasta el cofre.
- Partida perdedora: se realizan movimientos sin atacar para exponer al escuadrón a los
  enemigos. El guion previo de ataques ya no provocaba derrota con los nuevos bonos.
- Las pruebas capturan también System.err porque los errores de lectura se notifican allí.

## Tabla de comprobaciones ejecutadas

| N.º | Ejecutable | Comprobación | Resultado |
|---:|---|---|---|
| 1 | PruebasArena | Escudo reduce dano | Aprobada |
| 2 | PruebasArena | Escudo evita dano negativo | Aprobada |
| 3 | PruebasArena | Sobrecarga ataque normal | Aprobada |
| 4 | PruebasArena | Sobrecarga furia | Aprobada |
| 5 | PruebasArena | Hechizo consume mana y aplica defensa | Aprobada |
| 6 | PruebasArena | Sin mana lanza excepcion y controlador usa ataque basico | Aprobada |
| 7 | PruebasArena | Objetivo null no consume mana | Aprobada |
| 8 | PruebasArena | Vida minima y objetivo derrotado | Aprobada |
| 9 | PruebasArena | Curacion limitada a vida maxima | Aprobada |
| 10 | PruebasArena | Pocion cura y se consume | Aprobada |
| 11 | PruebasArena | Inventario vacio no falla | Aprobada |
| 12 | PruebasArena | Limites del tablero | Aprobada |
| 13 | PruebasArena | No saltos ni diagonales | Aprobada |
| 14 | PruebasArena | Movimiento sincroniza tablero | Aprobada |
| 15 | PruebasArena | Casilla ocupada por aliado | Aprobada |
| 16 | PruebasArena | Casilla ocupada por enemigo | Aprobada |
| 17 | PruebasArena | Bajas eliminadas; falta cofre | Aprobada |
| 18 | PruebasArena | Victoria requiere enemigos y cofre | Aprobada |
| 19 | PruebasArena | Cofre con enemigos no da victoria | Aprobada |
| 20 | PruebasArena | Caida del principal termina mision | Aprobada |
| 21 | PruebasArena | Derrota escuadron vacio | Aprobada |
| 22 | PruebasArena | Entradas invalidas y fin de entrada | Aprobada |
| 23 | PruebasArena | Partida completa por menu hasta victoria | Aprobada |
| 24 | PruebasArena | Partida completa por menu hasta derrota | Aprobada |
| 25 | PruebasRelaciones | Desarmado: nivel por cinco mas bono de tipo | Aprobada |
| 26 | PruebasRelaciones | Equipamiento conserva referencia y suma una vez | Aprobada |
| 27 | PruebasRelaciones | Arma influye en ataque normal | Aprobada |
| 28 | PruebasRelaciones | Furia duplica ataque equipado | Aprobada |
| 29 | PruebasRelaciones | Arma null no reemplaza equipamiento | Aprobada |
| 30 | PruebasRelaciones | Desequipar dos veces es seguro | Aprobada |
| 31 | PruebasRelaciones | Hechizo usa arma, defensa y corazon | Aprobada |
| 32 | PruebasRelaciones | Alta desde personaje sincroniza | Aprobada |
| 33 | PruebasRelaciones | Duplicados y null no alteran gremio | Aprobada |
| 34 | PruebasRelaciones | Cambio desde gremio retira anterior | Aprobada |
| 35 | PruebasRelaciones | Cambio desde personaje retira anterior | Aprobada |
| 36 | PruebasRelaciones | Retiro desde gremio ajeno no rompe relacion | Aprobada |
| 37 | PruebasRelaciones | Baja desde gremio sincroniza | Aprobada |
| 38 | PruebasRelaciones | Baja desde personaje sincroniza sin recursion | Aprobada |
| 39 | PruebasRelaciones | Lista gremio protegida | Aprobada |
| 40 | PruebasRelaciones | Recluta objetos existentes sin null ni duplicados | Aprobada |
| 41 | PruebasRelaciones | Expulsion conserva estado, arma y gremio | Aprobada |
| 42 | PruebasRelaciones | Disolver no destruye personajes ni sus atributos | Aprobada |
| 43 | PruebasRelaciones | Lista escuadron protegida | Aprobada |
| 44 | PruebasRelaciones | Corazon privado final y exclusivo | Aprobada |
| 45 | PruebasRelaciones | Agregacion conserva identidad del corazon | Aprobada |
| 46 | PruebasRelaciones | API publica no expone ni sustituye corazon | Aprobada |
| 47 | PruebasRelaciones | Dano y curacion tienen una sola fuente | Aprobada |
| 48 | PruebasRelaciones | Mana almacenado solo en corazon | Aprobada |
| 49 | PruebasRelaciones | Reportes completos sin recursion circular | Aprobada |
| 50 | PruebasRelaciones | Bajas actualizan escuadron y mapa sin borrar gremio | Aprobada |
| 51 | PruebasRelaciones | Reclutamiento incorpora el mismo personaje al combate sin duplicados | Aprobada |
| 52 | PruebasRelaciones | Recluta recibe casilla libre | Aprobada |
| 53 | PruebasRelaciones | Tablero lleno rechaza reclutas sin sobrescribir | Aprobada |
| 54 | PruebasRelaciones | Menu completo crea, asocia, recluta, reporta y retira | Aprobada |
| 55 | PruebasRelaciones | Menu maneja lista de gremios vacia | Aprobada |
| 56 | PruebasRelaciones | Principal expulsado bloquea batalla y se puede reclutar otra vez | Aprobada |
| 57 | PruebasRelaciones | Menu integra batalla ganadora, resultado persistente y regreso sin reiniciar | Aprobada |
| 58 | PruebasRelaciones | Arma equipada por menu influye en combate real | Aprobada |
| 59 | PruebasRelaciones | Fin de entrada cancela creacion incompleta | Aprobada |
| 60 | PruebasRelaciones | Posicion publica invalida rechazada | Aprobada |
| 61 | PruebasRelaciones | Pocion null no altera otros personajes | Aprobada |
| 62 | PruebasTaller9 | Constructor exige tipo y estado inicial VIVO | Aprobada |
| 63 | PruebasTaller9 | Tipo incompatible rechazado | Aprobada |
| 64 | PruebasTaller9 | Bonificadores fijos de ataque aplicados | Aprobada |
| 65 | PruebasTaller9 | Defensa de tipo y escudo se suman una vez | Aprobada |
| 66 | PruebasTaller9 | Arquero redondea bonificadores 1.5 a 2 | Aprobada |
| 67 | PruebasTaller9 | Especial del arquero aplica total mas cinco | Aprobada |
| 68 | PruebasTaller9 | Rareza multiplica dano base sin alterarlo | Aprobada |
| 69 | PruebasTaller9 | Consultas repetidas no acumulan rareza ni bonificador | Aprobada |
| 70 | PruebasTaller9 | Pocion rara cura su valor efectivo una sola vez | Aprobada |
| 71 | PruebasTaller9 | Pocion legendaria utiliza factor 2.5 | Aprobada |
| 72 | PruebasTaller9 | HashSet rechaza duplicado normalizando espacios y mayusculas | Aprobada |
| 73 | PruebasTaller9 | Duplicado en libro lleno retorna false | Aprobada |
| 74 | PruebasTaller9 | Libro lleno lanza checked sin insertar | Aprobada |
| 75 | PruebasTaller9 | Habilidad null rechazada sin efectos | Aprobada |
| 76 | PruebasTaller9 | Vista de habilidades protegida | Aprobada |
| 77 | PruebasTaller9 | TreeSet unico y orden natural | Aprobada |
| 78 | PruebasTaller9 | Vista de logros protegida | Aprobada |
| 79 | PruebasTaller9 | getOrDefault devuelve cero para enemigo desconocido | Aprobada |
| 80 | PruebasTaller9 | HashMap incrementa y registra un solo logro automatico por enemigo | Aprobada |
| 81 | PruebasTaller9 | Mapa rechaza clave null sin cambios | Aprobada |
| 82 | PruebasTaller9 | Bestiario protegido de escrituras externas | Aprobada |
| 83 | PruebasTaller9 | TreeMap ordena claves y actualiza contadores double | Aprobada |
| 84 | PruebasTaller9 | Inventario lleno lanza checked sin sustitucion parcial | Aprobada |
| 85 | PruebasTaller9 | Vista del inventario no permite saltarse capacidad | Aprobada |
| 86 | PruebasTaller9 | Retirar libera capacidad | Aprobada |
| 87 | PruebasTaller9 | Sin mana no consume recursos ni causa dano | Aprobada |
| 88 | PruebasTaller9 | Costo exactamente igual al mana disponible permitido | Aprobada |
| 89 | PruebasTaller9 | Costo de mana negativo rechazado sin cambios | Aprobada |
| 90 | PruebasTaller9 | Objetivo invalido no consume mana | Aprobada |
| 91 | PruebasTaller9 | Costo igual a vida lanza unchecked sin consumir | Aprobada |
| 92 | PruebasTaller9 | Costo mayor a vida lanza unchecked sin consumir | Aprobada |
| 93 | PruebasTaller9 | Costo de vida negativo rechazado | Aprobada |
| 94 | PruebasTaller9 | Ritual puede dejar exactamente uno de vida | Aprobada |
| 95 | PruebasTaller9 | Constructores preservan causa raiz | Aprobada |
| 96 | PruebasTaller9 | Jerarquias checked y unchecked correctas | Aprobada |
| 97 | PruebasTaller9 | Veneno resta cinco y permite actuar | Aprobada |
| 98 | PruebasTaller9 | Veneno dura dos turnos y no se reaplica al expirar | Aprobada |
| 99 | PruebasTaller9 | Aturdido no ataca, pierde un turno y recupera accion | Aprobada |
| 100 | PruebasTaller9 | Veneno letal sincroniza estado y vida | Aprobada |
| 101 | PruebasTaller9 | Pociones y curacion no resucitan derrotados | Aprobada |
| 102 | PruebasTaller9 | Cambio de estado no resucita | Aprobada |
| 103 | PruebasTaller9 | No se marca derrotado a un personaje con vida | Aprobada |
| 104 | PruebasTaller9 | Baja real contada una sola vez pese a multiples actualizaciones | Aprobada |
| 105 | PruebasTaller9 | Registro manual no elimina enemigos ni gana la batalla | Aprobada |
| 106 | PruebasTaller9 | Arquero se representa con A en tablero | Aprobada |
| 107 | PruebasTaller9 | Veneno se integra en bucle real y puede provocar derrota | Aprobada |
| 108 | PruebasTaller9 | Menu crea arquero desde values del enum y lo recluta | Aprobada |
| 109 | PruebasTaller9 | Menu informa duplicado y continua | Aprobada |
| 110 | PruebasTaller9 | Menu captura capacidad del libro y continua | Aprobada |
| 111 | PruebasTaller9 | Menu captura inventario lleno sin exceder capacidad | Aprobada |
| 112 | PruebasTaller9 | Menu captura SinManaException y conserva recursos | Aprobada |
| 113 | PruebasTaller9 | Menu captura VidaInsuficienteException y continua | Aprobada |
| 114 | PruebasTaller9 | Menu presenta HashMap, logros y TreeMap | Aprobada |
| 115 | PruebasTaller9 | Menu aplica estado y combate omite turno real | Aprobada |
| 116 | PruebasTaller9 | InputMismatch texto overflow decimal linea mixta y vacia recuperan lectura | Aprobada |
| 117 | PruebasTaller9 | Fin de entrada inicial no crea bucle infinito | Aprobada |
| 118 | PruebasTaller9 | EOF cancela creacion incompleta sin insertar | Aprobada |
| 119 | PruebasTaller9 | EOF en rareza no equipa parcialmente | Aprobada |
| 120 | PruebasTaller9 | Controlador captura fallo real de mana y ejecuta fallback explicito | Aprobada |
| 121 | PruebasTaller9 | Veneno enemigo registra bajas reales, logro final y victoria | Aprobada |
| 122 | PruebasTaller9 | Arena ya ganada no duplica estadisticas ni logros | Aprobada |

## Evidencia y puntos pendientes

Los registros completos están en target/verificacion-taller9:
PruebasArena.txt, PruebasRelaciones.txt, PruebasTaller9.txt, Main.txt y compilacion.txt.
La carpeta target es generada y no se incluye en el ZIP de fuentes.

| Área | Estado |
|---|---|
| Compilación Java 21 y ejecución de las tres suites | Verificada |
| Main con entrada inválida y retorno normal al menú | Verificado en proceso separado |
| Combate hasta victoria y derrota, relaciones y genéricos | Verificados |
| Enums, estados, rarezas, capacidades, mapas, conjuntos y excepciones | Verificados |
| Inspección de composición mediante reflexión solo en pruebas | Verificada; no expone el corazón en la API pública |
| Maven | No ejecutado; las pruebas main no se invocan automáticamente con mvn test |
| Breakpoints, Variables, Call Stack y Evaluate Expression en IntelliJ | Guía preparada; práctica en su interfaz pendiente |
| Recolección de basura | No observada ni afirmada |
| Publicación en GitHub | No realizada |

Los conjuntos y mapas usan String; no se introdujeron objetos propios como claves que
requieran nuevos equals/hashCode. Las pruebas de composición usan reflexión exclusivamente
para revisar propiedad e identidad; la aplicación no entrega referencias al corazón.
