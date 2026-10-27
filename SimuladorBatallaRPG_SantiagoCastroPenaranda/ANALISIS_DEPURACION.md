# Análisis de encapsulamiento y acceso

Santiago Castro Peñaranda. Módulo M4. 19/10/2026.
Los ejemplos incorrectos se conservan como texto y no forman parte de las fuentes.

## Caso A: acceso directo a un dato privado

```java
Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
heroe.puntosVida = 200; // Incorrecto: atributo privado.
System.out.printf("Vida: %d%n", heroe.puntosVida); // También es acceso privado.
```

El compilador informa que puntosVida tiene acceso privado en Personaje; por ejemplo,
"puntosVida has private access in co.edu.puj.rpg.modelo.Personaje". No es una excepción
del juego: el acceso indebido impide compilar. El modificador private protege el dato
tanto para lectura como para escritura desde estas clases externas.

La corrección usa la API pública y respeta los límites:

```java
heroe.setPuntosVida(200);
System.out.printf("Vida: %d%n", heroe.getPuntosVida()); // 100, por el máximo.
heroe.recibirDano(15);
System.out.printf("Vida: %d%n", heroe.getPuntosVida()); // 85.
```

| Modificador de un miembro | Acceso permitido |
|---|---|
| private | Desde el ámbito privado de la clase que lo declara; las clases anidadas de su mismo ámbito superior también pueden acceder. Main no pertenece a ese ámbito. |
| Sin modificador (package-private) | Desde otras clases del mismo paquete exacto. |
| protected | Desde el mismo paquete y desde subclases externas, con las restricciones de acceso mediante referencias propias de la herencia. |
| public | Desde otros paquetes que puedan acceder a la clase. |

modelo, modelo.enemigo y principal son paquetes diferentes. Un subpaquete no hereda
acceso al paquete padre. Main no puede invocar setNivel, pues no pertenece al paquete
modelo ni hereda de Personaje. La subida de nivel usa ganarExperiencia, que delega
en el setter protegido desde dentro del personaje.

## Caso B: constructor que evita las validaciones

```java
public Personaje(int puntosVida) {
    this.puntosVida = -50;
    this.estaVivo = true;
}
```

El código puede compilar: private no impide que la propia clase escriba en sus campos.
El error es lógico. El constructor ignora el parámetro y crea un objeto con vida
negativa que figura como vivo. Encapsular incluye preservar las reglas internas,
no solamente cambiar la visibilidad.

En el proyecto, el constructor completo valida primero nombre, vida máxima, ataque
y oro. Después inicializa vida con setPuntosVida, oro con setOro y nivel con setNivel.
El constructor sin parámetros delega al completo usando this(...).

Fragmento representativo de la inicialización correcta:

```java
this.nombre = nombre.trim();
this.vidaMaxima = vidaMaxima; // Previamente validada: mayor que cero.
this.puntosAtaque = puntosAtaque;
this.setPuntosVida(this.vidaMaxima);
this.setOro(oro);
this.setNivel(1);
```

El proyecto rechaza vidaMaxima <= 0 mediante IllegalArgumentException; no fabrica un
personaje que comience vivo con una vida inválida. Si en otra variante se recibe vida
actual, esa vida debe pasar por setPuntosVida una vez establecido el máximo válido.

## Caso C: asignaciones fuera de límites

Para un héroe con vida máxima 100:

| Llamada | Vida resultante | estaVivo |
|---|---:|---|
| setPuntosVida(999999) | 100 | true |
| setPuntosVida(-100) | 0 | false |
| setPuntosVida(0) | 0 | false |
| setPuntosVida(50) después de las anteriores | 50 | true |

El setter no se limita a guardar un número. Normaliza la vida al intervalo permitido
y sincroniza estaVivo. Un valor positivo posterior reactiva el estado según la regla
del taller. El método usarPocion, por separado, impide curar a un personaje derrotado.

```java
public void setPuntosVida(int vida) {
    if (vida <= 0) {
        this.puntosVida = 0;
        this.estaVivo = false;
    } else {
        this.puntosVida = Math.min(vida, this.vidaMaxima);
        this.estaVivo = true;
    }
}
```

## Verificación manual en IntelliJ

En Main, escribe heroe. y revisa la API disponible. Los campos privados y setNivel
no deben estar disponibles como accesos válidos; el IDE puede mostrar sugerencias
adicionales según su configuración, pero la autoridad final es el compilador.

Coloca breakpoints en setPuntosVida, setOro, comprarPocionGrande y ganarExperiencia.
Con Debug observa this y sus atributos mediante Variables o Watches. El depurador
puede inspeccionar campos privados: eso no significa que el código externo tenga
permiso para acceder a ellos. Mantén las pruebas de errores de compilación fuera
de las fuentes del proyecto final.
