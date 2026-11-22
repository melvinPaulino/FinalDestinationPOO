package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import logico.Altice;
import logico.Usuario;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private Usuario sesion;

	public static void main(String[] args) {
		Login.main(args);
	}
	
	public Principal() {
		this(null);
	}

	/**
	 * Create the frame.
	 */
	public Principal(Usuario sesion) {
		this.sesion = sesion;
		setForeground(Color.WHITE);
		setFont(null);
		setBackground(new Color(105, 105, 105));
		setTitle(sesion == null ? "Altice" : "Altice - " + sesion.getUsuario() + " (" + nombreRol() + ")");

		// El boton de cerrar de la ventana (la X) guarda los datos antes de salir
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		
		setSize(1920, 1080);
		setLocationRelativeTo(null);
		Point p = getLocation();
		setLocation(Math.max(p.x, 0), Math.max(p.y, 0));

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(105, 105, 105));
		setJMenuBar(menuBar);
		
				JMenu mnNewMenu = new JMenu("Clientes");
				mnNewMenu.setBackground(new Color(128, 0, 128));
				mnNewMenu.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
				menuBar.add(mnNewMenu);
				
						JMenuItem mntmNewMenuItem = new JMenuItem("Registrar un Cliente");
						mntmNewMenuItem.setBackground(new Color(46, 139, 87));
						mnNewMenu.add(mntmNewMenuItem);
						
								JMenuItem mntmNewMenuItem_1 = new JMenuItem("Consultar un Cliente");
								mntmNewMenuItem_1.setBackground(new Color(139, 0, 139));
								mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenu EmpleadosMenu_1_1 = new JMenu("Empleados");
		EmpleadosMenu_1_1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		EmpleadosMenu_1_1.setBackground(new Color(139, 0, 139));
		menuBar.add(EmpleadosMenu_1_1);
		
		JMenuItem mntmRegistrarUnEmpleado = new JMenuItem("Registrar un Empleado");
		mntmRegistrarUnEmpleado.setBackground(new Color(46, 139, 87));
		EmpleadosMenu_1_1.add(mntmRegistrarUnEmpleado);
		
		JMenuItem mntmConsultarEmpleados = new JMenuItem("Consultar empleados");
		mntmConsultarEmpleados.setBackground(new Color(139, 0, 139));
		EmpleadosMenu_1_1.add(mntmConsultarEmpleados);
		
		JMenu trabajadorMenu = new JMenu("Trabajadores");
		trabajadorMenu.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		trabajadorMenu.setBackground(new Color(139, 0, 139));
		menuBar.add(trabajadorMenu);
		
		JMenuItem mntmRegistrarUnTrabajador = new JMenuItem("Registrar un Trabajador");
		mntmRegistrarUnTrabajador.setBackground(new Color(46, 139, 87));
		trabajadorMenu.add(mntmRegistrarUnTrabajador);
		
		JMenuItem mntmConsultarUnTrabajador = new JMenuItem("Consultar un Trabajador");
		mntmConsultarUnTrabajador.setBackground(new Color(139, 0, 139));
		trabajadorMenu.add(mntmConsultarUnTrabajador);
		
		JMenu comercialMenu_1 = new JMenu("Comerciales");
		comercialMenu_1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		comercialMenu_1.setBackground(new Color(139, 0, 139));
		menuBar.add(comercialMenu_1);
		
		JMenuItem mntmRegistrarUnComercial = new JMenuItem("Registrar un comercial");
		mntmRegistrarUnComercial.setBackground(new Color(46, 139, 87));
		comercialMenu_1.add(mntmRegistrarUnComercial);
		
		JMenuItem mntmConsultarUnComercial = new JMenuItem("Consultar comerciales");
		mntmConsultarUnComercial.setBackground(new Color(139, 0, 139));
		comercialMenu_1.add(mntmConsultarUnComercial);

		JMenu AdministradoresMenu = new JMenu("Administradores");
		AdministradoresMenu.setBackground(new Color(139, 0, 139));
		AdministradoresMenu.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		menuBar.add(AdministradoresMenu);

		JMenuItem registerAdmin = new JMenuItem("Registrar un administrador");
		registerAdmin.setBackground(new Color(46, 139, 87));
		AdministradoresMenu.add(registerAdmin);

		JMenuItem consultarAdmin = new JMenuItem("Consultar un administrador");
		consultarAdmin.setBackground(new Color(139, 0, 139));
		AdministradoresMenu.add(consultarAdmin);
		
		JMenu planesMenu = new JMenu("Planes");
		planesMenu.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		planesMenu.setBackground(new Color(139, 0, 139));
		menuBar.add(planesMenu);
		
		JMenuItem mntmRegistrarUnPlan = new JMenuItem("Registrar un plan");
		mntmRegistrarUnPlan.setBackground(new Color(46, 139, 87));
		planesMenu.add(mntmRegistrarUnPlan);
		
		JMenuItem mntmConsultarPlanes = new JMenuItem("Consultar planes");
		mntmConsultarPlanes.setBackground(new Color(139, 0, 139));
		planesMenu.add(mntmConsultarPlanes);
		
		JMenu serviciosMenu = new JMenu("Servicios");
		serviciosMenu.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		serviciosMenu.setBackground(new Color(139, 0, 139));
		menuBar.add(serviciosMenu);
		
		JMenuItem mntmRegistrarUnServicio = new JMenuItem("Registrar un servicio");
		mntmRegistrarUnServicio.setBackground(new Color(46, 139, 87));
		serviciosMenu.add(mntmRegistrarUnServicio);
		
		JMenuItem mntmConsultarServicios = new JMenuItem("Consultar servicios");
		mntmConsultarServicios.setBackground(new Color(139, 0, 139));
		serviciosMenu.add(mntmConsultarServicios);

		JMenu mnNewMenu_1 = new JMenu("Facturas");
		mnNewMenu_1.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		mnNewMenu_1.setBackground(new Color(139, 0, 139));
		menuBar.add(mnNewMenu_1);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Facturar Contrato");
		mntmNewMenuItem_2.setBackground(new Color(46, 139, 87));
		mnNewMenu_1.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Consultar una Factura");
		mntmNewMenuItem_3.setBackground(new Color(139, 0, 139));
		mnNewMenu_1.add(mntmNewMenuItem_3);

		JMenu usuariosMenu = new JMenu("Usuarios");
		usuariosMenu.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		usuariosMenu.setBackground(new Color(139, 0, 139));
		menuBar.add(usuariosMenu);

		JMenuItem mntmRegistrarUnUsuario = new JMenuItem("Registrar un usuario");
		mntmRegistrarUnUsuario.setBackground(new Color(46, 139, 87));
		usuariosMenu.add(mntmRegistrarUnUsuario);

		JMenuItem mntmConsultarUsuarios = new JMenuItem("Consultar usuarios");
		mntmConsultarUsuarios.setBackground(new Color(139, 0, 139));
		usuariosMenu.add(mntmConsultarUsuarios);

		JMenu contratosMenu = new JMenu("Contratos");
		contratosMenu.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		contratosMenu.setBackground(new Color(139, 0, 139));
		menuBar.add(contratosMenu);

		JMenuItem mntmRegistrarUnContrato = new JMenuItem("Registrar un contrato");
		mntmRegistrarUnContrato.setBackground(new Color(46, 139, 87));
		contratosMenu.add(mntmRegistrarUnContrato);

		JMenuItem mntmConsultarContratos = new JMenuItem("Consultar contratos");
		mntmConsultarContratos.setBackground(new Color(139, 0, 139));
		contratosMenu.add(mntmConsultarContratos);

		JMenu consultasMenu = new JMenu("Consultas");
		consultasMenu.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		consultasMenu.setBackground(new Color(139, 0, 139));
		menuBar.add(consultasMenu);

		JMenuItem mntmFacturasPendientes = new JMenuItem("Facturas pendientes");
		mntmFacturasPendientes.setBackground(new Color(139, 0, 139));
		consultasMenu.add(mntmFacturasPendientes);

		JMenuItem mntmContratosPorEstado = new JMenuItem("Contratos por estado");
		mntmContratosPorEstado.setBackground(new Color(139, 0, 139));
		consultasMenu.add(mntmContratosPorEstado);

		JMenu reportesMenu = new JMenu("Reportes");
		reportesMenu.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		reportesMenu.setBackground(new Color(139, 0, 139));
		menuBar.add(reportesMenu);

		JMenuItem mntmReportePorPeriodo = new JMenuItem("Reporte por período");
		mntmReportePorPeriodo.setBackground(new Color(139, 0, 139));
		reportesMenu.add(mntmReportePorPeriodo);

		JMenuItem mntmReportePorLocal = new JMenuItem("Reporte por local");
		mntmReportePorLocal.setBackground(new Color(139, 0, 139));
		reportesMenu.add(mntmReportePorLocal);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(112, 128, 144));
		contentPane.setBorder(new MatteBorder(5, 5, 5, 5, new Color(245, 245, 220)));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Imagen de fondo (plantilla de 1920x1080)
		PanelFondo fondo = new PanelFondo("/img/fondo_main.png");
		fondo.setLayout(null);
		contentPane.add(fondo, BorderLayout.CENTER);

		// Barra inferior: datos de la sesion y boton de cerrar sesion
		JPanel barraInferior = new JPanel();
		barraInferior.setBackground(new Color(0, 96, 254));
		barraInferior.setBorder(new EmptyBorder(10, 24, 10, 24));
		barraInferior.setLayout(new BorderLayout(0, 0));
		contentPane.add(barraInferior, BorderLayout.SOUTH);

		JLabel lblSesion = new JLabel(
				sesion == null ? "Sin sesión" : "Sesión iniciada: " + sesion.getUsuario() + "  |  " + nombreRol());
		lblSesion.setForeground(Color.WHITE);
		lblSesion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		barraInferior.add(lblSesion, BorderLayout.WEST);

		BotonRedondeado btnCerrarSesion = new BotonRedondeado("Cerrar sesión", Color.WHITE, new Color(0, 96, 254));
		btnCerrarSesion.setPreferredSize(new Dimension(170, 42));
		btnCerrarSesion.addActionListener(e -> cerrarSesion());
		barraInferior.add(btnCerrarSesion, BorderLayout.EAST);
	}

	private String nombreRol() {
		if (sesion == null) {
			return "";
		}
		return sesion.getRol() == Usuario.Rol.ADMIN ? "Administrador" : "Comercial";
	}

	// Guarda los datos en el fichero. Si falla, pregunta si se quiere continuar de todos modos
	private boolean guardarDatos() {
		try {
			Altice.getInstancia().guardarDatos();
			return true;
		} catch (IOException e) {
			int opcion = JOptionPane.showConfirmDialog(this,
					"No se pudieron guardar los datos:\n" + e.getMessage() + "\n\n¿Continuar de todos modos?",
					"Error al guardar", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			return opcion == JOptionPane.YES_OPTION;
		}
	}

	// Boton de cerrar de la ventana: guarda y cierra el programa
	private void salir() {
		if (guardarDatos()) {
			dispose();
			System.exit(0);
		}
	}

	// Boton inferior: guarda, cierra esta ventana y vuelve al Login
	private void cerrarSesion() {
		if (guardarDatos()) {
			dispose();
			new Login().setVisible(true);
		}
	}
}
