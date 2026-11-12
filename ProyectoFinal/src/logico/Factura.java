package logico;

import java.time.LocalDate;

public class Factura {

	private String numero;
	private float monto;
	private LocalDate fecha;
	
		public Factura(String numero, float monto, LocalDate fecha) {
			super();
			this.numero = numero;
			this.monto = monto;
			this.fecha = fecha;
		}
}
