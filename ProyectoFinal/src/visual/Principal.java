package visual;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class Principal extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
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
	public Principal() {
		setForeground(Color.WHITE);
		setFont(null);
		setBackground(new Color(105, 105, 105));
		setTitle("Altice");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 990, 433);

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

		contentPane = new JPanel();
		contentPane.setBackground(new Color(112, 128, 144));
		contentPane.setBorder(new MatteBorder(5, 5, 5, 5, new Color(245, 245, 220)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
}
