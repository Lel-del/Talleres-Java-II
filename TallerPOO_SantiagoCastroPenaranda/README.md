# Taller POO: clases y objetos en Java

Estudiante: Santiago Castro Peñaranda. Módulo: M4.
Docente e institución: no proporcionados.
Proyecto: TallerPOO_SantiagoCastroPenaranda.
Paquete: com.taller.poo. Java 21, sin bibliotecas externas.

## Diseño y archivos

Vehiculo, Estudiante y CuentaBancaria representan entidades con atributos privados.
Sus constructores asignan el estado inicial. Los getters permiten consultar el estado;
los depósitos y retiros lo modifican mediante métodos que validan la operación.
ConversorMoneda contiene operaciones estáticas y no puede instanciarse.
Principal demuestra las clases; SistemaGestionCuentas permite operar por teclado.

```text
TallerPOO_SantiagoCastroPenaranda/
├── pom.xml
├── README.md
├── ANALISIS_DEPURACION.md
├── GUIA_COMPROBACION.md
└── src/main/java/com/taller/poo/
    ├── Vehiculo.java
    ├── Estudiante.java
    ├── CuentaBancaria.java
    ├── ConversorMoneda.java
    ├── Principal.java
    └── SistemaGestionCuentas.java
```

## Abrir en IntelliJ IDEA

1. Abre esta carpeta. Si IntelliJ no reconoce las fuentes, abre pom.xml como proyecto
   o pulsa sobre él con el botón derecho y selecciona Add as Maven Project.
2. En File > Project Structure, selecciona un JDK 21.
3. Espera a que IntelliJ termine de cargar el proyecto.
4. En src/main/java/com/taller/poo, abre Principal.java y ejecuta su método main.
5. Para el menú interactivo, ejecuta el main de SistemaGestionCuentas.java.
6. Escribe en la consola inferior y pulsa Enter después de cada respuesta.

Principal debe mostrar tres vehículos, tres estudiantes, tres cuentas, resultados de
operaciones y una conversión de ejemplo. Los saldos finales son 200.00, -70.00 y 400.00.

## Uso del sistema de cuentas

Al ejecutar SistemaGestionCuentas se crean estas cuentas ficticias:

| Cuenta | Saldo inicial | Límite de crédito |
|---|---:|---:|
| 000000 | 0.00 | 0.00 |
| 1001 | 0.00 | 100.00 |
| 1002 | 500.00 | null (sin crédito disponible) |

El menú permite depositar, retirar, consultar el reporte y salir. Los cambios duran
solo durante esa ejecución. Principal y SistemaGestionCuentas tienen objetos distintos:
las operaciones de Principal no cambian los saldos iniciales del menú.

Usa punto decimal al ingresar montos. El formato mostrado usa la configuración regional
de Java, por lo que puede presentar punto o coma como separador decimal.

El crédito es un límite de sobregiro. Por ejemplo, retirar 50.00 de la cuenta 1001
deja saldo -50.00 y conserva límite 100.00: quedan 50.00 disponibles. El crédito no
se suma al saldo al crear la cuenta ni se reduce por separado al retirar.

## Requisitos y decisiones de implementación

Son requisitos del taller las entidades, atributos privados, constructores, this,
StringBuilder, métodos de instancia y estáticos, dos ejecutables, arreglo de tres
cuentas, menú do-while/switch, búsquedas, validaciones y salida con printf.

Decisiones adicionales: Java 21 y estructura Maven sin dependencias; nombres y saldos
de ejemplo; getters; lectura con nextLine y conversión controlada; fin de entrada
como salida limpia; rechazo de NaN, infinito y depósitos que desborden double.
El constructor de cuenta exige saldo finito y crédito null o finito no negativo.
El conversor también devuelve 0.0 para entradas o resultados no finitos.

Se usa double porque lo exige el taller. Double permite distinguir crédito no
asignado (null) de un límite explícito de cero. Los constructores sin parámetros
delegan con this(...) al constructor completo para evitar duplicar asignaciones.

El nombre de Santiago identifica al autor; sus calificaciones y matrícula son ficticias.
Las demás personas, cuentas, vehículos y tasas son datos de demostración. No se afirma
que la tasa 0.92 sea una tasa actual.

Consulta ANALISIS_DEPURACION.md para los cuatro casos y GUIA_COMPROBACION.md para
las verificaciones y los pasos manuales.
