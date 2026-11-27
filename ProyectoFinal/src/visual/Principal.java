package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import logico.Empleado;
import logico.Persona;
import hilos.HiloMorosidad;
import hilos.HilosFacturacion;
import hilos.HilosGuardar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import visual.PanelClientes;
import visual.PanelContratos;
import visual.PanelComercial;
import visual.PanelServicio;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private Persona sesion;
	private static HiloMorosidad hiloMorosidad;
	private JPanel panelCentral;

	public static void main(String[] args) {
		Login.main(args);
	}

	public Principal() {
		this(null);
	}

	/**
	 * Create the frame.
	 */
	public Principal(Persona sesion) {
		this.sesion = sesion;

		if (hiloMorosidad == null || !hiloMorosidad.isAlive()) { //Esto crea el hilo si todavía no existe o si ya terminó, y luego lo inicia.
			hiloMorosidad = new HiloMorosidad(24L * 60 * 60 * 1000);
			hiloMorosidad.start();
		}

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
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				PanelClientes panel = new PanelClientes();

				panelCentral.removeAll();
				panelCentral.add(panel, BorderLayout.CENTER);

				panelCentral.revalidate();
				panelCentral.repaint();
			}
		});
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

		mntmRegistrarUnComercial.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {

		        PanelComercial panel = new PanelComercial();

		        panelCentral.removeAll();
		        panelCentral.add(panel, BorderLayout.CENTER);

		        panelCentral.revalidate();
		        panelCentral.repaint();
		    }

		});

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

		mntmRegistrarUnPlan.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        PanelPlan panel = new PanelPlan();

		        panelCentral.removeAll();
		        panelCentral.add(panel, BorderLayout.CENTER);
		        panelCentral.revalidate();
		        panelCentral.repaint();

		    }
		});

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
		mntmRegistrarUnServicio.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        PanelServicio panel = new PanelServicio();
		        panelCentral.removeAll();
		        panelCentral.add(panel, BorderLayout.CENTER);
		        panelCentral.revalidate();
		        panelCentral.repaint();
		    }
		});
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
		mntmNewMenuItem_2.addActionListener(e -> {

			mntmNewMenuItem_2.setEnabled(false);

			boolean iniciado = HilosFacturacion.iniciar(
					new Date(),
					15,

					error -> SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog( // hace que el aviso final
																								// se ejecute en el hilo
																								// de la interfaz
																								// gráfica, que es donde
																								// Swing espera que se
																								// actualicen las
																								// ventanas y se
																								// muestren los
																								// mensajes.
							this,
							"Ocurrió un error al generar las facturas:\n"
									+ error.getMessage(),
							"Error de facturación",
							JOptionPane.ERROR_MESSAGE)),

					() -> SwingUtilities.invokeLater(() -> {
						mntmNewMenuItem_2.setEnabled(true);

						JOptionPane.showMessageDialog(
								this,
								"El proceso de facturación terminó.",
								"Facturación",
								JOptionPane.INFORMATION_MESSAGE);
					}));

			if (iniciado) {
				JOptionPane.showMessageDialog(
						this,
						"Se inició el proceso de facturación.",
						"Facturación",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				mntmNewMenuItem_2.setEnabled(true);

				JOptionPane.showMessageDialog(
						this,
						"Ya hay un proceso de facturación en ejecución.",
						"Facturación",
						JOptionPane.WARNING_MESSAGE);
			}
		});
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
		
		mntmRegistrarUnContrato.addActionListener(e -> {
		    PanelContratos panel = new PanelContratos();

		    panelCentral.removeAll();
		    panelCentral.add(panel, BorderLayout.CENTER);

		    panelCentral.revalidate();
		    panelCentral.repaint();
		});

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

		// Panel central donde se mostrarán las diferentes pantallas
		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout(0, 0));

		PanelFondo fondo = new PanelFondo("/img/fondo_main.png");
		fondo.setLayout(null);

		panelCentral.add(fondo, BorderLayout.CENTER);
		contentPane.add(panelCentral, BorderLayout.CENTER);

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
		boolean esAdmin = sesion instanceof Empleado && ((Empleado) sesion).isEsAdmin();
		return esAdmin ? "Administrador" : "Comercial";
	}

	// Intenta guardar los datos usando HilosGuardar, para que la interfaz no se
	// quede bloqueada mientras se escribe el archivo. Cuando termina el guardado,
	// decide si debe continuar con la acción que le pasaste, como cerrar el
	// programa o cerrar sesión.
	private void guardarDatos(Runnable alGuardar) {

		boolean iniciado = HilosGuardar.iniciar(hilo -> {

			if (hilo.isGuardadoExitoso()) {
				alGuardar.run();

			} else {
				int opcion = JOptionPane.showConfirmDialog(
						this,
						"No se pudieron guardar los datos:\n"
								+ hilo.getError().getMessage()
								+ "\n\n¿Continuar de todos modos?",
						"Error al guardar",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.ERROR_MESSAGE);

				if (opcion == JOptionPane.YES_OPTION) {
					alGuardar.run();
				}
			}
		});

		if (!iniciado) {
			JOptionPane.showMessageDialog(
					this,
					"Ya hay un proceso de guardado en ejecución.",
					"Guardado",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	// Boton de cerrar de la ventana: guarda y cierra el programa
	private void salir() {
		guardarDatos(() -> {
			dispose();
			System.exit(0);
			hiloMorosidad.detenerHilo(); // Detiene el hilo de morosidad antes de salir del programa
		});
	}

	private void cerrarSesion() {
		guardarDatos(() -> {
			dispose();
			new Login().setVisible(true);
		});
	}
	
	
}
