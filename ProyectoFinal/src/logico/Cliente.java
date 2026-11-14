package logico;

import java.util.ArrayList;
import java.util.Date;

public class Cliente extends Persona {

	private String codigo;
	private Date fechaRegistro;
	private boolean activo;
	private ArrayList<Factura> lasFacturas;

	public Cliente(String cedula, String nombre, String apellido, String telefono, String correo, String direccion,
			String codigo, Date fechaRegistro) {
		super(cedula, nombre, apellido, telefono, correo, direccion);
		this.codigo = codigo;
		this.fechaRegistro = fechaRegistro;
		this.activo = true;
		this.lasFacturas = new ArrayList<>();
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public ArrayList<Factura> getLasFacturas() {
		return lasFacturas;
	}

	public void setLasFacturas(ArrayList<Factura> lasFacturas) {
		this.lasFacturas = lasFacturas;
	}

}