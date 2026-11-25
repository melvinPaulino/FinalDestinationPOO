package hilos;

import java.util.Date;
import logico.Altice;

public class HiloMorosidad extends Thread {

    private final long intervalo;
    private volatile boolean ejecutando; // Permite que los cambios realizados sobre esta variable sean visibles para
                                         // los demás hilos.

    public HiloMorosidad(long intervalo) {
        super("Hilo-Morosidad");

        if (intervalo <= 0) {
            throw new IllegalArgumentException(
                    "El intervalo debe ser mayor que cero");
        }

        this.intervalo = intervalo;
        this.ejecutando = true;
    }

    @Override
    public void run() {
        try {
            while (ejecutando && !isInterrupted()) {

                Altice.getInstancia().verificarMorosidad(new Date());

                Thread.sleep(intervalo); // Suspende temporalmente la ejecución del hilo actual durante una cantidad
                                         // determinada de milisegundos.
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } finally {
            ejecutando = false;
        }
    }

    public void detenerHilo() {
        ejecutando = false;
        interrupt();
    }

    public boolean isEjecutando() {
        return ejecutando;
    }
}
