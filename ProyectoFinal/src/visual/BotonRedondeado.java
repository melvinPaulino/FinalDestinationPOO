package visual;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

// Boton plano con las esquinas redondeadas, que cambia de color al pasar el mouse y al presionarlo.
public class BotonRedondeado extends JButton {

	private static final long serialVersionUID = 1L;

	private Color colorNormal;
	private Color colorHover;
	private Color colorPresionado;

	public BotonRedondeado(String texto, Color fondo, Color colorTexto) {
		super(texto);
		this.colorNormal = fondo;
		this.colorHover = oscurecer(fondo, 0.92f);
		this.colorPresionado = oscurecer(fondo, 0.82f);
		setForeground(colorTexto);
		setFont(new Font("Segoe UI", Font.BOLD, 16));
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setOpaque(false);
		setRolloverEnabled(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	private static Color oscurecer(Color c, float factor) {
		return new Color((int) (c.getRed() * factor), (int) (c.getGreen() * factor), (int) (c.getBlue() * factor));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (getModel().isPressed()) {
			g2.setColor(colorPresionado);
		} else if (getModel().isRollover()) {
			g2.setColor(colorHover);
		} else {
			g2.setColor(colorNormal);
		}
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
		g2.dispose();
		super.paintComponent(g);
	}

}
