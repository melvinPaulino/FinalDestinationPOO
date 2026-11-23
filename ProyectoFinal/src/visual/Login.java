package visual;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import logico.Altice;
import logico.Comercial;
import logico.Empleado;
import logico.Persona;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Color AZUL = new Color(0, 96, 254);
	private static final Color AZUL_OSCURO = new Color(0, 72, 200);
	private static final Color TEXTO = new Color(33, 43, 54);
	private static final Color GRIS = new Color(100, 110, 125);
	private static final Color BORDE = new Color(205, 212, 222);
	private static final Color ROJO = new Color(200, 40, 40);

	private static final int MINIMO = 5;
	private static final int MAXIMO = 30;

	private PanelFondo contentPane;
	private PestanaBoton btnAdmin;
	private PestanaBoton btnComercial;
	private JTextArea txtInfo;
	private JLabel lblUsuario;
	private JTextArea txtMensaje;
	private JTextField txtUsuario;
	private JPasswordField txtClave;
	private JCheckBox chkMostrar;
	private BotonRedondeado btnIniciar;
	private char echoOriginal;
	private Rol rolSeleccionado = Rol.ADMIN;

	// Pestana elegida en el login (solo controla lo que se muestra, no es un dato del sistema)
	private enum Rol {
		ADMIN,
		COMERCIAL
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Se cargan los datos guardados (si es la primera vez, no hay archivo y no pasa nada)
					Altice.cargarDatos();
				} catch (IOException | ClassNotFoundException e) {
					JOptionPane.showMessageDialog(null,
							"No se pudo leer el archivo de datos.\n"
									+ "Se guardo una copia en datos/altice.dat.bak y el sistema iniciara sin datos.\n\n"
									+ "Detalle: " + e.getMessage(),
							"Altice", JOptionPane.WARNING_MESSAGE);
				}
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Altice - Iniciar sesión");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// El area interna mide exactamente 740x740, igual que la imagen de fondo
		contentPane = new PanelFondo("/img/fondo_login.png");
		contentPane.setPreferredSize(new Dimension(740, 740));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// ---- Las dos divisiones: Admin y Comercial ----
		btnAdmin = new PestanaBoton("Admin");
		btnAdmin.setBounds(157, 250, 213, 44);
		btnAdmin.addActionListener(e -> cambiarRol(Rol.ADMIN));
		contentPane.add(btnAdmin);

		btnComercial = new PestanaBoton("Comercial");
		btnComercial.setBounds(370, 250, 213, 44);
		btnComercial.addActionListener(e -> cambiarRol(Rol.COMERCIAL));
		contentPane.add(btnComercial);

		txtInfo = crearTextoAjustado(new Font("Segoe UI", Font.PLAIN, 13));
		txtInfo.setBounds(157, 304, 426, 40);
		contentPane.add(txtInfo);

		// ---- Usuario / cedula ----
		lblUsuario = new JLabel("Usuario o cédula");
		lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblUsuario.setForeground(TEXTO);
		lblUsuario.setBounds(157, 352, 426, 18);
		contentPane.add(lblUsuario);

		txtUsuario = new JTextField();
		txtUsuario.setDocument(new LimitadorDocumento(MAXIMO));
		estilizarCampo(txtUsuario);
		txtUsuario.setBounds(157, 372, 426, 44);
		contentPane.add(txtUsuario);

		// ---- Contrasena ----
		JLabel lblClave = new JLabel("Contraseña");
		lblClave.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblClave.setForeground(TEXTO);
		lblClave.setBounds(157, 428, 426, 18);
		contentPane.add(lblClave);

		txtClave = new JPasswordField();
		txtClave.setDocument(new LimitadorDocumento(MAXIMO));
		estilizarCampo(txtClave);
		txtClave.setBounds(157, 448, 426, 44);
		contentPane.add(txtClave);
		echoOriginal = txtClave.getEchoChar();

		chkMostrar = new JCheckBox("Mostrar contraseña");
		chkMostrar.setOpaque(false);
		chkMostrar.setFocusPainted(false);
		chkMostrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		chkMostrar.setForeground(GRIS);
		chkMostrar.setBounds(153, 498, 200, 22);
		chkMostrar.addActionListener(e -> txtClave.setEchoChar(chkMostrar.isSelected() ? (char) 0 : echoOriginal));
		contentPane.add(chkMostrar);

		txtMensaje = crearTextoAjustado(new Font("Segoe UI", Font.BOLD, 12));
		txtMensaje.setForeground(ROJO);
		txtMensaje.setBounds(157, 528, 426, 36);
		contentPane.add(txtMensaje);

		// ---- Boton de inicio de sesion ----
		btnIniciar = new BotonRedondeado("Iniciar sesión", AZUL, Color.WHITE);
		btnIniciar.setBounds(157, 576, 426, 52);
		btnIniciar.addActionListener(e -> iniciarSesion());
		contentPane.add(btnIniciar);
		getRootPane().setDefaultButton(btnIniciar);

		actualizarModo();
		pack();
		setLocationRelativeTo(null);
	}

	private void cambiarRol(Rol rol) {
		rolSeleccionado = rol;
		actualizarModo();
	}

	// Ajusta los textos segun la pestana elegida y segun si ya existe un administrador
	private void actualizarModo() {
		boolean esAdmin = rolSeleccionado == Rol.ADMIN;
		boolean primerInicio = !Altice.getInstancia().existeAdministrador();

		btnAdmin.setSeleccionada(esAdmin);
		btnComercial.setSeleccionada(!esAdmin);

		if (esAdmin && primerInicio) {
			txtInfo.setForeground(AZUL);
			txtInfo.setText("Primer inicio de sesión: los datos que ingreses crearán la cuenta de administrador.");
			lblUsuario.setText("Usuario");
		} else if (!esAdmin && primerInicio) {
			txtInfo.setForeground(GRIS);
			txtInfo.setText("Todavía no hay cuentas creadas. El primer inicio de sesión se hace en la pestaña Admin.");
			lblUsuario.setText("Usuario o cédula");
		} else {
			txtInfo.setForeground(GRIS);
			txtInfo.setText("Ingresa con tu usuario o tu cédula y tu contraseña.");
			lblUsuario.setText("Usuario o cédula");
		}
		txtMensaje.setText("");
	}

	private void iniciarSesion() {
		String credencial = txtUsuario.getText().trim();
		String clave = new String(txtClave.getPassword());

		if (credencial.length() < MINIMO) {
			mostrarError("El usuario debe tener al menos " + MINIMO + " caracteres.");
			return;
		}
		if (clave.length() < MINIMO || clave.length() > MAXIMO) {
			mostrarError("La contraseña debe tener entre " + MINIMO + " y " + MAXIMO + " caracteres.");
			return;
		}

		Altice altice = Altice.getInstancia();
		Persona sesion;

		if (rolSeleccionado == Rol.ADMIN && !altice.existeAdministrador()) {
			// Primer inicio de sesion: estos datos se registran como la cuenta del primer administrador
			sesion = altice.registrarAdministradorInicial(credencial, clave);
			if (sesion == null) {
				mostrarError("No se pudo crear la cuenta de administrador.");
				return;
			}
			try {
				altice.guardarDatos();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,
						"La cuenta se creó, pero no se pudo guardar en el archivo:\n" + e.getMessage(), "Altice",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			sesion = autenticarSegunPestana(credencial, clave);
			if (sesion == null) {
				mostrarError("Usuario, cédula o contraseña incorrectos.");
				txtClave.setText("");
				return;
			}
		}

		dispose();
		new Principal(sesion).setVisible(true);
	}

	// Verifica las credenciales y que la cuenta tenga acceso a la pestana elegida:
	// Admin = empleado con permisos de administrador, Comercial = empleado de tipo Comercial
	private Persona autenticarSegunPestana(String credencial, String clave) {
		Persona persona = Altice.getInstancia().autenticar(credencial, clave);
		if (persona instanceof Empleado) {
			Empleado empleado = (Empleado) persona;
			if (rolSeleccionado == Rol.ADMIN && empleado.isEsAdmin()) {
				return empleado;
			}
			if (rolSeleccionado == Rol.COMERCIAL && empleado instanceof Comercial) {
				return empleado;
			}
		}
		return null;
	}

	// Texto de solo lectura que se ajusta al ancho disponible (varias lineas) y deja ver el fondo
	private JTextArea crearTextoAjustado(Font fuente) {
		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setFocusable(false);
		area.setOpaque(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setBorder(null);
		area.setFont(fuente);
		area.setForeground(GRIS);
		return area;
	}

	private void mostrarError(String mensaje) {
		txtMensaje.setText(mensaje);
	}

	private void estilizarCampo(JTextField campo) {
		campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		campo.setForeground(TEXTO);
		campo.setCaretColor(AZUL);
		bordeCampo(campo, BORDE);
		campo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				bordeCampo(campo, AZUL);
			}

			@Override
			public void focusLost(FocusEvent e) {
				bordeCampo(campo, BORDE);
			}
		});
	}

	private void bordeCampo(JTextField campo, Color color) {
		campo.setBorder(BorderFactory.createCompoundBorder(new LineBorder(color, 2, true), new EmptyBorder(0, 12, 0, 12)));
	}

	// Pestana plana: texto con una linea inferior azul cuando esta seleccionada
	private static class PestanaBoton extends JButton {

		private static final long serialVersionUID = 1L;

		private boolean seleccionada;

		PestanaBoton(String texto) {
			super(texto);
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusPainted(false);
			setOpaque(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			setSeleccionada(false);
		}

		void setSeleccionada(boolean seleccionada) {
			this.seleccionada = seleccionada;
			setForeground(seleccionada ? AZUL : GRIS);
			setFont(new Font("Segoe UI", seleccionada ? Font.BOLD : Font.PLAIN, 17));
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (seleccionada) {
				g.setColor(AZUL);
				g.fillRect(0, getHeight() - 3, getWidth(), 3);
			} else {
				g.setColor(BORDE);
				g.fillRect(0, getHeight() - 1, getWidth(), 1);
			}
		}
	}

	// Limita la cantidad de caracteres de un campo (acepta cualquier caracter)
	private static class LimitadorDocumento extends PlainDocument {

		private static final long serialVersionUID = 1L;

		private final int maximo;

		LimitadorDocumento(int maximo) {
			super();
			this.maximo = maximo;
		}

		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (str == null) {
				return;
			}
			int permitido = maximo - getLength();
			if (permitido <= 0) {
				return;
			}
			if (str.length() > permitido) {
				str = str.substring(0, permitido);
			}
			super.insertString(offs, str, a);
		}
	}

}
