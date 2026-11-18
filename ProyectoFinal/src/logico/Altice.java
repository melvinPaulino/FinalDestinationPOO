package logico;

import java.util.ArrayList;

public class Altice {

	private ArrayList<Persona> lasPersonas;
	private ArrayList<Factura> lasFacturas;

	private static int generadorIdpersona= 1;
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

	public void setLosClientes(ArrayList<Cliente> losClientes) {
		this.lasPersonas = lasPersonas;
	}

	public ArrayList<Factura> getLasFacturas() {
		return lasFacturas;
	}

	public void setLasFacturas(ArrayList<Factura> lasFacturas) {
		this.lasFacturas = lasFacturas;
	}

	public static int getGeneradorIdCliente() {
		return generadorIdpersona;
	}

	public static void setGeneradorIdCliente(int generadorIdCliente) {
		Altice.generadorIdpersona = generadorIdCliente;
	}

	public static int getGeneradorIdFactura() {
		return generadorIdFactura;
	}

	public static void setGeneradorIdFactura(int generadorIdFactura) {
		Altice.generadorIdFactura = generadorIdFactura;
	}

}