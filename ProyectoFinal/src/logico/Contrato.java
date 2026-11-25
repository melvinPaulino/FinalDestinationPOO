package logico;

import java.io.Serializable;
import java.util.Date;

public class Contrato implements Serializable {

	private static final long serialVersionUID = 1L;

	private String idContrato;
	private Cliente cliente;
	private Plan plan;
	private Comercial comercialAsignado;
	private Date fechaInicial;
	private String estadoContrato;
	private float cargoFijoMensual;
	private int diaCorte;
	private Date fechaSuspension;

	public Contrato(String idContrato, Cliente cliente, Plan plan, Comercial comercialAsignado, Date fechaInicial,
			String estadoContrato, float cargoFijoMensual, int diaCorte, Date fechaSuspension) {
		super();
		this.idContrato = idContrato;
		this.cliente = cliente;
		this.plan = plan;
		this.comercialAsignado = comercialAsignado;
		this.fechaInicial = fechaInicial;
		this.estadoContrato = estadoContrato;
		this.cargoFijoMensual = cargoFijoMensual;
		this.diaCorte = diaCorte;
		this.fechaSuspension = fechaSuspension;
	}

	public String getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(String idContrato) {
		this.idContrato = idContrato;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Comercial getComercialAsignado() {
		return comercialAsignado;
	}

	public void setComercialAsignado(Comercial comercialAsignado) {
		this.comercialAsignado = comercialAsignado;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getEstadoContrato() {
		return estadoContrato;
	}

	public void setEstadoContrato(String estadoContrato) {
		this.estadoContrato = estadoContrato;
	}

	public float getCargoFijoMensual() {
		return cargoFijoMensual;
	}

	public void setCargoFijoMensual(float cargoFijoMensual) {
		this.cargoFijoMensual = cargoFijoMensual;
	}

	public int getDiaCorte() {
		return diaCorte;
	}

	public void setDiaCorte(int diaCorte) {
		this.diaCorte = diaCorte;
	}

	public Date getFechaSuspension() {
		return fechaSuspension;
	}

	public float calcularCargoMensual() {
		return cargoFijoMensual;
	}

	public void setFechaSuspension(Date fechaSuspension) {
		this.fechaSuspension = fechaSuspension;
	}

	public synchronized boolean estaActivo() {
		return estadoContrato.equalsIgnoreCase("ACTIVO");
	}

	public synchronized void suspenderContrato() {
		this.estadoContrato = "SUSPENDIDO";
		this.fechaSuspension = new Date();
	}

	public synchronized boolean estaSuspendido() {
		return estadoContrato.equalsIgnoreCase(EstadoContrato.SUSPENDIDO.name());
	}

	public synchronized boolean estaCancelado() {
		return estadoContrato.equalsIgnoreCase(EstadoContrato.CANCELADO.name());
	}

	public synchronized void reactivarContrato() {
		this.estadoContrato = EstadoContrato.ACTIVO.name();
		this.fechaSuspension = null;
	}

	public synchronized void cancelarContrato() {
		this.estadoContrato = EstadoContrato.CANCELADO.name();
	}

	// Esto es para controlar el estado mediante valores definidos
	public enum EstadoContrato {
		ACTIVO,
		SUSPENDIDO,
		CANCELADO
	}

}
