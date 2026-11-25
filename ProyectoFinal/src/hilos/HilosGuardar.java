package hilos;

import java.io.IOException;
import logico.Altice;

public class HilosGuardar extends Thread {

    private boolean guardadoExitoso;
    private Exception error;

    public HilosGuardar() {
        super("Hilo-Guardar");
        this.guardadoExitoso = false;
        this.error = null;
    }

    // Para guardar la información de Altice en el archivo altice.dat sin detener
    // innecesariamente la interfaz gráfica.
    @Override
    public void run() {
        try {
            Altice.getInstancia().guardarDatos();
            guardadoExitoso = true;

        } catch (IOException | RuntimeException e) {
            error = e;
        }
    }

    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }

    public Exception getError() {
        return error;
    }

}
