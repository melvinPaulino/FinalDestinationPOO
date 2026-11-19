package logico;
 
import java.util.Date;
 
public class Trabajador extends Empleado {
 
	private String especialidad;
	private float horasExtra;
	private float pagoHoraExtra;
 
	public Trabajador(String cedula, String nombre, String apellido, String telefono, String correo, String direccion,
			String codigo, float salarioBase, Date fechaIngreso, String especialidad, float pagoHoraExtra) {
		super(cedula, nombre, apellido, telefono, correo, direccion, codigo, salarioBase, fechaIngreso);
		this.especialidad = especialidad;
		this.horasExtra = 0;
		this.pagoHoraExtra = pagoHoraExtra;
	}
 
	public String getEspecialidad() {
		return especialidad;
	}
 
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
 
	public float getHorasExtra() {
		return horasExtra;
	}
 
	public void setHorasExtra(float horasExtra) {
		this.horasExtra = horasExtra;
	}
 
	public float getPagoHoraExtra() {
		return pagoHoraExtra;
	}
 
	public void setPagoHoraExtra(float pagoHoraExtra) {
		this.pagoHoraExtra = pagoHoraExtra;
	}
 
}