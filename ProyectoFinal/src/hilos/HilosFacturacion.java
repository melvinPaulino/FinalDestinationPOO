package hilos;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import logico.Altice;

public class HilosFacturacion extends Thread {

    private final Date fechaProceso;
    private final int diasVencimiento;
    private final Consumer<RuntimeException> alFallar;
    private final Runnable alTerminar;

    private static final AtomicBoolean procesando = new AtomicBoolean(false);

    private HilosFacturacion(
            Date fechaProceso,
            int diasVencimiento,
            Consumer<RuntimeException> alFallar,
            Runnable alTerminar) {

        super("Hilo-Facturacion");

        this.fechaProceso = new Date(fechaProceso.getTime());
        this.diasVencimiento = diasVencimiento;
        this.alFallar = alFallar;
        this.alTerminar = alTerminar;
    }

    public static boolean iniciar(
            Date fechaProceso,
            int diasVencimiento,
            Consumer<RuntimeException> alFallar,
            Runnable alTerminar) {

        if (!procesando.compareAndSet(false, true)) {
            return false;
        }

        HilosFacturacion hilo = new HilosFacturacion(
                fechaProceso,
                diasVencimiento,
                alFallar,
                alTerminar);

        hilo.start();
        return true;
    }

    @Override
    public void run() {
        try {
            Altice.getInstancia().generarFacturas(
                    fechaProceso,
                    diasVencimiento);

        } catch (RuntimeException e) {
            if (alFallar != null) {
                alFallar.accept(e);
            }

        } finally {
            procesando.set(false);

            if (alTerminar != null) {
                alTerminar.run();
            }
        }
    }

    public static boolean isProcesando() {
        return procesando.get();
    }
}

/**
 * Para crear una factura:
 * HilosFacturacion hilo = new HilosFacturacion(new Date(), 15);
 * hilo.start();
 */
