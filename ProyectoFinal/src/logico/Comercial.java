package logico;
 
import java.util.Date;
 
public class Comercial extends Empleado {
 
	private float metaMensual;
	private float porcientoComision;
 
	public Comercial(String cedula, String nombre, String apellido, String telefono, String correo, String direccion,
			String codigo, float salarioBase, Date fechaIngreso, float metaMensual, float porcientoComision) {
		super(cedula, nombre, apellido, telefono, correo, direccion, codigo, salarioBase, fechaIngreso);
		this.metaMensual = metaMensual;
		this.porcientoComision = porcientoComision;
	}
 
	public float getMetaMensual() {
		return metaMensual;
	}
 
	public void setMetaMensual(float metaMensual) {
		this.metaMensual = metaMensual;
	}
 
	public float getPorcientoComision() {
		return porcientoComision;
	}
 
	public void setPorcientoComision(float porcientoComision) {
		this.porcientoComision = porcientoComision;
	}
 
}