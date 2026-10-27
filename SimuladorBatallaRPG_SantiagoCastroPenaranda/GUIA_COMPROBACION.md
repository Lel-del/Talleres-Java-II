# Guía de comprobación del RPG

## Ejecución manual en IntelliJ

Abre Main.java y ejecuta main con JDK 21. Escribe cada entrada en la consola y pulsa
Enter. Reinicia el programa para volver a los valores de demostración.

| Prueba | Pasos | Resultado esperado |
|---|---|---|
| Estado inicial | 3 | Héroe 100/100, oro 50.00, nivel 1, una poción; enemigo 150/150. Sin daño. |
| Entrada errónea | hola, 0, 9, 3 | Avisos y estado inicial intacto. |
| Volver del submenú | 2, 3, 3 | Ningún daño ni cambio de inventario. |
| Poción con vida completa | 2, 1 | No consume poción ni permite contraataque. |
| Compra válida | 2, 2 | Oro 25.00, dos pociones; contraataque del enemigo por 15. |
| Saldo insuficiente | Comprar tres veces seguidas | Las dos primeras compras cuestan 25.00; la tercera no entrega nada ni consume turno. |
| Curación con efecto | Atacar tres veces, luego 2, 1 | Cura hasta 40 antes del contraataque y descuenta una poción. |
| Inventario vacío | Gastar la única poción inicial y repetir 2, 1 | Rechaza y no consume turno. |
| Crítico | Atacar durante varias ejecuciones | Algunos ataques indican GOLPE CRÍTICO. No se garantiza en una ejecución particular. |
| Victoria | Atacar tres veces, usar la poción inicial y seguir atacando | Victoria, recompensa única 120 XP, nivel 2 y 20 XP restantes. |
| Derrota | Atacar sin curarse en varias ejecuciones | Puede perder según los críticos. Al caer, vida 0 y vivo false; termina el menú. |
| Huida | 4 | Finaliza como HUIDA, sin daño adicional. |

## Límites de dominio

En un contexto de comprobación independiente o con el depurador, verifica:

- Personaje() y constructor completo: estado consistente; entradas iniciales inválidas se rechazan.
- setPuntosVida(-10), 0 y 100000: para máximo 100, resultados 0/false, 0/false y 100/true.
- usarPocion(Integer.MAX_VALUE): no desborda ni supera el máximo.
- recibirDano(0) o negativo: no cambia vida. Daño mayor a la vida: 0 y false.
- setOro(-1), NaN e infinito: retorna false y conserva el saldo.
- setNivel(0) y negativo desde una subclase de comprobación: no alteran nivel.
- Dos reclamaciones de XP del mismo enemigo: solo la primera concede recompensa.
- Daño crítico grande: no produce daño negativo por overflow.
- Todos los campos de instancia del héroe y enemigo son privados.

## Verificación realizada

El 19/10/2026 se compiló con JDK 21, javac -encoding UTF-8 -Xlint:all -Werror,
sin errores ni advertencias. Se ejecutaron 94 comprobaciones de dominio e
interfaz, todas satisfactorias. Incluyeron constructores, vida límite, coherencia
del estado, oro no finito, nivel protegido, inventario, compras, XP única y
overflow. Se probó el flujo real de consola con estado, errores, tienda,
curación, daño real, huida, victoria, derrota y fin de entrada.

Para verificar ataques normales y críticos sin pruebas dependientes del azar,
se usó una subclase de comprobación externa a las fuentes que fuerza la decisión
del crítico. También se verificaron los límites 0, 0.149999 y 0.15 de la regla.
Las pruebas deterministas de combate llamaron al mismo flujo ejecutarBatalla
que utiliza Main. Después se ejecutó el main real con Math.random para una
victoria y una huida, ambas satisfactorias.

Se verificaron tres rechazos de compilación en fuentes temporales independientes:
acceso a puntosVida privado, llamada externa a setNivel protegido y acceso a
un miembro package-private desde un subpaquete distinto. Se inspeccionaron los
campos de instancia para confirmar private y las fuentes para comprobar el
uso exclusivo de printf. Los casos deliberadamente incorrectos no están en src.

Las comprobaciones se ejecutaron directamente con el JDK instalado. No se
ejecutó Maven ni se abrió la interfaz gráfica de IntelliJ.

## Verificación manual pendiente

Abrir IntelliJ y revisar su autocompletado, la configuración efectiva del JDK y el
depurador gráfico. Las pruebas externas del compilador no sustituyen esta revisión
de la interfaz de IntelliJ. El historial aleatorio real varía entre ejecuciones.
