package logico;

import java.util.ArrayList;

public class Altice {

	private ArrayList<Cliente> losClientes;
	private ArrayList<Factura> lasFacturas;

	private static int generadorIdCliente = 1;
	private static int generadorIdFactura = 1;

	private static Altice instancia = null;

	private Altice() {
		super();
		this.losClientes = new ArrayList<>();
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

	public ArrayList<Cliente> getLosClientes() {
		return losClientes;
	}

	public void setLosClientes(ArrayList<Cliente> losClientes) {
		this.losClientes = losClientes;
	}

	public ArrayList<Factura> getLasFacturas() {
		return lasFacturas;
	}

	public void setLasFacturas(ArrayList<Factura> lasFacturas) {
		this.lasFacturas = lasFacturas;
	}

	public static int getGeneradorIdCliente() {
		return generadorIdCliente;
	}

	public static void setGeneradorIdCliente(int generadorIdCliente) {
		Altice.generadorIdCliente = generadorIdCliente;
	}

	public static int getGeneradorIdFactura() {
		return generadorIdFactura;
	}

	public static void setGeneradorIdFactura(int generadorIdFactura) {
		Altice.generadorIdFactura = generadorIdFactura;
	}

}