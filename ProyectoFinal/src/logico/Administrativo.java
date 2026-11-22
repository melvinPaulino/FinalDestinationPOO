package logico;
 
import java.util.Date;
 
public class Administrativo extends Empleado {

	private static final long serialVersionUID = 1L;
 
	private String departamento;
	private float bonoMensual;
 
	public Administrativo(String cedula, String nombre, String apellido, String telefono, String correo,
			String direccion, String codigo, float salarioBase, Date fechaIngreso, String departamento,
			float bonoMensual) {
		super(cedula, nombre, apellido, telefono, correo, direccion, codigo, salarioBase, fechaIngreso);
		this.departamento = departamento;
		this.bonoMensual = bonoMensual;
	}
 
	public String getDepartamento() {
		return departamento;
	}
 
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
 
	public float getBonoMensual() {
		return bonoMensual;
	}
 
	public void setBonoMensual(float bonoMensual) {
		this.bonoMensual = bonoMensual;
	}

	//Decir que es administrador, para que pueda acceder a la interfaz de administrador
	@Override
	public void setEsAdmin(boolean esAdmin) {
    this.esAdmin = esAdmin;
}
 
}