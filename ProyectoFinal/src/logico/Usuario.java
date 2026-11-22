package logico;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Rol {
		ADMIN,
		COMERCIAL
	}

	private String usuario;
	private String cedula;
	private String sal;
	private String claveCifrada;
	private Rol rol;
	private boolean activo;

	// La clave llega en texto normal, pero nunca se guarda asi: se almacena cifrada (SHA-256 con sal)
	public Usuario(String usuario, String cedula, String clave, Rol rol) {
		super();
		this.usuario = usuario;
		this.cedula = cedula;
		this.rol = rol;
		this.activo = true;
		setClave(clave);
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public void setClave(String clave) {
		this.sal = generarSal();
		this.claveCifrada = cifrar(clave, sal);
	}

	public boolean claveCoincide(String clave) {
		return MessageDigest.isEqual(claveCifrada.getBytes(StandardCharsets.UTF_8),
				cifrar(clave, sal).getBytes(StandardCharsets.UTF_8));
	}

	// Acepta el nombre de usuario (sin distinguir mayusculas) o la cedula (con o sin guiones)
	public boolean credencialCoincide(String credencial) {
		if (usuario.equalsIgnoreCase(credencial)) {
			return true;
		}
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
			byte[] hash = md.digest((sal + clave).getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 no esta disponible", e);
		}
	}

}
