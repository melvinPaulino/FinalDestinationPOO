package logico;

import java.util.Date;

public class Factura {

	public static final float TASA_ITBIS = 0.18f;

	private String codigo;
	private Date fecha;
	private Date fechaVencimiento;
	private Date periodoInicio;
	private Date periodoFin;
	private float subtotal;
	private float descuento;
	private float itbis;
	private float total;
	private Cliente cliente;

	public Factura(String codigo, Cliente cliente, Date fecha, Date fechaVencimiento, Date periodoInicio,
			Date periodoFin) {
		super();
		this.codigo = codigo;
		this.cliente = cliente;
		this.fecha = fecha;
		this.fechaVencimiento = fechaVencimiento;
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		this.subtotal = 0;
		this.descuento = 0;
		this.itbis = 0;
		this.total = 0;
	}

	public String getCodigo() {
		return codigo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFin() {
		return periodoFin;
	}

	public void setPeriodoFin(Date periodoFin) {
		this.periodoFin = periodoFin;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getDescuento() {
		return descuento;
	}

	public void setDescuento(float descuento) {
		this.descuento = descuento;
	}

	public float getItbis() {
		return itbis;
	}

	public void setItbis(float itbis) {
		this.itbis = itbis;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}