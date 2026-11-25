package hilos;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import logico.Altice;

public class HilosFacturacion extends Thread {

    private Date fechaProceso;
    private int diasVencimiento;

    private static final AtomicBoolean procesando = new AtomicBoolean(false);

    public HilosFacturacion(Date fechaProceso, int diasVencimiento) {
        super("Hilo-Facturacion");
        this.fechaProceso = new Date(fechaProceso.getTime());
        this.diasVencimiento = diasVencimiento;
    }

    public void run() {
        //Comprueba si el valor actual es false y, únicamente si lo es, lo cambia a true. 
        // Ambas operaciones se realizan de manera atómica.
        if (!procesando.compareAndSet(false, true)) {
            return;
        }

        try {
            Altice.getInstancia().generarFacturas(
                fechaProceso, diasVencimiento
            );

        } finally {
            procesando.set(false);
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
