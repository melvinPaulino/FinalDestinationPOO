package logico;
 
import java.util.Date;
 
public abstract class Empleado extends Persona {
 
	private String codigo;
	private float salarioBase;
	private Date fechaIngreso;
	private boolean activo;
 
	public Empleado(String cedula, String nombre, String apellido, String telefono, String correo, String direccion,
			String codigo, float salarioBase, Date fechaIngreso) {
		super(cedula, nombre, apellido, telefono, correo, direccion);
		this.codigo = codigo;
		this.salarioBase = salarioBase;
		this.fechaIngreso = fechaIngreso;
		this.activo = true;
	}
 
	public String getCodigo() {
		return codigo;
	}
 
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
 
	public float getSalarioBase() {
		return salarioBase;
	}
 
	public void setSalarioBase(float salarioBase) {
		this.salarioBase = salarioBase;
	}
 
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
 
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
 
	public boolean isActivo() {
		return activo;
	}
 
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
 
}