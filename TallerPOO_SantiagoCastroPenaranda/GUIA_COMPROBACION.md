# Guía de comprobación

## Compilación y demostración

En IntelliJ, selecciona JDK 21, carga pom.xml y usa Build > Build Project.
Ejecuta Principal: debe mostrar tres vehículos, estudiantes y cuentas. Revisa el
vehículo por defecto: Genérico, Sin Modelo, 0.00, 120 y false. Los otros dos deben
conservar los valores de sus constructores. Revisa los tres tipos de constructor
de CuentaBancaria en el código y sus estados iniciales antes de las operaciones con Debug.

Los depósitos y retiros de Principal deben producir true, true, true, true, false,
false y true, en ese orden. Los saldos finales deben ser 200.00, -70.00 y 400.00.
La conversión de ejemplo de 100.00 a tasa 0.92 debe dar 92.00 EUR.

## Prueba manual del menú

Ejecuta SistemaGestionCuentas desde el inicio para cada caso independiente.
Escribe cada entrada en una línea y pulsa Enter.

| Caso | Entradas en orden | Resultado esperado |
|---|---|---|
| Reporte inicial | 3 | Tres cuentas, saldos 0.00, 0.00 y 500.00; total 500.00 |
| Menú inválido | hola, 9, 3 | Dos avisos y luego reporte, sin cierre inesperado |
| Cuenta inexistente | 1, 9999 | Error de cuenta inexistente, regresa al menú |
| Depósito válido | 1, 000000, 50.50, 3 | Saldo 50.50 y total 550.50 |
| Montos inválidos | 1, 000000, texto, 0, -5, NaN, Infinity, 25 | Rechaza cada valor inválido; acepta 25 |
| Retiro con crédito | 2, 1001, 80, 3 | Saldo -80.00; crédito sigue 100.00 |
| Exceso de crédito | 2, 1001, 101 | Rechaza; saldo continúa 0.00 |
| Retiro sin crédito | 2, 1002, 50, 3 | Saldo 450.00; crédito null se trata como cero |
| Fondos insuficientes sin crédito | 2, 1002, 501 | Rechaza y conserva saldo 500.00 |
| Retiro con saldo cero y crédito cero | 2, 000000, 1 | Rechaza y conserva saldo cero |
| Salir | 4 | Mensaje de cierre, sin repetir el menú |

Los números pueden mostrarse con coma decimal según la configuración regional.
Para las entradas se solicita punto decimal. Los intentos de montos inválidos no
modifican la cuenta; se vuelve a pedir el monto de la misma operación.

## Verificación realizada

El 16/10/2026 se compiló el proyecto con el JDK 21 instalado, usando javac
con -encoding UTF-8 y -Xlint:all, sin errores ni advertencias. Se ejecutaron
45 comprobaciones de clases: valores de constructores, this, reporte de estudiante,
depósitos y retiros válidos e inválidos, límites exactos de crédito, crédito null,
ausencia de cambios tras rechazos, montos no finitos, desbordamiento, conversión
y constructor privado de la utilidad. Todas pasaron.

Se ejecutó Principal y se verificaron los siete resultados de operaciones, los
tres saldos finales y la conversión. Se ejecutó el menú con entradas no numéricas,
opción fuera de rango, cuenta inexistente, montos no positivos, NaN, Infinity,
desbordamiento numérico de entrada, depósito, retiros con y sin crédito, tres
rechazos por fondos insuficientes, reportes inicial/final y salida. El total
final esperado y observado fue 420.50. También se comprobó el cierre por fin
de entrada y el uso exclusivo de printf en las fuentes.

Estas ejecuciones se hicieron con el JDK instalado; no se abrió la interfaz
de IntelliJ ni se realizó una compilación mediante Maven.
