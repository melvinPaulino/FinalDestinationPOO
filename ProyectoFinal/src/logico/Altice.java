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
	private ArrayList<Servicio> losServicios;
	private ArrayList<Plan> losPlanes;

	private static int generadorIdpersona = 1;
	private static int generadorIdFactura = 1;

	private static Altice instancia = null;

	// Ubicacion del fichero de datos (relativa a la carpeta desde donde se ejecuta
	// el programa)
	private static final String CARPETA_DATOS = "datos";
	private static final String ARCHIVO_DATOS = "altice.dat";

	private Altice() {
		super();
		this.lasPersonas = new ArrayList<>();
		this.lasFacturas = new ArrayList<>();
		this.losServicios = new ArrayList<>();
		this.losPlanes = new ArrayList<>();
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

	public ArrayList<Persona> getLasPersonas() {
		return lasPersonas;
	}

	public void setLasPersonas(ArrayList<Persona> lasPersonas) {
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

	public ArrayList<Servicio> getLosServicios() {
		return losServicios;
	}

	public void setLosServicios(ArrayList<Servicio> losServicios) {
		this.losServicios = losServicios;
	}

	public ArrayList<Plan> getLosPlanes() {
		return losPlanes;
	}

	public void setLosPlanes(ArrayList<Plan> losPlanes) {
		this.losPlanes = losPlanes;
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

	// Necesario para el primer inicio de sesion: si no hay administrador, esa
	// cuenta se crea
	public boolean existeAdministrador() {
		for (Persona u : lasPersonas) {
			if (u instanceof Empleado) {
				Empleado empleado = (Empleado) u;
				if (empleado.isEsAdmin()) {
					return true;
				}
			}
		}
		return false;
	}

	// Registra una persona solo si sus credenciales estan configuradas y son
	// unicas.
	public boolean registrarPersona(Persona nueva) {
		if (nueva == null || nueva.getUsuario() == null || nueva.getUsuario().trim().isEmpty()
				|| nueva.getCedula() == null || nueva.getCedula().trim().isEmpty())
			return false;
		for (Persona p : lasPersonas) {
			if (p.getUsuario() != null && p.getUsuario().equalsIgnoreCase(nueva.getUsuario()))
				return false;
			if (p.getCedula() != null && soloDigitos(p.getCedula()).equals(soloDigitos(nueva.getCedula())))
				return false;
		}
		lasPersonas.add(nueva);
		return true;
	}

	private static String soloDigitos(String valor) {
		return valor.replaceAll("[^0-9]", "");
	}

	// Devuelve la persona autenticada, o null si las credenciales son invalidas.
	public Persona autenticar(String credencial, String clave) {
		for (Persona p : lasPersonas) {
			boolean activo = (p instanceof Empleado && ((Empleado) p).isActivo())
					|| (p instanceof Cliente && ((Cliente) p).isActivo());
			if (activo && p.credencialCoincide(credencial) && p.claveCoincide(clave))
				return p;
		}
		return null;
	}

	// Guarda todo el sistema (esta instancia de Altice y, con ella, todos sus
	// objetos) en datos/altice.dat
	public void guardarDatos() throws IOException {
		File carpeta = new File(CARPETA_DATOS);
		if (!carpeta.exists() && !carpeta.mkdirs()) {
			throw new IOException("No se pudo crear la carpeta " + CARPETA_DATOS);
		}
		File destino = new File(carpeta, ARCHIVO_DATOS);
		File temporal = new File(carpeta, ARCHIVO_DATOS + ".tmp");

		// Se escribe primero en un archivo temporal: si algo falla a mitad, el archivo
		// anterior queda intacto
		try (ObjectOutputStream salida = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(temporal)))) {
			salida.writeObject(this);
			salida.writeInt(generadorIdpersona);
			salida.writeInt(generadorIdFactura);
		}
		Files.move(temporal.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	// Carga el sistema desde datos/altice.dat. Si el archivo no existe (primera
	// ejecucion) no hace nada
	public static void cargarDatos() throws IOException, ClassNotFoundException {

		File archivo = new File(CARPETA_DATOS, ARCHIVO_DATOS);
		if (!archivo.exists()) {
			return;
		}

		try (ObjectInputStream entrada = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream(archivo)))) {
			Altice leida = (Altice) entrada.readObject();

			// Si se carga un archivo antiguo, la nueva lista podría quedar en null, porque
			// no existía cuando se guardó el archivo.
			// Para asegurarnos de que esto no pase:
			if (leida.losServicios == null) {
				leida.losServicios = new ArrayList<>();
			}
			generadorIdpersona = entrada.readInt();
			generadorIdFactura = entrada.readInt();
			instancia = leida;
		} catch (IOException | ClassNotFoundException e) {
			Files.copy(archivo.toPath(), new File(CARPETA_DATOS, ARCHIVO_DATOS + ".bak").toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			throw e;
			// Si el archivo no se puede leer se conserva una copia para no perder lo que
			// contenga

		}
	}

	// -----------------------------CRUD Empleado-----------------------------

	// Registra un empleado verificando que no exista previamente.
	public boolean registrarEmpleado(Empleado empleado) {
		if (empleado == null) {
			return false;
		}

		return registrarPersona(empleado);
	}

	// Busca un empleado por su cedula o codigo.
	public Empleado buscarEmpleado(String identificador) {
		if (identificador == null || identificador.isBlank()) {
			return null;
		}

		for (Persona persona : lasPersonas) {
			if (persona instanceof Empleado) {
				Empleado empleado = (Empleado) persona;

				if (empleado.getCedula().equals(identificador)
						|| empleado.getCodigo().equalsIgnoreCase(identificador)) {
					return empleado;
				}
			}
		}

		return null;
	}

	// Devuelve todos los empleados registrados.
	public ArrayList<Empleado> obtenerEmpleados() {
		ArrayList<Empleado> empleados = new ArrayList<>();

		for (Persona persona : lasPersonas) {
			if (persona instanceof Empleado) {
				empleados.add((Empleado) persona);
			}
		}

		return empleados;
	}

	// Devuelve los empleados pertenecientes al tipo indicado.
	public <T extends Empleado> ArrayList<T> obtenerEmpleadosPorTipo(Class<T> tipo) {
		ArrayList<T> empleados = new ArrayList<>();

		for (Persona persona : lasPersonas) {
			if (tipo.isInstance(persona)) {
				empleados.add(tipo.cast(persona));
			}
		}

		return empleados;
	}

	// Actualiza los datos generales de un empleado.
	public boolean modificarEmpleado(String identificador, String nombre,
			String apellido, String telefono, String correo,
			String direccion, float salarioBase) {

		Empleado empleado = buscarEmpleado(identificador);

		if (empleado == null) {
			return false;
		}

		empleado.setNombre(nombre);
		empleado.setApellido(apellido);
		empleado.setTelefono(telefono);
		empleado.setCorreo(correo);
		empleado.setDireccion(direccion);
		empleado.setSalarioBase(salarioBase);

		return true;
	}

	// Desactiva un empleado sin eliminar su informacion.
	public boolean desactivarEmpleado(String identificador) {
		Empleado empleado = buscarEmpleado(identificador);

		if (empleado == null || !empleado.isActivo()) {
			return false;
		}

		// Impide desactivar al ultimo administrador activo.
		if (empleado.isEsAdmin() && contarAdministradoresActivos() <= 1) {
			return false;
		}

		empleado.setActivo(false);
		return true;
	}

	// Reactiva un empleado previamente desactivado.
	public boolean activarEmpleado(String identificador) {
		Empleado empleado = buscarEmpleado(identificador);

		if (empleado == null || empleado.isActivo()) {
			return false;
		}

		empleado.setActivo(true);
		return true;
	}

	// -----------------------------CRUD Admins-----------------------------

	// Devuelve los empleados con permisos administrativos.
	public ArrayList<Empleado> obtenerAdministradores() {
		ArrayList<Empleado> administradores = new ArrayList<>();

		for (Empleado empleado : obtenerEmpleados()) {
			if (empleado.isEsAdmin()) {
				administradores.add(empleado);
			}
		}

		return administradores;
	}

	// Cuenta los administradores que tienen su cuenta activa.
	public int contarAdministradoresActivos() {
		int cantidad = 0;

		for (Empleado empleado : obtenerAdministradores()) {
			if (empleado.isActivo()) {
				cantidad++;
			}
		}

		return cantidad;
	}

	// Concede permisos administrativos a un empleado.
	public boolean asignarAdministrador(String identificador) {
		Empleado empleado = buscarEmpleado(identificador);

		if (empleado == null || !empleado.isActivo()
				|| empleado.isEsAdmin()) {
			return false;
		}

		empleado.setEsAdmin(true);
		return true;
	}

	public boolean retirarAdministrador(String identificador) {
		Empleado empleado = buscarEmpleado(identificador);

		if (empleado == null || !empleado.isActivo()
				|| !empleado.isEsAdmin()) {
			return false;
		}

		// Impide retirar permisos al ultimo administrador activo.
		if (empleado.isActivo() && contarAdministradoresActivos() <= 1) {
			return false;
		}

		empleado.setEsAdmin(false);
		return true;

	}

	// -----------------------------CRUD Clientes-----------------------------

	// Registra un cliente verificando que no exista previamente.
	public boolean registrarCliente(Cliente cliente) {
		if (cliente == null) {
			return false;
		}

		// Verifica que el codigo del cliente no este repetido.
		for (Cliente c : obtenerClientes()) {
			if (c.getCodigo().equalsIgnoreCase(cliente.getCodigo())) {
				return false;
			}
		}

		return registrarPersona(cliente);
	}

	public Cliente buscarCliente(String identificador) {
		if (identificador == null || identificador.isBlank()) {
			return null;
		}

		for (Persona persona : lasPersonas) {
			if (persona instanceof Cliente) {
				Cliente cliente = (Cliente) persona;

				if (cliente.getCedula().equals(identificador)
						|| cliente.getCodigo().equalsIgnoreCase(identificador)) {
					return cliente;
				}
			}
		}

		return null;

	}

	public ArrayList<Cliente> listarClientesActivos() {
		ArrayList<Cliente> clientesActivos = new ArrayList<>();

		for (Persona persona : lasPersonas) {
			if (persona instanceof Cliente) {
				Cliente cliente = (Cliente) persona;

				if (cliente.isActivo()) {
					clientesActivos.add(cliente);
				}
			}

		}
		return clientesActivos;
	}

	private boolean modificarCliente(String identificador, String nombre,
			String apellido, String telefono, String correo,
			String direccion) {

		Cliente cliente = buscarCliente(identificador);

		if (cliente == null) {
			return false;
		}

		cliente.setNombre(nombre);
		cliente.setApellido(apellido);
		cliente.setTelefono(telefono);
		cliente.setCorreo(correo);
		cliente.setDireccion(direccion);

		return true;
	}

	public boolean desactivarCliente(String identificador) {
		Cliente cliente = buscarCliente(identificador);

		if (cliente == null || !cliente.isActivo()) {
			return false;
		}

		cliente.setActivo(false);
		return true;
	}

	public boolean activarCliente(String identificador) {
		Cliente cliente = buscarCliente(identificador);

		if (cliente == null || cliente.isActivo()) {
			return false;
		}

		cliente.setActivo(true);
		return true;
	}

	// Devuelve los contratos asociados a un cliente.
	public ArrayList<Contrato> obtenerContratosCliente(String identificador) {
		Cliente cliente = buscarCliente(identificador);

		if (cliente == null) {
			return new ArrayList<>();
		}

		return new ArrayList<>(cliente.getLosContratos());
	}

	// Devuelve las facturas asociadas a un cliente.
	public ArrayList<Factura> obtenerFacturasCliente(String identificador) {
		Cliente cliente = buscarCliente(identificador);

		if (cliente == null) {
			return new ArrayList<>();
		}

		return new ArrayList<>(cliente.getLasFacturas());
	}

	// Cambia el usuario de un cliente si no esta repetido.
	public boolean cambiarUsuarioCliente(String identificador,
			String nuevoUsuario) {

		Cliente cliente = buscarCliente(identificador);

		if (cliente == null || nuevoUsuario == null
				|| nuevoUsuario.isBlank()) {
			return false;
		}

		nuevoUsuario = nuevoUsuario.trim();

		for (Persona persona : lasPersonas) {
			if (persona != cliente && persona.getUsuario() != null
					&& persona.getUsuario().equalsIgnoreCase(nuevoUsuario)) {
				return false;
			}
		}

		cliente.setUsuario(nuevoUsuario);
		return true;
	}

	// Cambia la contrasena de un cliente verificando la anterior.
	public boolean cambiarClaveCliente(String identificador,
			String claveActual, String claveNueva) {

		Cliente cliente = buscarCliente(identificador);

		if (cliente == null || claveActual == null
				|| claveNueva == null || claveNueva.isBlank()) {
			return false;
		}

		if (!cliente.claveCoincide(claveActual)) {
			return false;
		}

		cliente.setClave(claveNueva);
		return true;
	}

	// -----------------------------CRUD Servicios-----------------------------

	// Registra un servicio si su codigo no existe previamente.
	public boolean registrarServicio(Servicio servicio) {
		if (servicio == null || servicio.getCodigo() == null
				|| servicio.getCodigo().isBlank()) {
			return false;
		}

		if (buscarServicio(servicio.getCodigo()) != null) {
			return false;
		}

		losServicios.add(servicio);
		return true;
	}

	// Busca un servicio por su codigo.
	public Servicio buscarServicio(String codigo) {
		if (codigo == null || codigo.isBlank()) {
			return null;
		}

		for (Servicio servicio : losServicios) {
			if (servicio.getCodigo().equalsIgnoreCase(codigo.trim())) {
				return servicio;
			}
		}

		return null;
	}

	// Devuelve todos los servicios registrados.
	public ArrayList<Servicio> obtenerServicios() {
		return new ArrayList<>(losServicios);
	}

	// Tambien podriamos tener la opcion de buscar solo los servicios activos
	public ArrayList<Servicio> obtenerServiciosActivos() {
		ArrayList<Servicio> activos = new ArrayList<>();

		for (Servicio servicio : losServicios) {
			if (servicio.isEstado()) {
				activos.add(servicio);
			}
		}

		return activos;
	}

	// Modifica los datos de un servicio existente.
	public boolean modificarServicio(String codigo, String nombre,
			String descripcion, float precio) {

		Servicio servicio = buscarServicio(codigo);

		if (servicio == null || nombre == null
				|| nombre.isBlank() || precio < 0) {
			return false;
		}

		servicio.setNombre(nombre);
		servicio.setDescripcion(descripcion);
		servicio.setPrecio(precio);

		return true;
	}

	// Desactiva un servicio sin eliminarlo del sistema.
	public boolean desactivarServicio(String codigo) {
		Servicio servicio = buscarServicio(codigo);

		if (servicio == null || !servicio.isEstado()) {
			return false;
		}

		servicio.desactivarServicio();
		return true;
	}

	// Reactiva un servicio previamente desactivado.
	public boolean activarServicio(String codigo) {
		Servicio servicio = buscarServicio(codigo);

		if (servicio == null || servicio.isEstado()) {
			return false;
		}

		servicio.activarServicio();
		return true;
	}

	// Elimina un servicio solamente si no pertenece a ningun plan.
	// Implementar despues de agregar losPlanes a Altice.
	public boolean eliminarServicio(String codigo) {
		Servicio servicio = buscarServicio(codigo);

		if (servicio == null) {
			return false;
		}

		for (Plan plan : losPlanes) {
			if (plan.getLosServicios().contains(servicio)) {
				return false;
			}
		}

		return losServicios.remove(servicio);
	}

	// -----------------------------CRUD Planes-----------------------------

	private boolean registrarPlan(Plan plan) {
		if (plan == null || plan.getCodigo() == null
				|| plan.getCodigo().isBlank()) {
			return false;
		}

		if (buscarPlan(plan.getCodigo()) != null) {
			return false;
		}

		losPlanes.add(plan);
		return true;
	}

	private Plan buscarPlan(String codigo) {
		if (codigo == null || codigo.isBlank()) {
			return null;
		}

		for (Plan plan : losPlanes) {
			if (plan.getCodigo().equalsIgnoreCase(codigo.trim())) {
				return plan;
			}
		}

		return null;
	}

	private ArrayList<Plan> obtenerPlanes() {
		return new ArrayList<>(losPlanes);
	}

	// Util para cuando registremos contratos
	private ArrayList<Plan> obtenerPlanesActivos() {
		ArrayList<Plan> activos = new ArrayList<>();

		for (Plan plan : losPlanes) {
			if (plan.isActivo()) {
				activos.add(plan);
			}
		}

		return activos;
	}

	// Modifica los datos generales de un plan.
	public boolean modificarPlan(String codigo, String nombre,
			String descripcion, float descuento) {

		Plan plan = buscarPlan(codigo);

		if (plan == null || nombre == null
				|| nombre.isBlank() || descuento < 0) {
			return false;
		}

		plan.setNombre(nombre);
		plan.setDescripcion(descripcion);
		plan.setDescuento(descuento);

		return true;
	}

	// Desactiva un plan sin eliminarlo.
	public boolean desactivarPlan(String codigo) {
		Plan plan = buscarPlan(codigo);

		if (plan == null || !plan.isActivo()) {
			return false;
		}

		plan.setActivo(false);
		return true;
	}

	// Reactiva un plan previamente desactivado.
	public boolean activarPlan(String codigo) {
		Plan plan = buscarPlan(codigo);

		if (plan == null || plan.isActivo()) {
			return false;
		}

		plan.setActivo(true);
		return true;
	}

	// Agrega un servicio activo a un plan evitando duplicados.
	public boolean agregarServicioPlan(String codigoPlan,
			String codigoServicio) {

		Plan plan = buscarPlan(codigoPlan);
		Servicio servicio = buscarServicio(codigoServicio);

		if (plan == null || servicio == null) {
			return false;
		}

		if (!plan.isActivo() || !servicio.isEstado()) {
			return false;
		}

		if (plan.getLosServicios().contains(servicio)) {
			return false;
		}

		plan.getLosServicios().add(servicio);
		return true;
	}

	// Quita un servicio de un plan.
	public boolean quitarServicioPlan(String codigoPlan,
			String codigoServicio) {

		Plan plan = buscarPlan(codigoPlan);
		Servicio servicio = buscarServicio(codigoServicio);

		if (plan == null || servicio == null) {
			return false;
		}

		return plan.getLosServicios().remove(servicio);
	}

	// Devuelve los servicios asociados a un plan.
	public ArrayList<Servicio> obtenerServiciosPlan(String codigoPlan) {
		Plan plan = buscarPlan(codigoPlan);

		if (plan == null) {
			return new ArrayList<>();
		}

		return new ArrayList<>(plan.getLosServicios());
	}

	// Calcula el precio de un plan sumando sus servicios.
	public float calcularPrecioPlan(String codigoPlan) {
		Plan plan = buscarPlan(codigoPlan);

		if (plan == null) {
			return 0;
		}

		return plan.calcularPrecio();
	}

	// Elimina un plan si no esta asociado a ningun contrato.
	public boolean eliminarPlan(String codigo) {
		Plan plan = buscarPlan(codigo);

		if (plan == null) {
			return false;
		}

		for (Cliente cliente : obtenerClientes()) {
			for (Contrato contrato : cliente.getLosContratos()) {
				if (contrato.getPlan() == plan) {
					return false;
				}
			}
		}

		return losPlanes.remove(plan);
	}

}