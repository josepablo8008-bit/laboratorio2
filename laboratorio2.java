// Importa la clase Scanner para leer datos desde la terminal.
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

// Declara la clase pública principal.
public class Laboratorio2 {

    // Define la capacidad máxima del bus.
    public static final int CAPACIDAD_BUS = 30;

    // Define el máximo de pasajeros que pueden subir al mismo tiempo.
    public static final int MAXIMO_SUBIENDO = 3;

    // Crea el semáforo para controlar que solo 3 pasajeros suban simultáneamente.
    public static Semaphore semaforoSubida = new Semaphore(MAXIMO_SUBIENDO, true);

    // Declara el semáforo de capacidad total del bus.
    public static Semaphore semaforoCapacidad;

    // Crea un objeto para sincronizar las secciones críticas.
    public static final Object bloqueo = new Object();

    // Declara el contador de pasajeros que ya están a bordo.
    public static int pasajerosAbordados = 0;

    // Declara el método principal.
    public static void main(String[] args) {

        // Crea el objeto Scanner para entrada por teclado.
        Scanner entrada = new Scanner(System.in);

        // Declara la variable para almacenar cuántos pasajeros ya están a bordo.
        int pasajerosIniciales;

        // Declara la variable para almacenar cuántos pasajeros intentarán abordar.
        int nuevosPasajeros;

        // Imprime el encabezado del sistema.
        System.out.println("==============================================");

        // Imprime el título del programa.
        System.out.println(" TERMINAL CENTRA NORTE - CONTROL DE ABORDAJE ");

        // Imprime una línea decorativa.
        System.out.println("==============================================");

        // Pide al usuario cuántos pasajeros ya están dentro del bus.
        System.out.print("Ingrese cuántos pasajeros ya están a bordo (0 a 30): ");

        // Lee el número de pasajeros iniciales.
        pasajerosIniciales = entrada.nextInt();

        // Valida que el número ingresado esté dentro del rango permitido.
        while (pasajerosIniciales < 0 || pasajerosIniciales > CAPACIDAD_BUS) {

            // Muestra mensaje de error si el valor está fuera de rango.
            System.out.print("Valor inválido. Ingrese un número entre 0 y 30: ");

            // Vuelve a leer el dato.
            pasajerosIniciales = entrada.nextInt();
        }

        // Asigna al contador general la cantidad inicial de pasajeros.
        pasajerosAbordados = pasajerosIniciales;

        // Calcula cuántos espacios quedan disponibles en el bus.
        int espaciosDisponibles = CAPACIDAD_BUS - pasajerosIniciales;

        // Inicializa el semáforo de capacidad con los espacios restantes.
        semaforoCapacidad = new Semaphore(espaciosDisponibles, true);

        // Pide al usuario cuántos pasajeros nuevos intentarán abordar.
        System.out.print("Ingrese cuántos pasajeros nuevos intentarán abordar: ");

        // Lee la cantidad de nuevos pasajeros.
        nuevosPasajeros = entrada.nextInt();

        // Valida que la cantidad no sea negativa.
        while (nuevosPasajeros < 0) {

            // Muestra mensaje de error.
            System.out.print("Valor inválido. Ingrese un número mayor o igual a 0: ");

            // Vuelve a leer el dato.
            nuevosPasajeros = entrada.nextInt();
        }

        // Crea el arreglo de hilos según la cantidad de nuevos pasajeros.
        Thread[] hilos = new Thread[nuevosPasajeros];

        // Muestra el estado inicial del sistema.
        System.out.println();

        // Imprime la capacidad total del bus.
        System.out.println("Capacidad máxima del bus: " + CAPACIDAD_BUS);

        // Imprime los pasajeros que ya estaban abordo.
        System.out.println("Pasajeros ya a bordo al iniciar: " + pasajerosIniciales);

        // Imprime los espacios libres al iniciar.
        System.out.println("Espacios disponibles al iniciar: " + espaciosDisponibles);

        // Imprime la cantidad máxima simultánea permitida.
        System.out.println("Máximo de pasajeros subiendo al mismo tiempo: " + MAXIMO_SUBIENDO);

        // Imprime una línea en blanco.
        System.out.println();

        // Inicia un ciclo para crear e iniciar cada nuevo pasajero.
        for (int i = 0; i < nuevosPasajeros; i++) {

            // Calcula el identificador del pasajero.
            int id = i + 1;

            // Crea un nuevo hilo de pasajero.
            hilos[i] = new Pasajero(id);

            // Inicia el hilo.
            hilos[i].start();

            // Inicia bloque try para pausar ligeramente la creación de hilos.
            try {

                // Espera 250 milisegundos para simular llegada progresiva.
                Thread.sleep(250);

            // Captura posibles interrupciones.
            } catch (InterruptedException e) {

                // Imprime mensaje de error.
                System.out.println("Error al crear pasajeros.");
            }
        }

        // Inicia un ciclo para esperar que todos los hilos terminen.
        for (int i = 0; i < nuevosPasajeros; i++) {

            // Inicia bloque try para usar join.
            try {

                // Espera la finalización del hilo actual.
                hilos[i].join();

            // Captura posibles interrupciones.
            } catch (InterruptedException e) {

                // Imprime mensaje de error.
                System.out.println("Error al esperar los hilos.");
            }
        }

        // Imprime una línea en blanco.
        System.out.println();

        // Imprime línea decorativa final.
        System.out.println("==============================================");

        // Imprime el mensaje de finalización.
        System.out.println("Abordaje finalizado.");

        // Imprime el total final de pasajeros a bordo.
        System.out.println("Total final de pasajeros a bordo: " + pasajerosAbordados);

        // Imprime cuántos pasajeros nuevos sí lograron subir.
        System.out.println("Pasajeros nuevos que lograron abordar: " + (pasajerosAbordados - pasajerosIniciales));

        // Calcula cuántos pasajeros nuevos fueron rechazados.
        System.out.println("Pasajeros nuevos rechazados: " + (nuevosPasajeros - (pasajerosAbordados - pasajerosIniciales)));

        // Imprime línea decorativa de cierre.
        System.out.println("==============================================");

        // Cierra el objeto Scanner.
        entrada.close();
    }
}

