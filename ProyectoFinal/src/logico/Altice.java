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
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class Altice implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<Persona> lasPersonas;
	private ArrayList<Factura> lasFacturas;
	private ArrayList<Servicio> losServicios;
	private ArrayList<Plan> losPlanes;

	private static final AtomicInteger generadorIdpersona = new AtomicInteger(1);
	private static final AtomicInteger generadorIdFactura = new AtomicInteger(1);

	private static volatile Altice instancia = null;
	private static final Object DATOS_LOCK = new Object();

	// Ubicacion del fichero de datos (relativa a la carpeta desde donde se ejecuta el programa)
	private static final String CARPETA_DATOS = "datos";
	private static final String ARCHIVO_DATOS = "altice.dat";

	private transient ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	private Altice() {
		super();
		this.lasPersonas = new ArrayList<>();
		this.lasFacturas = new ArrayList<>();
		this.losServicios = new ArrayList<>();
		this.losPlanes = new ArrayList<>();
	}

	// Reconstruye el lock transitorio al deserializar desde disco.
	private void readObject(ObjectInputStream entrada) throws IOException, ClassNotFoundException {
		entrada.defaultReadObject();
		this.lock = new ReentrantReadWriteLock();
	}

	private <T> T conLectura(Supplier<T> accion) {
		lock.readLock().lock();
		try {
			return accion.get();
		} finally {
			lock.readLock().unlock();
		}
	}

	private <T> T conEscritura(Supplier<T> accion) {
		lock.writeLock().lock();
		try {
			return accion.get();
		} finally {
			lock.writeLock().unlock();
		}
	}

	private void ejecutarConEscritura(Runnable accion) {
		lock.writeLock().lock();
		try {
			accion.run();
		} finally {
			lock.writeLock().unlock();
		}
	}

	public static Altice getInstancia() {
		Altice actual = instancia;
		if (actual == null) {
			synchronized (Altice.class) {
				actual = instancia;
				if (actual == null) {
					actual = new Altice();
					instancia = actual;
				}
			}
		}
		return actual;
	}

	public static synchronized void setInstancia(Altice instancia) {
		Altice.instancia = instancia;
	}

	public ArrayList<Persona> getLasPersonas() {
		return conLectura(() -> new ArrayList<>(lasPersonas));
	}

	public void setLasPersonas(ArrayList<Persona> lasPersonas) {
		ejecutarConEscritura(() -> this.lasPersonas = lasPersonas != null ? new ArrayList<>(lasPersonas) : new ArrayList<>());
	}

	public ArrayList<Factura> getLasFacturas() {
		return conLectura(() -> new ArrayList<>(lasFacturas));
	}

	public void setLasFacturas(ArrayList<Factura> lasFacturas) {
		ejecutarConEscritura(() -> this.lasFacturas = lasFacturas != null ? new ArrayList<>(lasFacturas) : new ArrayList<>());
	}

	public static int getGeneradorIdPersona() {
		return generadorIdpersona.get();
	}

	public ArrayList<Servicio> getLosServicios() {
		return conLectura(() -> new ArrayList<>(losServicios));
	}

	public void setLosServicios(ArrayList<Servicio> losServicios) {
		ejecutarConEscritura(() -> this.losServicios = losServicios != null ? new ArrayList<>(losServicios) : new ArrayList<>());
	}

	public ArrayList<Plan> getLosPlanes() {
		return conLectura(() -> new ArrayList<>(losPlanes));
	}

	public void setLosPlanes(ArrayList<Plan> losPlanes) {
		ejecutarConEscritura(() -> this.losPlanes = losPlanes != null ? new ArrayList<>(losPlanes) : new ArrayList<>());
	}

	public static void setGeneradorIdPersona(int generadorIdPersona) {
		Altice.generadorIdpersona.set(generadorIdPersona);
	}

	public static int getGeneradorIdFactura() {
		return generadorIdFactura.get();
	}

	public static void setGeneradorIdFactura(int generadorIdFactura) {
		Altice.generadorIdFactura.set(generadorIdFactura);
	}

	public ArrayList<Cliente> obtenerClientes() {
		return conLectura(() -> {
			ArrayList<Cliente> clientes = new ArrayList<>();
			for (Persona persona : lasPersonas) {
				if (persona instanceof Cliente) {
					clientes.add((Cliente) persona);
				}
			}
			return clientes;
		});
	}

	public boolean existeFactura(Cliente cliente, Date inicio, Date fin) {
		return conLectura(() -> {
			for (Factura factura : lasFacturas) {

				if (factura.getCliente() == cliente && factura.getPeriodoInicio().equals(inicio)
						&& factura.getPeriodoFin().equals(fin)) {

					return true;
				}
			}

			return false;
		});
	}

	public ArrayList<Factura> obtenerFacturasPendientes() {
		return conLectura(() -> {
			ArrayList<Factura> pendientes = new ArrayList<>();

			for (Factura factura : lasFacturas) {

				if (!factura.isPagada() && !factura.isAnulada()) {
					pendientes.add(factura);
				}
			}

			return pendientes;
		});
	}

	// Necesario para el primer inicio de sesion: si no hay administrador, esa
	// cuenta se crea
	public boolean existeAdministrador() {
		return conLectura(() -> {
			for (Persona u : lasPersonas) {
				if (u instanceof Empleado) {
					Empleado empleado = (Empleado) u;
					if (empleado.isEsAdmin()) {
						return true;
					}
				}
			}
			return false;
		});
	}

	// Registra una persona solo si sus credenciales estan configuradas y son
	// unicas.
	public boolean registrarPersona(Persona nueva) {
		return conEscritura(() -> {
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
		});
	}

	private static String soloDigitos(String valor) {
		return valor.replaceAll("[^0-9]", "");
	}

	// Devuelve la persona autenticada, o null si las credenciales son invalidas.
	public Persona autenticar(String credencial, String clave) {
		return conLectura(() -> {
			for (Persona p : lasPersonas) {
				boolean activo = (p instanceof Empleado && ((Empleado) p).isActivo())
						|| (p instanceof Cliente && ((Cliente) p).isActivo());
				if (activo && p.credencialCoincide(credencial) && p.claveCoincide(clave))
					return p;
			}
			return null;
		});
	}

	// Guarda todo el sistema (esta instancia de Altice y, con ella, todos sus objetos en altice.dat
	public void guardarDatos() throws IOException {
		synchronized (DATOS_LOCK) {
			lock.readLock().lock();
			try {
				File carpeta = new File(CARPETA_DATOS);
				if (!carpeta.exists() && !carpeta.mkdirs()) {
					throw new IOException("No se pudo crear la carpeta " + CARPETA_DATOS);
				}
				File destino = new File(carpeta, ARCHIVO_DATOS);
				File temporal = new File(carpeta, ARCHIVO_DATOS + ".tmp");

				// Se escribe primero en un archivo temporal: si algo falla a mitad, el archivo anterior queda intacto
				try (ObjectOutputStream salida = new ObjectOutputStream(
						new BufferedOutputStream(new FileOutputStream(temporal)))) {
					salida.writeObject(this);
					salida.writeInt(generadorIdpersona.get());
					salida.writeInt(generadorIdFactura.get());
				}
				Files.move(temporal.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} finally {
				lock.readLock().unlock();
			}
		}
	}

	// Carga el sistema desde datos/altice.dat. Si el archivo no existe (primera ejecucion) no hace nada
	public static void cargarDatos() throws IOException, ClassNotFoundException {
		synchronized (DATOS_LOCK) {
			File archivo = new File(CARPETA_DATOS, ARCHIVO_DATOS);
			if (!archivo.exists()) {
				return;
			}

			try (ObjectInputStream entrada = new ObjectInputStream(new BufferedInputStream(new FileInputStream(archivo)))) {
				Altice leida = (Altice) entrada.readObject();

				// Si se carga un archivo antiguo, la nueva lista podría quedar en null, porque no existía cuando se guardó el archivo.
				// Para asegurarnos de que esto no pase:
				
				if (leida.losServicios == null) {
					leida.losServicios = new ArrayList<>();
				}
				if (leida.losPlanes == null) {
					leida.losPlanes = new ArrayList<>();
				}
				int idPersona = entrada.readInt();
				int idFactura = entrada.readInt();
				synchronized (Altice.class) {
					generadorIdpersona.set(idPersona);
					generadorIdFactura.set(idFactura);
					instancia = leida;
				}
			} catch (IOException | ClassNotFoundException e) {
				Files.copy(archivo.toPath(), new File(CARPETA_DATOS, ARCHIVO_DATOS + ".bak").toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				throw e;
				
				// Si el archivo no se puede leer se conserva una copia para no perder lo que contenga

			}
		}
	}

	//CRUD Empleado

	// Registra un empleado verificando que no exista previamente.
	public boolean registrarEmpleado(Empleado empleado) {
		if (empleado == null) {
			return false;
		}

		return registrarPersona(empleado);
	}

	// Busca un empleado por su cedula o codigo.
	public Empleado buscarEmpleado(String identificador) {
		return conLectura(() -> {
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
		});
	}

	// Devuelve todos los empleados registrados.
	public ArrayList<Empleado> obtenerEmpleados() {
		return conLectura(() -> {
			ArrayList<Empleado> empleados = new ArrayList<>();

			for (Persona persona : lasPersonas) {
				if (persona instanceof Empleado) {
					empleados.add((Empleado) persona);
				}
			}

			return empleados;
		});
	}

	// Devuelve los empleados pertenecientes al tipo indicado.
	public <T extends Empleado> ArrayList<T> obtenerEmpleadosPorTipo(Class<T> tipo) {
		return conLectura(() -> {
			ArrayList<T> empleados = new ArrayList<>();

			for (Persona persona : lasPersonas) {
				if (tipo.isInstance(persona)) {
					empleados.add(tipo.cast(persona));
				}
			}

			return empleados;
		});
	}

	// Actualiza los datos generales de un empleado.
	public boolean modificarEmpleado(String identificador, String nombre, String apellido, String telefono,
			String correo, String direccion, float salarioBase) {
		return conEscritura(() -> {
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
		});
	}

	// Desactiva un empleado sin eliminar su informacion.
	public boolean desactivarEmpleado(String identificador) {
		return conEscritura(() -> {
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
		});
	}

	// Reactiva un empleado previamente desactivado.
	public boolean activarEmpleado(String identificador) {
		return conEscritura(() -> {
			Empleado empleado = buscarEmpleado(identificador);

			if (empleado == null || empleado.isActivo()) {
				return false;
			}

			empleado.setActivo(true);
			return true;
		});
	}

	// CRUD Admins

	// Devuelve los empleados con permisos administrativos.
	public ArrayList<Empleado> obtenerAdministradores() {
		return conLectura(() -> {
			ArrayList<Empleado> administradores = new ArrayList<>();

			for (Empleado empleado : obtenerEmpleados()) {
				if (empleado.isEsAdmin()) {
					administradores.add(empleado);
				}
			}

			return administradores;
		});
	}

	// Cuenta los administradores que tienen su cuenta activa.
	public int contarAdministradoresActivos() {
		return conLectura(() -> {
			int cantidad = 0;

			for (Empleado empleado : obtenerAdministradores()) {
				if (empleado.isActivo()) {
					cantidad++;
				}
			}

			return cantidad;
		});
	}

	// Concede permisos administrativos a un empleado.
	public boolean asignarAdministrador(String identificador) {
		return conEscritura(() -> {
			Empleado empleado = buscarEmpleado(identificador);

			if (empleado == null || !empleado.isActivo() || empleado.isEsAdmin()) {
				return false;
			}

			empleado.setEsAdmin(true);
			return true;
		});
	}

	public boolean retirarAdministrador(String identificador) {
		return conEscritura(() -> {
			Empleado empleado = buscarEmpleado(identificador);

			if (empleado == null || !empleado.isActivo() || !empleado.isEsAdmin()) {
				return false;
			}

			// Impide retirar permisos al ultimo administrador activo.
			if (empleado.isActivo() && contarAdministradoresActivos() <= 1) {
				return false;
			}

			empleado.setEsAdmin(false);
			return true;
		});
	}

	// CRUD Clientes                  

	// Registra un cliente verificando que no exista previamente.
	public boolean registrarCliente(Cliente cliente) {
		return conEscritura(() -> {
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
		});
	}

	public Cliente buscarCliente(String identificador) {
		return conLectura(() -> {
			if (identificador == null || identificador.isBlank()) {
				return null;
			}

			for (Persona persona : lasPersonas) {
				if (persona instanceof Cliente) {
					Cliente cliente = (Cliente) persona;

					if (cliente.getCedula().equals(identificador) || cliente.getCodigo().equalsIgnoreCase(identificador)) {
						return cliente;
					}
				}
			}

			return null;
		});
	}

	public ArrayList<Cliente> listarClientesActivos() {
		return conLectura(() -> {
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
		});
	}

	private boolean modificarCliente(String identificador, String nombre, String apellido, String telefono,
			String correo, String direccion) {
		return conEscritura(() -> {
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
		});
	}

	public boolean desactivarCliente(String identificador) {
		return conEscritura(() -> {
			Cliente cliente = buscarCliente(identificador);

			if (cliente == null || !cliente.isActivo()) {
				return false;
			}

			cliente.setActivo(false);
			return true;
		});
	}

	public boolean activarCliente(String identificador) {
		return conEscritura(() -> {
			Cliente cliente = buscarCliente(identificador);

			if (cliente == null || cliente.isActivo()) {
				return false;
			}

			cliente.setActivo(true);
			return true;
		});
	}

	// Devuelve los contratos asociados a un cliente.
	public ArrayList<Contrato> obtenerContratosCliente(String identificador) {
		return conLectura(() -> {
			Cliente cliente = buscarCliente(identificador);

			if (cliente == null) {
				return new ArrayList<>();
			}

			return new ArrayList<>(cliente.getLosContratos());
		});
	}

	// Devuelve las facturas asociadas a un cliente.
	public ArrayList<Factura> obtenerFacturasCliente(String identificador) {
		return conLectura(() -> {
			Cliente cliente = buscarCliente(identificador);

			if (cliente == null) {
				return new ArrayList<>();
			}

			return new ArrayList<>(cliente.getLasFacturas());
		});
	}

	// Cambia el usuario de un cliente si no esta repetido.
	public boolean cambiarUsuarioCliente(String identificador, String nuevoUsuario) {
		return conEscritura(() -> {
			Cliente cliente = buscarCliente(identificador);

			if (cliente == null || nuevoUsuario == null || nuevoUsuario.isBlank()) {
				return false;
			}

			String usuarioLimpio = nuevoUsuario.trim();

			for (Persona persona : lasPersonas) {
				if (persona != cliente && persona.getUsuario() != null
						&& persona.getUsuario().equalsIgnoreCase(usuarioLimpio)) {
					return false;
				}
			}

			cliente.setUsuario(usuarioLimpio);
			return true;
		});
	}

	// Cambia la contrasena de un cliente verificando la anterior.
	public boolean cambiarClaveCliente(String identificador, String claveActual, String claveNueva) {
		return conEscritura(() -> {
			Cliente cliente = buscarCliente(identificador);

			if (cliente == null || claveActual == null || claveNueva == null || claveNueva.isBlank()) {
				return false;
			}

			if (!cliente.claveCoincide(claveActual)) {
				return false;
			}

			cliente.setClave(claveNueva);
			return true;
		});
	}

	// CRUD Servicios

	// Registra un servicio si su codigo no existe previamente.
	public boolean registrarServicio(Servicio servicio) {
		return conEscritura(() -> {
			if (servicio == null || servicio.getCodigo() == null || servicio.getCodigo().isBlank()) {
				return false;
			}

			if (buscarServicio(servicio.getCodigo()) != null) {
				return false;
			}

			losServicios.add(servicio);
			return true;
		});
	}

	// Busca un servicio por su codigo.
	public Servicio buscarServicio(String codigo) {
		return conLectura(() -> {
			if (codigo == null || codigo.isBlank()) {
				return null;
			}

			for (Servicio servicio : losServicios) {
				if (servicio.getCodigo().equalsIgnoreCase(codigo.trim())) {
					return servicio;
				}
			}

			return null;
		});
	}

	// Devuelve todos los servicios registrados.
	public ArrayList<Servicio> obtenerServicios() {
		return conLectura(() -> new ArrayList<>(losServicios));
	}

	// Tambien podriamos tener la opcion de buscar solo los servicios activos
	public ArrayList<Servicio> obtenerServiciosActivos() {
		return conLectura(() -> {
			ArrayList<Servicio> activos = new ArrayList<>();

			for (Servicio servicio : losServicios) {
				if (servicio.isEstado()) {
					activos.add(servicio);
				}
			}

			return activos;
		});
	}

	// Modifica los datos de un servicio existente.
	public boolean modificarServicio(String codigo, String nombre, String descripcion, float precio) {
		return conEscritura(() -> {
			Servicio servicio = buscarServicio(codigo);

			if (servicio == null || nombre == null || nombre.isBlank() || precio < 0) {
				return false;
			}

			servicio.setNombre(nombre);
			servicio.setDescripcion(descripcion);
			servicio.setPrecio(precio);

			return true;
		});
	}

	// Desactiva un servicio sin eliminarlo del sistema.
	public boolean desactivarServicio(String codigo) {
		return conEscritura(() -> {
			Servicio servicio = buscarServicio(codigo);

			if (servicio == null || !servicio.isEstado()) {
				return false;
			}

			servicio.desactivarServicio();
			return true;
		});
	}

	// Reactiva un servicio previamente desactivado.
	public boolean activarServicio(String codigo) {
		return conEscritura(() -> {
			Servicio servicio = buscarServicio(codigo);

			if (servicio == null || servicio.isEstado()) {
				return false;
			}

			servicio.activarServicio();
			return true;
		});
	}

	// Elimina un servicio solamente si no pertenece a ningun plan.
	// Implementar despues de agregar losPlanes a Altice.
	public boolean eliminarServicio(String codigo) {
		return conEscritura(() -> {
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
		});
	}

	// CRUD Planes

	private boolean registrarPlan(Plan plan) {
		return conEscritura(() -> {
			if (plan == null || plan.getCodigo() == null || plan.getCodigo().isBlank()) {
				return false;
			}

			if (buscarPlan(plan.getCodigo()) != null) {
				return false;
			}

			losPlanes.add(plan);
			return true;
		});
	}

	private Plan buscarPlan(String codigo) {
		return conLectura(() -> {
			if (codigo == null || codigo.isBlank()) {
				return null;
			}

			for (Plan plan : losPlanes) {
				if (plan.getCodigo().equalsIgnoreCase(codigo.trim())) {
					return plan;
				}
			}

			return null;
		});
	}

	private ArrayList<Plan> obtenerPlanes() {
		return conLectura(() -> new ArrayList<>(losPlanes));
	}

	// Util para cuando registremos contratos
	private ArrayList<Plan> obtenerPlanesActivos() {
		return conLectura(() -> {
			ArrayList<Plan> activos = new ArrayList<>();

			for (Plan plan : losPlanes) {
				if (plan.isActivo()) {
					activos.add(plan);
				}
			}

			return activos;
		});
	}

	// Modifica los datos generales de un plan.
	public boolean modificarPlan(String codigo, String nombre, String descripcion, float descuento) {
		return conEscritura(() -> {
			Plan plan = buscarPlan(codigo);

			if (plan == null || nombre == null || nombre.isBlank() || descuento < 0) {
				return false;
			}

			plan.setNombre(nombre);
			plan.setDescripcion(descripcion);
			plan.setDescuento(descuento);

			return true;
		});
	}

	// Desactiva un plan sin eliminarlo.
	public boolean desactivarPlan(String codigo) {
		return conEscritura(() -> {
			Plan plan = buscarPlan(codigo);

			if (plan == null || !plan.isActivo()) {
				return false;
			}

			plan.setActivo(false);
			return true;
		});
	}

	// Reactiva un plan previamente desactivado.
	public boolean activarPlan(String codigo) {
		return conEscritura(() -> {
			Plan plan = buscarPlan(codigo);

			if (plan == null || plan.isActivo()) {
				return false;
			}

			plan.setActivo(true);
			return true;
		});
	}

	// Agrega un servicio activo a un plan evitando duplicados.
	public boolean agregarServicioPlan(String codigoPlan, String codigoServicio) {
		return conEscritura(() -> {
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
		});
	}

	// Quita un servicio de un plan.
	public boolean quitarServicioPlan(String codigoPlan, String codigoServicio) {
		return conEscritura(() -> {
			Plan plan = buscarPlan(codigoPlan);
			Servicio servicio = buscarServicio(codigoServicio);

			if (plan == null || servicio == null) {
				return false;
			}

			return plan.getLosServicios().remove(servicio);
		});
	}

	// Devuelve los servicios asociados a un plan.
	public ArrayList<Servicio> obtenerServiciosPlan(String codigoPlan) {
		return conLectura(() -> {
			Plan plan = buscarPlan(codigoPlan);

			if (plan == null) {
				return new ArrayList<>();
			}

			return new ArrayList<>(plan.getLosServicios());
		});
	}

	// Calcula el precio de un plan sumando sus servicios.
	public float calcularPrecioPlan(String codigoPlan) {
		return conLectura(() -> {
			Plan plan = buscarPlan(codigoPlan);

			if (plan == null) {
				return 0f;
			}

			return plan.calcularPrecio();
		});
	}

	// Elimina un plan si no esta asociado a ningun contrato.
	public boolean eliminarPlan(String codigo) {
		return conEscritura(() -> {
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
		});
	}

	public void generarFacturas(Date fechaProceso, int diasVencimiento) {
		ejecutarConEscritura(() -> {
			Calendar fechaCorte = Calendar.getInstance();
			fechaCorte.set(Calendar.HOUR_OF_DAY, 0);
			fechaCorte.set(Calendar.MINUTE, 0);
			fechaCorte.set(Calendar.SECOND, 0);
			fechaCorte.set(Calendar.MILLISECOND, 0);

			int diaActual = fechaCorte.get(Calendar.DAY_OF_MONTH);

			for (Cliente cliente : obtenerClientes()) {

				if (!cliente.isActivo()) {
					continue;
				}

				for (Contrato contrato : cliente.getLosContratos()) {

					if (!contrato.estaActivo()) {
						continue;
					}

					// Comprobar el dia de corte del contrato
					if (contrato.getDiaCorte() != diaActual) {
						continue;
					}

					// Definir el periodo de facturacion
					Calendar periodoFin = (Calendar) fechaCorte.clone();

					Calendar periodoInicio = (Calendar) fechaCorte.clone();
					periodoInicio.add(Calendar.MONTH, -1);
					periodoInicio.add(Calendar.DAY_OF_MONTH, 1);

					Date inicio = periodoInicio.getTime();
					Date fin = periodoFin.getTime();

					// Evitar duplicar facturas del mismo contrato
					boolean existe = false;

					for (Factura factura : lasFacturas) {

						if (factura.getCliente() == cliente && factura.getPeriodoInicio().equals(inicio)
								&& factura.getPeriodoFin().equals(fin) && factura.getContratos().contains(contrato)) {

							existe = true;
							break;
						}
					}

					if (existe) {
						continue;
					}

					// Calcular vencimiento
					Calendar vencimiento = (Calendar) fechaCorte.clone();
					vencimiento.add(Calendar.DAY_OF_MONTH, diasVencimiento);

					ArrayList<Contrato> contratosFactura = new ArrayList<>();
					contratosFactura.add(contrato);

					// Crear factura
					String codigo = "FAC-" + generadorIdFactura.getAndIncrement();

					Factura factura = new Factura(codigo, fechaProceso, vencimiento.getTime(), inicio, fin, 0, 0, 0, 0,
							cliente, false, null, contratosFactura);

					factura.calcularTotal();

					// Registrar factura en el sistema y el cliente
					lasFacturas.add(factura);
					cliente.getLasFacturas().add(factura);
				}
			}
		});
	}

	public ArrayList<Factura> obtenerFacturasVencidasCliente(Cliente cliente, Date fechaProceso) {
		return conLectura(() -> {
			ArrayList<Factura> vencidas = new ArrayList<>();

			for (Factura factura : cliente.getLasFacturas()) {

				if (!factura.isPagada() && !factura.isAnulada() && factura.getFechaVencimiento() != null
						&& factura.getFechaVencimiento().before(fechaProceso)) {

					vencidas.add(factura);
				}
			}

			return vencidas;
		});
	}

	public float calcularDeudaPendiente(Cliente cliente) {
		return conLectura(() -> {
			float deudaTotal = 0;

			for (Factura factura : cliente.getLasFacturas()) {

				if (!factura.isPagada() && !factura.isAnulada()) {
					deudaTotal += factura.getTotal();
				}
			}

			return deudaTotal;
		});
	}

	public void suspenderContratosCliente(Cliente cliente) {
		ejecutarConEscritura(() -> {
			for (Contrato contrato : cliente.getLosContratos()) {

				if (contrato.estaActivo()) {
					contrato.suspenderContrato();
				}
			}
		});
	}

	public void verificarMorosidad(Date fechaProceso) {
		ejecutarConEscritura(() -> {
			for (Cliente cliente : obtenerClientes()) {

				ArrayList<Factura> vencidas = obtenerFacturasVencidasCliente(cliente, fechaProceso);

				if (vencidas.size() >= 3) {

					float deuda = calcularDeudaPendiente(cliente);

					suspenderContratosCliente(cliente);

					System.out.println("Cliente moroso: " + cliente.getNombre());

					System.out.println("Deuda acumulada: RD$" + deuda);
				}
			}
		});
	}


	// Primer inicio de sesion

	// Crea la cuenta del primer administrador con el usuario y la clave ingresados en el login.
	// Devuelve null si ya existe un administrador o si los datos no son validos.
	public Administrativo registrarAdministradorInicial(String usuario, String clave) {
		return conEscritura(() -> {
			if (existeAdministrador() || usuario == null || usuario.isBlank() || clave == null || clave.isEmpty()) {
				return null;
			}

			int idActual = generadorIdpersona.get();

			// La cedula y los demas datos todavia no se conocen: se completan luego desde el sistema
			Administrativo admin = new Administrativo("SIN-CEDULA", "Administrador", "", "", "", "",
					"ADM-" + idActual, 0, new Date(), "Administracion", 0);
			admin.setUsuario(usuario);
			admin.setClave(clave);
			admin.setEsAdmin(true);

			if (!registrarPersona(admin)) {
				return null;
			}
			generadorIdpersona.compareAndSet(idActual, idActual + 1);
			return admin;
		});
	}

	// CRUD Contratos

	// Registra un contrato nuevo (cliente + plan + comercial) con estado ACTIVO.
	// Devuelve el contrato creado, o null si algun dato no es valido.
	public Contrato registrarContrato(String identificadorCliente, String codigoPlan, String identificadorComercial,
			int diaCorte) {
		return conEscritura(() -> {
			Cliente cliente = buscarCliente(identificadorCliente);
			Plan plan = buscarPlan(codigoPlan);
			Empleado comercial = buscarEmpleado(identificadorComercial);

			if (cliente == null || !cliente.isActivo()) {
				return null;
			}

			// El plan debe estar activo y tener al menos un servicio
			if (plan == null || !plan.isActivo() || plan.getLosServicios().isEmpty()) {
				return null;
			}

			// El contrato lo vende un comercial activo
			if (!(comercial instanceof Comercial) || !comercial.isActivo()) {
				return null;
			}

			// Se usa maximo el dia 28 para que el corte exista en todos los meses
			if (diaCorte < 1 || diaCorte > 28) {
				return null;
			}

			// Un cliente no puede tener dos contratos vigentes con el mismo plan
			for (Contrato existente : cliente.getLosContratos()) {
				if (existente.getPlan() == plan && !existente.estaCancelado()) {
					return null;
				}
			}

			// El cargo mensual queda fijado con el precio del plan al momento de firmar
			Contrato contrato = new Contrato(generarIdContrato(), cliente, plan, (Comercial) comercial, new Date(),
					Contrato.EstadoContrato.ACTIVO.name(), plan.calcularPrecio(), diaCorte, null);

			cliente.getLosContratos().add(contrato);
			return contrato;
		});
	}

	// Los contratos viven dentro de su cliente: el id siguiente se calcula con el mayor existente
	private String generarIdContrato() {
		return conLectura(() -> {
			int mayor = 0;

			for (Contrato contrato : obtenerContratos()) {
				String id = contrato.getIdContrato();

				if (id != null && id.startsWith("CON-")) {
					try {
						mayor = Math.max(mayor, Integer.parseInt(id.substring(4)));
					} catch (NumberFormatException e) {
						// Un id con otro formato se ignora
					}
				}
			}

			return "CON-" + (mayor + 1);
		});
	}

	// Busca un contrato por su id en todos los clientes.
	public Contrato buscarContrato(String idContrato) {
		return conLectura(() -> {
			if (idContrato == null || idContrato.isBlank()) {
				return null;
			}

			for (Contrato contrato : obtenerContratos()) {
				if (contrato.getIdContrato().equalsIgnoreCase(idContrato.trim())) {
					return contrato;
				}
			}

			return null;
		});
	}

	// Devuelve todos los contratos del sistema.
	public ArrayList<Contrato> obtenerContratos() {
		return conLectura(() -> {
			ArrayList<Contrato> contratos = new ArrayList<>();

			for (Cliente cliente : obtenerClientes()) {
				contratos.addAll(cliente.getLosContratos());
			}

			return contratos;
		});
	}

	// Devuelve los contratos que estan en el estado indicado.
	public ArrayList<Contrato> obtenerContratosPorEstado(Contrato.EstadoContrato estado) {
		return conLectura(() -> {
			ArrayList<Contrato> resultado = new ArrayList<>();

			for (Contrato contrato : obtenerContratos()) {
				if (contrato.getEstadoContrato().equalsIgnoreCase(estado.name())) {
					resultado.add(contrato);
				}
			}

			return resultado;
		});
	}

	// Devuelve los contratos vendidos por un comercial.
	public ArrayList<Contrato> obtenerContratosComercial(String identificadorComercial) {
		return conLectura(() -> {
			ArrayList<Contrato> resultado = new ArrayList<>();
			Empleado comercial = buscarEmpleado(identificadorComercial);

			if (comercial == null) {
				return resultado;
			}

			for (Contrato contrato : obtenerContratos()) {
				if (contrato.getComercialAsignado() == comercial) {
					resultado.add(contrato);
				}
			}

			return resultado;
		});
	}

	// Devuelve los contratos que usan un plan.
	public ArrayList<Contrato> obtenerContratosPlan(String codigoPlan) {
		return conLectura(() -> {
			ArrayList<Contrato> resultado = new ArrayList<>();
			Plan plan = buscarPlan(codigoPlan);

			if (plan == null) {
				return resultado;
			}

			for (Contrato contrato : obtenerContratos()) {
				if (contrato.getPlan() == plan) {
					resultado.add(contrato);
				}
			}

			return resultado;
		});
	}

	// Devuelve los contratos no cancelados cuyo plan incluye un servicio.
	public ArrayList<Contrato> obtenerContratosServicio(String codigoServicio) {
		return conLectura(() -> {
			ArrayList<Contrato> resultado = new ArrayList<>();
			Servicio servicio = buscarServicio(codigoServicio);

			if (servicio == null) {
				return resultado;
			}

			for (Contrato contrato : obtenerContratos()) {
				if (!contrato.estaCancelado() && contrato.getPlan().getLosServicios().contains(servicio)) {
					resultado.add(contrato);
				}
			}

			return resultado;
		});
	}

	// Devuelve los servicios que incluye el plan de un contrato.
	public ArrayList<Servicio> obtenerServiciosContrato(String idContrato) {
		return conLectura(() -> {
			Contrato contrato = buscarContrato(idContrato);

			if (contrato == null) {
				return new ArrayList<>();
			}

			return new ArrayList<>(contrato.getPlan().getLosServicios());
		});
	}

	// Devuelve los servicios que un cliente tiene contratados (contratos activos, sin repetir).
	public ArrayList<Servicio> obtenerServiciosCliente(String identificador) {
		return conLectura(() -> {
			ArrayList<Servicio> servicios = new ArrayList<>();

			for (Contrato contrato : obtenerContratosCliente(identificador)) {
				if (contrato.estaActivo()) {
					for (Servicio servicio : contrato.getPlan().getLosServicios()) {
						if (!servicios.contains(servicio)) {
							servicios.add(servicio);
						}
					}
				}
			}

			return servicios;
		});
	}

	// Modifica el plan, el comercial y el dia de corte de un contrato que no este cancelado.
	// El cargo mensual solo se recalcula si el plan cambia.
	public boolean modificarContrato(String idContrato, String codigoPlan, String identificadorComercial,
			int diaCorte) {
		return conEscritura(() -> {
			Contrato contrato = buscarContrato(idContrato);
			Plan plan = buscarPlan(codigoPlan);
			Empleado comercial = buscarEmpleado(identificadorComercial);

			if (contrato == null || contrato.estaCancelado()) {
				return false;
			}

			// Un plan inactivo solo es valido si el contrato ya lo tenia
			if (plan == null || plan.getLosServicios().isEmpty() || (!plan.isActivo() && plan != contrato.getPlan())) {
				return false;
			}

			// Un comercial inactivo solo es valido si el contrato ya lo tenia
			if (!(comercial instanceof Comercial)
					|| (!comercial.isActivo() && comercial != contrato.getComercialAsignado())) {
				return false;
			}

			if (diaCorte < 1 || diaCorte > 28) {
				return false;
			}

			for (Contrato otro : contrato.getCliente().getLosContratos()) {
				if (otro != contrato && otro.getPlan() == plan && !otro.estaCancelado()) {
					return false;
				}
			}

			if (plan != contrato.getPlan()) {
				contrato.setPlan(plan);
				contrato.setCargoFijoMensual(plan.calcularPrecio());
			}

			contrato.setComercialAsignado((Comercial) comercial);
			contrato.setDiaCorte(diaCorte);
			return true;
		});
	}

	// Suspende un contrato activo.
	public boolean suspenderContrato(String idContrato) {
		return conEscritura(() -> {
			Contrato contrato = buscarContrato(idContrato);

			if (contrato == null || !contrato.estaActivo()) {
				return false;
			}

			contrato.suspenderContrato();
			return true;
		});
	}

	// Reactiva un contrato suspendido, siempre que el cliente siga activo y no tenga facturas vencidas.
	public boolean reactivarContrato(String idContrato) {
		return conEscritura(() -> {
			Contrato contrato = buscarContrato(idContrato);

			if (contrato == null || !contrato.estaSuspendido() || !contrato.getCliente().isActivo()) {
				return false;
			}

			if (!obtenerFacturasVencidasCliente(contrato.getCliente(), new Date()).isEmpty()) {
				return false;
			}

			contrato.reactivarContrato();
			return true;
		});
	}

	// Cancela un contrato (no se borra: queda en el historial del cliente).
	public boolean cancelarContrato(String idContrato) {
		return conEscritura(() -> {
			Contrato contrato = buscarContrato(idContrato);

			if (contrato == null || contrato.estaCancelado()) {
				return false;
			}

			contrato.cancelarContrato();
			return true;
		});
	}

	// Elimina un contrato solo si todavia no tiene ninguna factura.
	public boolean eliminarContrato(String idContrato) {
		return conEscritura(() -> {
			Contrato contrato = buscarContrato(idContrato);

			if (contrato == null) {
				return false;
			}

			for (Factura factura : lasFacturas) {
				if (factura.getContratos().contains(contrato)) {
					return false;
				}
			}

			return contrato.getCliente().getLosContratos().remove(contrato);
		});
	}

	//CRUD Facturas

	// Crea la factura de un contrato activo para el mes que termina en fechaProceso.
	// Devuelve null si el contrato no se puede facturar o si ese periodo ya fue facturado.
	public Factura facturarContrato(String idContrato, Date fechaProceso, int diasVencimiento) {
		return conEscritura(() -> {
			Contrato contrato = buscarContrato(idContrato);

			if (contrato == null || fechaProceso == null || diasVencimiento < 0) {
				return null;
			}

			if (!contrato.estaActivo() || !contrato.getCliente().isActivo()) {
				return null;
			}

			Calendar fin = Calendar.getInstance();
			fin.setTime(fechaProceso);
			fin.set(Calendar.HOUR_OF_DAY, 0);
			fin.set(Calendar.MINUTE, 0);
			fin.set(Calendar.SECOND, 0);
			fin.set(Calendar.MILLISECOND, 0);

			Calendar inicio = (Calendar) fin.clone();
			inicio.add(Calendar.MONTH, -1);
			inicio.add(Calendar.DAY_OF_MONTH, 1);

			// Evita facturar dos veces el mismo contrato en el mismo periodo
			for (Factura existente : lasFacturas) {
				if (!existente.isAnulada() && existente.getContratos().contains(contrato)
						&& existente.getPeriodoInicio().equals(inicio.getTime())
						&& existente.getPeriodoFin().equals(fin.getTime())) {
					return null;
				}
			}

			Calendar vencimiento = (Calendar) fin.clone();
			vencimiento.add(Calendar.DAY_OF_MONTH, diasVencimiento);

			ArrayList<Contrato> contratosFactura = new ArrayList<>();
			contratosFactura.add(contrato);

			Factura factura = new Factura("FAC-" + generadorIdFactura.getAndIncrement(), fechaProceso,
					vencimiento.getTime(), inicio.getTime(), fin.getTime(), 0, 0, 0, 0, contrato.getCliente(), false,
					null, contratosFactura);

			factura.calcularTotal();

			lasFacturas.add(factura);
			contrato.getCliente().getLasFacturas().add(factura);
			return factura;
		});
	}

	// Busca una factura por su codigo.
	public Factura buscarFactura(String codigo) {
		return conLectura(() -> {
			if (codigo == null || codigo.isBlank()) {
				return null;
			}

			for (Factura factura : lasFacturas) {
				if (factura.getCodigo().equalsIgnoreCase(codigo.trim())) {
					return factura;
				}
			}

			return null;
		});
	}

	// Devuelve todas las facturas del sistema (pagadas, pendientes y anuladas).
	public ArrayList<Factura> obtenerFacturas() {
		return conLectura(() -> new ArrayList<>(lasFacturas));
	}

	public ArrayList<Factura> obtenerFacturasPagadas() {
		return conLectura(() -> {
			ArrayList<Factura> pagadas = new ArrayList<>();

			for (Factura factura : lasFacturas) {
				if (factura.isPagada()) {
					pagadas.add(factura);
				}
			}

			return pagadas;
		});
	}

	public ArrayList<Factura> obtenerFacturasAnuladas() {
		return conLectura(() -> {
			ArrayList<Factura> anuladas = new ArrayList<>();

			for (Factura factura : lasFacturas) {
				if (factura.isAnulada()) {
					anuladas.add(factura);
				}
			}

			return anuladas;
		});
	}

	// Facturas no pagadas ni anuladas cuyo vencimiento ya paso.
	public ArrayList<Factura> obtenerFacturasVencidas(Date fechaProceso) {
		return conLectura(() -> {
			ArrayList<Factura> vencidas = new ArrayList<>();

			for (Factura factura : lasFacturas) {
				if (!factura.isPagada() && !factura.isAnulada() && factura.getFechaVencimiento() != null
						&& factura.getFechaVencimiento().before(fechaProceso)) {
					vencidas.add(factura);
				}
			}

			return vencidas;
		});
	}

	// Facturas emitidas entre dos fechas (ambas incluidas). Sirve para los reportes por periodo.
	public ArrayList<Factura> obtenerFacturasPorPeriodo(Date desde, Date hasta) {
		return conLectura(() -> {
			ArrayList<Factura> resultado = new ArrayList<>();

			if (desde == null || hasta == null) {
				return resultado;
			}

			for (Factura factura : lasFacturas) {
				if (!factura.getFecha().before(desde) && !factura.getFecha().after(hasta)) {
					resultado.add(factura);
				}
			}

			return resultado;
		});
	}

	// Devuelve las facturas de un contrato.
	public ArrayList<Factura> obtenerFacturasContrato(String idContrato) {
		return conLectura(() -> {
			ArrayList<Factura> resultado = new ArrayList<>();
			Contrato contrato = buscarContrato(idContrato);

			if (contrato == null) {
				return resultado;
			}

			for (Factura factura : lasFacturas) {
				if (factura.getContratos().contains(contrato)) {
					resultado.add(factura);
				}
			}

			return resultado;
		});
	}

	// Marca una factura como pagada (no se puede pagar una factura anulada ni pagar dos veces).
	public boolean registrarPagoFactura(String codigo) {
		return conEscritura(() -> {
			Factura factura = buscarFactura(codigo);

			if (factura == null || factura.isPagada() || factura.isAnulada()) {
				return false;
			}

			factura.registrarPago();
			return true;
		});
	}

	// Cambia la fecha de vencimiento y el descuento de una factura que siga pendiente.
	// El subtotal no cambia: solo se recalculan el ITBIS y el total.
	public boolean modificarFactura(String codigo, Date nuevoVencimiento, float descuento) {
		return conEscritura(() -> {
			Factura factura = buscarFactura(codigo);

			if (factura == null || factura.isPagada() || factura.isAnulada()) {
				return false;
			}

			if (nuevoVencimiento == null || nuevoVencimiento.before(factura.getFecha())) {
				return false;
			}

			if (descuento < 0 || descuento > factura.getSubtotal()) {
				return false;
			}

			float baseImponible = factura.getSubtotal() - descuento;
			float itbis = baseImponible * Factura.TASA_ITBIS;

			factura.setFechaVencimiento(nuevoVencimiento);
			factura.setDescuento(descuento);
			factura.setItbis(itbis);
			factura.setTotal(baseImponible + itbis);
			return true;
		});
	}

	// Anula una factura que no este pagada (no se borra: queda en el historial).
	public boolean anularFactura(String codigo) {
		return conEscritura(() -> {
			Factura factura = buscarFactura(codigo);

			if (factura == null || factura.isPagada() || factura.isAnulada()) {
				return false;
			}

			factura.anular();
			return true;
		});
	}

}