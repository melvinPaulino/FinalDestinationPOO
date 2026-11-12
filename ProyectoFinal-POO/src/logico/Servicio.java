package logico;

public class Servicio {
	
	private String codigo;
	private String nombre;
	private String descripcion;
	private float precio;
	private boolean estado;
	
	public Servicio(String codigo, String nombre, String descripcion, float precio, boolean estado) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.estado = estado;
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

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
	public void activarServicio() {
		
		
	}
	
	public void desactivarServicio() {
		
		
	}
	
	
	public float consultarPrecio() {
		
		
		return precio;
	}
	
	
	
	
}