// Declara la clase Pasajero que extiende Thread.
class Pasajero extends Thread {

    // Declara el identificador del pasajero.
    private int id;

    // Crea un generador aleatorio para los tiempos de abordaje.
    private static Random random = new Random();

    // Declara el constructor de la clase.
    public Pasajero(int id) {

        // Asigna el identificador recibido al atributo del objeto.
        this.id = id;
    }

    // Sobrescribe el método run que ejecutará cada hilo.
    @Override
    public void run() {

        // Intenta reservar un espacio dentro del bus.
        boolean pudoReservarEspacio = Laboratorio2.semaforoCapacidad.tryAcquire();

        // Verifica si ya no había espacio.
        if (!pudoReservarEspacio) {

            // Entra en una sección sincronizada para imprimir ordenadamente.
            synchronized (Laboratorio2.bloqueo) {

                // Imprime mensaje de rechazo.
                System.out.println("Pasajero nuevo " + id + " rechazado: el bus ya está lleno.");
            }

            // Termina la ejecución del hilo.
            return;
        }

        // Inicia bloque try.
        try {

            // Adquiere permiso para subir, respetando máximo 3 simultáneos.
            Laboratorio2.semaforoSubida.acquire();

            // Entra a sección sincronizada.
            synchronized (Laboratorio2.bloqueo) {

                // Imprime mensaje de inicio de abordaje.
                System.out.println("Pasajero nuevo " + id + " está subiendo al bus...");
            }

            // Calcula tiempo aleatorio entre 2000 y 5000 milisegundos.
            int tiempo = 2000 + random.nextInt(3001);

            // Simula el proceso de validar boleto y acomodar equipaje.
            Thread.sleep(tiempo);

            // Entra a sección sincronizada para actualizar conteo.
            synchronized (Laboratorio2.bloqueo) {

                // Incrementa el total de pasajeros a bordo.
                Laboratorio2.pasajerosAbordados++;

                // Imprime mensaje de éxito con tiempo y total actual.
                System.out.println("Pasajero nuevo " + id + " abordó correctamente en "
                        + (tiempo / 1000.0) + " segundos. Total a bordo: "
                        + Laboratorio2.pasajerosAbordados);

                // Verifica si el bus llegó a su capacidad máxima.
                if (Laboratorio2.pasajerosAbordados == Laboratorio2.CAPACIDAD_BUS) {

                    // Imprime mensaje de capacidad completa.
                    System.out.println(">>> El bus llegó a su capacidad máxima de 30 pasajeros.");

                    // Imprime mensaje de cierre automático del abordaje.
                    System.out.println(">>> El abordaje se cierra automáticamente.");
                }
            }

        // Captura interrupciones.
        } catch (InterruptedException e) {

            // Entra a sección sincronizada para imprimir ordenadamente.
            synchronized (Laboratorio2.bloqueo) {

                // Imprime mensaje de interrupción.
                System.out.println("Pasajero nuevo " + id + " fue interrumpido.");
            }

        // Ejecuta siempre este bloque al final.
        } finally {

            // Libera el permiso del área de subida.
            Laboratorio2.semaforoSubida.release();
        }
    }
}
