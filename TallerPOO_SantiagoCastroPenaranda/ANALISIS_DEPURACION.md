# Análisis y depuración de POO

Autor: Santiago Castro Peñaranda. Módulo: M4.
Los fragmentos incorrectos son ejemplos documentados y no forman parte del código compilable.

## Caso 1: variable local sin inicializar

```java
Estudiante estudiante1;
String reporte = estudiante1.obtenerReporteAcademicos(); // Incorrecto.
```

El compilador informa que la variable estudiante1 podría no haber sido inicializada
(variable estudiante1 might not have been initialized). No se llega a ejecutar esa
invocación. Declarar una variable local de referencia no crea un objeto ni le asigna
automáticamente null. Java exige asignación definida antes de leerla.

Si se escribiera Estudiante estudiante1 = null, sí compilaría la declaración, pero
invocar después el método produciría NullPointerException. El título del caso en la
guía menciona esa excepción; el fragmento concreto falla antes, durante la compilación.

Corrección compatible con el constructor de este proyecto:

```java
Estudiante estudiante1 = new Estudiante(
        "EST-001", "Santiago Castro Peñaranda", 4.50, true);
String reporte = estudiante1.obtenerReporteAcademicos();
System.out.printf("%s%n", reporte);
```

La matrícula y la nota son ficticias. new crea el objeto y el constructor inicializa
su estado. La variable almacena la referencia que permite invocar el método de instancia.

## Caso 2: método de instancia desde un contexto estático

Una llamada directa a encenderMotor() desde main produce un error como
"non-static method encenderMotor() cannot be referenced from a static context".
main pertenece a la clase y puede ejecutarse sin un objeto; un método de instancia
necesita una instancia receptora, que normalmente está representada por this.

Alternativa 1: crear un objeto. Es la adecuada si el método modifica el motor de un vehículo.

```java
GestorVehiculos gestor = new GestorVehiculos();
gestor.encenderMotor();
```

Alternativa 2: declarar el método estático cuando no dependa del estado de un objeto.
Para el ejemplo que solo imprime un mensaje, esta alternativa puede compilar:

```java
public static void encenderMotor() {
    System.out.printf("El motor del vehículo ha sido encendido.%n");
}
// Dentro de main:
// GestorVehiculos.encenderMotor();
```

Volver estático un método no es una corrección de diseño apropiada si necesita
leer o modificar atributos de instancia; en ese caso debe conservarse la primera opción.

## Caso 3: un falso constructor con void

```java
public void CuentaBancaria(String numeroCuenta, double saldoInicial) {
    this.numeroCuenta = numeroCuenta;
    this.saldo = saldoInicial;
}
```

La presencia de void lo convierte en un método ordinario con nombre CuentaBancaria.
Un constructor no declara tipo de retorno y debe llamarse igual que su clase.
En el fragmento aislado, al no existir constructores explícitos, Java aporta uno
sin parámetros. Por eso new CuentaBancaria("A-101", 500.0) no encuentra un constructor
compatible con String y double. Los métodos ordinarios no satisfacen una llamada a new.

La corrección de ese ejemplo aislado sería:

```java
public CuentaBancaria(String numeroCuenta, double saldoInicial) {
    this.numeroCuenta = numeroCuenta;
    this.saldo = saldoInicial;
}
```

En la implementación completa del taller se exige inicializar también titular y crédito;
se utiliza su constructor de cuatro parámetros en lugar de agregar este constructor parcial:

```java
CuentaBancaria cuenta = new CuentaBancaria("A-101", "Titular de ejemplo", 500.0, null);
```

## Caso 4: sombreado y ausencia de this

```java
public Vehiculo(int velocidadMaxima, boolean encendido) {
    velocidadMaxima = velocidadMaxima;
    encendido = encendido;
}
```

Para new Vehiculo(200, true), el ejemplo imprime 0 y false. Los parámetros ocultan
los atributos del mismo nombre: cada asignación escribe el parámetro sobre sí mismo.
Los atributos de instancia siguen con sus valores predeterminados: 0 para int y false
para boolean. No es un error de compilación, sino un error lógico.

Corrección del constructor del caso aislado:

```java
public Vehiculo(int velocidadMaxima, boolean encendido) {
    this.velocidadMaxima = velocidadMaxima;
    this.encendido = encendido;
}
```

Con esta corrección el ejemplo imprimiría 200 y true. El proyecto aplica el mismo
principio a todos los atributos en su constructor completo de cinco parámetros.

## Cómo observarlo en IntelliJ IDEA

Coloca breakpoints en los constructores de Vehiculo y CuentaBancaria. Ejecuta Principal
con Debug, usa Step Over e inspecciona this y los parámetros en Variables. Puedes
añadir Watches como this.saldo o this.limiteCredito. Observa que null se comprueba
antes del desempaquetado. Para revisar un error de compilación, trabaja en una copia
temporal independiente, evitando introducir ejemplos incorrectos en las fuentes entregables.
