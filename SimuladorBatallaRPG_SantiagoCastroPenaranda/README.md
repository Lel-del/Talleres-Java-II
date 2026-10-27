# Simulador de Batalla RPG: Arena de Héroes

Estudiante: Santiago Castro Peñaranda. Módulo: M4.
Docente e institución: no proporcionados. Fecha: 19/10/2026.
Proyecto independiente: SimuladorBatallaRPG_SantiagoCastroPenaranda.
Java 21, SDK estándar, sin bibliotecas externas.

## Diseño y archivos

Personaje protege vida, oro, nivel, inventario y experiencia. Enemigo controla su
salud y que su recompensa se entregue una sola vez. Main lee opciones, coordina
los turnos y construye el historial, consultando el estado mediante getters.
Las clases de dominio no imprimen mensajes: devuelven resultados o permiten
consultar sus cambios. Main usa exclusivamente System.out.printf.

```text
SimuladorBatallaRPG_SantiagoCastroPenaranda/
├── pom.xml
├── README.md
├── ANALISIS_DEPURACION.md
├── GUIA_COMPROBACION.md
└── src/main/java/co/edu/puj/rpg/
    ├── modelo/
    │   ├── Personaje.java
    │   └── enemigo/
    │       └── Enemigo.java
    └── principal/
        └── Main.java
```

## Abrir y ejecutar en IntelliJ IDEA

1. Abre la carpeta del proyecto o su pom.xml como proyecto Maven.
2. Selecciona JDK 21 en File > Project Structure > Project SDK.
3. Si aparece una notificación Maven, recarga el proyecto. No necesita dependencias.
4. Abre src/main/java/co/edu/puj/rpg/principal/Main.java.
5. Ejecuta el main con el triángulo verde. Escribe en la consola y pulsa Enter.

Si usas terminal con el JDK en PATH, puedes compilar sin Maven desde la carpeta:

```powershell
New-Item -ItemType Directory -Force target/classes | Out-Null
$fuentes = @(Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object { $_.FullName })
javac -encoding UTF-8 -d target/classes $fuentes
java -cp target/classes co.edu.puj.rpg.principal.Main
```

La guía pide comprobar JAVA_HOME: en PowerShell consulta $env:JAVA_HOME y verifica
que señale la carpeta del JDK, no bin. IntelliJ también permite seleccionar su JDK
directamente. Esta entrega no cambia variables globales del equipo.

## Cómo jugar

El héroe empieza con vida 100, ataque 20, nivel 1, oro 50.00 y una poción grande.
El enemigo tiene vida 150, ataque 15 y recompensa 120 XP. Cada poción cuesta 25.00
y restaura hasta 40 puntos de vida. Son valores ficticios de demostración.

El menú principal conserva cuatro opciones: atacar, usar poción/tienda, ver estado
y huir. La opción 2 abre un submenú para usar una poción, comprarla o volver.

- Atacar, usar una poción con efecto y comprar con éxito consumen un turno.
- Tras esas acciones el enemigo contraataca una vez si ambos siguen vivos.
- Ver estado, volver, errores de entrada y operaciones rechazadas no consumen turno.
- Una poción no se gasta con vida completa ni con inventario vacío.
- El héroe derrotado no ataca, no compra ni se cura con pociones. Huir termina el juego.
- Un enemigo derrotado no contraataca. Cada ataque del héroe tiene 15% de probabilidad
  de duplicar su daño. El daño registrado es el realmente aplicado.
- Al ganar, se reciben 120 XP una sola vez. Cada 100 XP sube un nivel: al terminar
  esta batalla, nivel 2 y 20 XP restantes. El nivel no aumenta vida ni ataque en esta versión.

El historial es acumulativo y vuelve a imprimirse después de cada acción del menú.
No significa que los eventos anteriores se repitan: su texto se conserva para consulta.
La última impresión añade el resultado final y la recompensa cuando corresponde.

Para ganar incluso sin críticos, prueba: atacar tres veces, usar una poción desde
la opción 2 y seguir atacando. El combate podría acabar antes por golpes críticos.
Cerrar la entrada termina como huida, sin excepción por falta de datos.

## Encapsulamiento y decisiones

Los siete atributos solicitados de Personaje y todos los datos de instancia de Enemigo
son private. No existen setters para modificar libremente ataque o vida máxima.
Las constantes públicas son inmutables y no exponen datos de instancia modificables.

setPuntosVida conserva la relación vida/estado. Por especificación, asignarle una
vida positiva puede volver a poner estaVivo en true; en el combate, usarPocion bloquea
la resurrección de un derrotado. setOro retorna boolean para comunicar la aceptación
o rechazo sin imprimir desde el dominio. Los tres setters validados son final para que una subclase no pueda omitir sus
validaciones ni redefinir una llamada durante la construcción. setNivel es protected y se invoca desde
ganarExperiencia, no desde Main.

La guía anuncia dos constructores pero solo detalla el completo. Se agregó Personaje()
con valores predeterminados documentados. Los constructores rechazan nombres vacíos,
vida máxima o ataque no positivos y oro inválido para no construir estados inconsistentes.
El enemigo también valida sus datos iniciales.

Los subpaquetes NO comparten automáticamente acceso package-private. Main interactúa
con modelo y modelo.enemigo usando la API pública. ejecutarBatalla tiene visibilidad
de paquete dentro de principal. El método protegido esGolpeCritico permite separar
la regla del 15% de la tirada aleatoria para comprobar ambos resultados sin depender
del azar; atacar sigue usando Math.random en el juego.

Se usan long en sumas y multiplicaciones intermedias para evitar desbordamientos de
int. Al aplicar daño o subir el nivel se limita el resultado al rango de int. Las
curaciones no superan la vida máxima. setOro rechaza negativos, NaN e infinito.

Requisitos del taller: paquetes, atributos privados, setters validados, getters,
constructores, combate do-while/switch, printf, historial, tienda, experiencia,
críticos y análisis de depuración. Decisiones adicionales: precios y estadísticas,
inventario inicial, reglas de contraataque/turnos, el segundo constructor, estructura
Maven sin dependencias, valores límite y la forma de comunicar compras rechazadas.

Consulta ANALISIS_DEPURACION.md y GUIA_COMPROBACION.md para estudiar y verificar.
