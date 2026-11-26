package hilos;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import logico.Altice;

public class HilosGuardar extends Thread {

    private boolean guardadoExitoso;
    private Exception error;

    private final Consumer<HilosGuardar> alTerminar; //Represents an operation that accepts a single input argument and returns no result.

    private static final AtomicBoolean guardando = new AtomicBoolean(false);

    public HilosGuardar(Consumer<HilosGuardar> alTerminar) {
        super("Hilo-Guardar");
        this.alTerminar = alTerminar;
        this.guardadoExitoso = false;
        this.error = null;
    }

    public static boolean iniciar(Consumer<HilosGuardar> alTerminar) {
        if (!guardando.compareAndSet(false, true)) {
            return false;
        }

        HilosGuardar hilo = new HilosGuardar(alTerminar);
        hilo.start();
        return true;
    }

    @Override
    public void run() {
        try {
            Altice.getInstancia().guardarDatos();
            guardadoExitoso = true;

        } catch (IOException | RuntimeException e) {
            error = e;

        } finally {
            guardando.set(false);

            if (alTerminar != null) {
                SwingUtilities.invokeLater(() -> alTerminar.accept(this));
            }
        }
    }

    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }

    public Exception getError() {
        return error;
    }

    public static boolean isGuardando() {
        return guardando.get();
    }
}