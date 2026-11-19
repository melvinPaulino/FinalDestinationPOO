package logico;

import java.util.Date;
import java.util.ArrayList;

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
	private boolean pagada;
	private Date fechaPago;
	private ArrayList<Contrato> contratos;

	public Factura(String codigo, Date fecha, Date fechaVencimiento, Date periodoInicio, Date periodoFin,
			float subtotal, float descuento, float itbis, float total, Cliente cliente, boolean pagada, Date fechaPago,
			ArrayList<Contrato> contratos) {
		super();
		this.codigo = codigo;
		this.fecha = fecha;
		this.fechaVencimiento = fechaVencimiento;
		this.periodoInicio = periodoInicio;
		this.periodoFin = periodoFin;
		this.subtotal = subtotal;
		this.descuento = descuento;
		this.itbis = itbis;
		this.total = total;
		this.cliente = cliente;
		this.pagada = pagada;
		this.fechaPago = fechaPago;
		this.contratos = contratos;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public ArrayList<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(ArrayList<Contrato> constratos) {
		this.contratos = constratos;
	}

	public static float getTasaItbis() {
		return TASA_ITBIS;
	}

	public void setPagada(boolean pagada) {
		this.pagada = pagada;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	} 

	public void registrarPago() {
    if (!pagada) {
        this.pagada = true;
        this.fechaPago = new Date();
    }
}

public void calcularTotal() {
    subtotal = 0;

    for (Contrato contrato : contratos) {
        subtotal += contrato.calcularCargoMensual();
    }

    float baseImponible = subtotal - descuento;

    itbis = baseImponible * TASA_ITBIS;
    total = baseImponible + itbis;
}

public boolean isPagada() {
    return pagada;
}
}