package logico;

import java.util.ArrayList;
import java.util.Date;

public class Altice {

	private ArrayList<Persona> lasPersonas;
	private ArrayList<Factura> lasFacturas;

	private static int generadorIdpersona = 1;
	private static int generadorIdFactura = 1;

	private static Altice instancia = null;

	private Altice() {
		super();
		this.lasPersonas = new ArrayList<>();
		this.lasFacturas = new ArrayList<>();
	}

	public static Altice getInstancia() {
		if (instancia == null) {
			instancia = new Altice();
		}
		return instancia;
	}

	public static void setInstancia(Altice instancia) {
		Altice.instancia = instancia;
	}

	public ArrayList<Persona> getLosClientes() {
		return lasPersonas;
	}

	public void setLosClientes(ArrayList<Persona> lasPersonas) {
		this.lasPersonas = lasPersonas;
	}

	public ArrayList<Factura> getLasFacturas() {
		return lasFacturas;
	}

	public void setLasFacturas(ArrayList<Factura> lasFacturas) {
		this.lasFacturas = lasFacturas;
	}

	public static int getGeneradorIdPersona() {
		return generadorIdpersona;
	}

	public static void setGeneradorIdPersona(int generadorIdPersona) {
		Altice.generadorIdpersona = generadorIdPersona;
	}

	public static int getGeneradorIdFactura() {
		return generadorIdFactura;
	}

	public static void setGeneradorIdFactura(int generadorIdFactura) {
		Altice.generadorIdFactura = generadorIdFactura;
	}

	public ArrayList<Cliente> obtenerClientes() {
		ArrayList<Cliente> clientes = new ArrayList<>();
		for (Persona persona : lasPersonas) {
			if (persona instanceof Cliente) {
				clientes.add((Cliente) persona);
			}
		}
		return clientes;
	}

	public boolean existeFactura(Cliente cliente, Date inicio, Date fin) {

    for (Factura factura : lasFacturas) {

        if (factura.getCliente() == cliente
                && factura.getPeriodoInicio().equals(inicio)
                && factura.getPeriodoFin().equals(fin)) {

            return true;
        }
    }

    return false;
}
public ArrayList<Factura> obtenerFacturasPendientes() {

    ArrayList<Factura> pendientes = new ArrayList<>();

    for (Factura factura : lasFacturas) {

        if (!factura.isPagada()) {
            pendientes.add(factura);
        }
    }

    return pendientes;
}

}