package logico;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;

public class Altice implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<Persona> lasPersonas;
	private ArrayList<Factura> lasFacturas;
	private ArrayList<Usuario> losUsuarios;

	private static int generadorIdpersona = 1;
	private static int generadorIdFactura = 1;

	private static Altice instancia = null;

	// Ubicacion del fichero de datos (relativa a la carpeta desde donde se ejecuta el programa)
	private static final String CARPETA_DATOS = "datos";
	private static final String ARCHIVO_DATOS = "altice.dat";

	private Altice() {
		super();
		this.lasPersonas = new ArrayList<>();
		this.lasFacturas = new ArrayList<>();
		this.losUsuarios = new ArrayList<>();
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

	public ArrayList<Usuario> getLosUsuarios() {return losUsuarios;}
	public void setLosUsuarios(ArrayList<Usuario> losUsuarios) {this.losUsuarios = losUsuarios;}

	// Necesario para el primer inicio de sesion: si no hay administrador, esa cuenta se crea
	public boolean existeAdministrador() {
		for (Usuario u : losUsuarios) {
			if (u.getRol() == Usuario.Rol.ADMIN) {
				return true;
			}
		}
		return false;
	}

	// Devuelve false si ya existe una cuenta con ese nombre de usuario
	public boolean registrarUsuario(Usuario nuevo) {
		for (Usuario u : losUsuarios) {
			if (u.getUsuario().equalsIgnoreCase(nuevo.getUsuario())) {
				return false;
			}
		}
		losUsuarios.add(nuevo);
		return true;
	}

	// credencial = nombre de usuario o cedula. Devuelve null si los datos no son validos
	public Usuario autenticar(String credencial, String clave, Usuario.Rol rol) {
		for (Usuario u : losUsuarios) {
			if (u.isActivo() && u.getRol() == rol && u.credencialCoincide(credencial) && u.claveCoincide(clave)) {
				return u;
			}
		}
		return null;
	}

	// Guarda todo el sistema (esta instancia de Altice y, con ella, todos sus objetos) en datos/altice.dat
	public void guardarDatos() throws IOException {
		File carpeta = new File(CARPETA_DATOS);
		if (!carpeta.exists() && !carpeta.mkdirs()) {throw new IOException("No se pudo crear la carpeta " + CARPETA_DATOS);}
		File destino = new File(carpeta, ARCHIVO_DATOS);
		File temporal = new File(carpeta, ARCHIVO_DATOS + ".tmp");

	// Se escribe primero en un archivo temporal: si algo falla a mitad, el archivo anterior queda intacto
		try (ObjectOutputStream salida = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(temporal)))) {
			salida.writeObject(this);
			salida.writeInt(generadorIdpersona);
			salida.writeInt(generadorIdFactura);
		}
		Files.move(temporal.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	// Carga el sistema desde datos/altice.dat. Si el archivo no existe (primera ejecucion) no hace nada
	public static void cargarDatos() throws IOException, ClassNotFoundException {
	
		File archivo = new File(CARPETA_DATOS, ARCHIVO_DATOS);
		if (!archivo.exists()) {return;}
		
		try (ObjectInputStream entrada = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(archivo)))) {
			Altice leida = (Altice) entrada.readObject();
			generadorIdpersona = entrada.readInt();
			generadorIdFactura = entrada.readInt();
			instancia = leida;
		} catch (IOException | ClassNotFoundException e) {
			Files.copy(archivo.toPath(), new File(CARPETA_DATOS, ARCHIVO_DATOS + ".bak").toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			throw e;
	// Si el archivo no se puede leer se conserva una copia para no perder lo que contenga

		}
	}

}