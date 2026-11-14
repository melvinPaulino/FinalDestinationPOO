package logico;
 
import java.util.ArrayList;
 
public class Plan {
 
	private String codigo;
	private String nombre;
	private String descripcion;
	private boolean activo;
	private float descuento;
	private ArrayList<Servicio> losServicios;
 
	public Plan(String codigo, String nombre, String descripcion, float descuento) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.descuento = descuento;
		this.activo = true;
		this.losServicios = new ArrayList<>();
	}
 
	public String getCodigo() {
		return codigo;
	}
 
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
 
	public String getNombre() {
		return nombre;
	}
 
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
 
	public String getDescripcion() {
		return descripcion;
	}
 
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
 
	public boolean isActivo() {
		return activo;
	}
 
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
 
	public float getDescuento() {
		return descuento;
	}
 
	public void setDescuento(float descuento) {
		this.descuento = descuento;
	}
 
	public ArrayList<Servicio> getLosServicios() {
		return losServicios;
	}
 
	public void setLosServicios(ArrayList<Servicio> losServicios) {
		this.losServicios = losServicios;
	}
 
}