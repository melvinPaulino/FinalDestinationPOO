package hilos;

import java.io.IOException;
import java.util.Date;

import logico.Altice;

public class PruebaHilos {

    public static void main(String[] args)
            throws InterruptedException,IOException, ClassNotFoundException {

        HilosFacturacion hilo1 =
                new HilosFacturacion(new Date(), 15);

        HilosFacturacion hilo2 =
                new HilosFacturacion(new Date(), 15);

        hilo1.start();
        hilo2.start();

        hilo1.join();
        hilo2.join();


        Altice.cargarDatos();

        HilosGuardar hilo = new HilosGuardar();

        hilo.start();

        System.out.println("Guardando información...");

        hilo.join();

        if (hilo.isGuardadoExitoso()) {
            System.out.println("Datos guardados correctamente.");
        } else {
            System.out.println("Error al guardar los datos.");
            System.out.println(hilo.getError().getMessage());
        }

         Altice.cargarDatos();

        HiloMorosidad hiloM = new HiloMorosidad(5000);

        hiloM.start();

        System.out.println("Hilo de morosidad iniciado.");

        Thread.sleep(16000);

        hiloM.interrupt();

        hilo.join();

        System.out.println("Hilo de morosidad finalizado.");

        System.out.println("¿Sigue activo? " + hiloM.isAlive());
    }
    }