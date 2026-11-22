package visual;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

// Panel que dibuja una imagen de fondo ajustada a su tamano.
// La imagen se busca dentro del proyecto (por ejemplo "/img/fondo_login.png").
public class PanelFondo extends JPanel {

	private static final long serialVersionUID = 1L;

	private transient Image imagen;

	public PanelFondo(String recurso) {
		super();
		URL url = PanelFondo.class.getResource(recurso);
		if (url != null) {
			imagen = new ImageIcon(url).getImage();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		if (imagen != null) {
			g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
		} else {
			// Si la imagen no se encuentra, se pinta un degradado azul para que la ventana no quede vacia
			g2.setPaint(new GradientPaint(0, 0, new Color(18, 102, 244), 0, getHeight(), new Color(20, 70, 200)));
			g2.fillRect(0, 0, getWidth(), getHeight());
		}
		g2.dispose();
	}

}
