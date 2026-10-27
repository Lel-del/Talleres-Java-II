# Depuración de los talleres 7 y 8 en IntelliJ

Esta es una guía para practicar. No se ha ejecutado la interfaz de IntelliJ ni se han
capturado breakpoints durante la actualización; las comprobaciones automáticas se detallan aparte.

## Relaciones del taller 8

1. Abre Main y arranca Debug con Shift+F9. Pon un breakpoint en Personaje.toString().
   Crea un gremio, asigna un personaje con la opción 7 y muestra el estado con la opción 5.
2. En Frames / Call Stack observa AplicacionArenaRPG.mostrarReporte → Personaje.toString.
   No debe alternar repetidamente con Gremio.toString. El reporte del gremio usa únicamente
   getNombre de los miembros. No introduzcas una recursión deliberada en la versión final.
3. Pon breakpoints en Personaje.unirseAGremio y Gremio.agregarMiembro. Con F7 entra en las
   llamadas cruzadas; con F8 avanza sin entrar. Al finalizar, comprueba en Variables que
   Personaje.gremio apunta al mismo gremio que contiene al personaje en miembros.
4. Con un personaje que ya tenga gremio, elige otro: comprueba que salirDeGremio elimina
   primero la relación anterior. En Alt+F8 (Evaluate Expression), dentro de Personaje, evalúa
   getGremio(), getGremio().getMiembros().contains(this), siempre que getGremio() no sea null.
   Puedes añadir estas expresiones a Watches mediante la interfaz del depurador. Alt+F8 abre
   evaluación de expresiones; no es por sí solo el comando de crear un Watch.
5. En calcularAtaqueTotal observa armaEquipada. Sin arma es null y el condicional evita
   dereferenciarla. Equipa y retira un arma desde las opciones 4 y 9 y compara el ataque total.
6. En el constructor de Personaje verifica que corazon se crea mediante new. En Variables
   despliega corazon y observa saludBase y manaBase. Cada personaje debe tener un objeto distinto.
7. Pon un breakpoint en Escuadron.expulsarPersonaje. Expulsa un héroe con la opción 10:
   sigue en el registro personajes de AplicacionArenaRPG con sus atributos y gremio intactos,
   aunque ya no aparezca en miembros. Al reclutarlo no se crea un personaje nuevo.

## Combate del taller 7

1. Pon breakpoints en realizarAtaqueEspecial de Guerrero/Mago y en el do-while de Arena.jugar.
   En el menú de gestión usa 11 para iniciar; en combate usa 1 y selecciona un enemigo.
2. Usa F7 para entrar en atacar/defender/recibirDano. Inspecciona el ataque equipado, el escudo
   y la salud dentro del corazón. Sin maná, el mago utiliza el ataque básico.
3. Dentro de Arena, evalúa heroes.size(), enemigos.size(), heroes.get(0).estaVivo() y
   tablero[2][2]. heroes es una vista de la lista del escuadrón, no una copia.
4. En mover usa un breakpoint condicional con x < 0 || x >= 5 || y < 0 || y >= 5.
   Introduce un movimiento inválido y verifica que se rechaza antes de acceder a la matriz.

| Problema | Punto de inspección / prevención |
|---|---|
| NullPointerException | armaEquipada/gremio pueden ser null; corazon se inicializa siempre |
| Desincronización | al terminar alta/baja, inspecciona personaje.gremio y gremio.miembros |
| StackOverflowError | Call Stack no debe repetir indefinidamente Personaje y Gremio.toString |
| ConcurrentModificationException | retirarCaidos/removeIf se ejecutan fuera de recorridos de combate; no alteres las listas en un for-each |
| Índice inválido | menú valida índices; mover valida 0..4 |
| ClassCastException | Inventario<Pocion> usa genéricos y recupera Pocion sin casteos |

No se afirma haber observado recolección de basura. El comportamiento de agregación se demuestra
conservando una referencia al personaje, expulsándolo y consultando el mismo objeto después.
