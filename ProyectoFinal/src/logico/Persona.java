package logico;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public abstract class Persona implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cedula;
	private String nombre;
	private String apellido;
	private String telefono;
	private String correo;
	private String direccion;
	private String usuario;
	private String sal;
	private String claveCifrada;

	public Persona(String cedula, String nombre, String apellido, String telefono, String correo, String direccion) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.correo = correo;
		this.direccion = direccion;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getUsuario() { return usuario; }

	public void setUsuario(String usuario) {
		if (usuario == null || usuario.trim().isEmpty())
			throw new IllegalArgumentException("El usuario no puede estar vacio");
		this.usuario = usuario.trim();
	}

	// La contrasena se guarda como hash con sal, nunca como texto plano.
	public void setClave(String clave) {
		if (clave == null || clave.isEmpty())
			throw new IllegalArgumentException("La clave no puede estar vacia");
		this.sal = generarSal();
		this.claveCifrada = cifrar(clave, sal);
	}

	public boolean claveCoincide(String clave) {
		return clave != null && sal != null && claveCifrada != null
			&& MessageDigest.isEqual(claveCifrada.getBytes(StandardCharsets.UTF_8),
				cifrar(clave, sal).getBytes(StandardCharsets.UTF_8));
	}

	// Se puede iniciar sesion con usuario o cedula (con o sin guiones).
	public boolean credencialCoincide(String credencial) {
		if (credencial == null || credencial.trim().isEmpty()) return false;
		if (usuario != null && usuario.equalsIgnoreCase(credencial.trim())) return true;
		String digitos = soloDigitos(credencial);
		return cedula != null && !digitos.isEmpty() && soloDigitos(cedula).equals(digitos);
	}

	private static String soloDigitos(String texto) {
		return texto.replaceAll("[^0-9]", "");
	}

	private static String generarSal() {
		byte[] bytes = new byte[16];
		new SecureRandom().nextBytes(bytes);
		return Base64.getEncoder().encodeToString(bytes);
	}

	private static String cifrar(String clave, String sal) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return Base64.getEncoder().encodeToString(
				md.digest((sal + clave).getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 no esta disponible", e);
		}
	}
}