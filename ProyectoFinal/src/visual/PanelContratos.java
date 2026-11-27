
package visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import logico.Altice;
import logico.Cliente;
import logico.Comercial;
import logico.Contrato;
import logico.Plan;

public class PanelContratos extends JPanel {

    private static final long serialVersionUID = 1L;

    // =============================================
    // COLORES CORPORATIVOS
    // =============================================

    private static final Color AZUL_OSCURO =
            new Color(13, 71, 161);

    private static final Color AZUL =
            new Color(25, 118, 210);

    private static final Color AZUL_CLARO =
            new Color(227, 242, 253);

    private static final Color FONDO =
            new Color(243, 247, 252);

    private static final Color BLANCO =
            Color.WHITE;

    private static final Color TEXTO =
            new Color(45, 55, 72);

    private static final Color BORDE =
            new Color(210, 222, 235);

    private static final Color GRIS =
            new Color(96, 125, 139);

    // =============================================
    // COMPONENTES
    // =============================================

    private JTextField txtDiaCorte;

    private JComboBox<Cliente> comboCliente;
    private JComboBox<Plan> comboPlan;
    private JComboBox<Comercial> comboComercial;

    private JTable tablaContratos;

    private Altice altice;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    public PanelContratos() {

        altice = Altice.getInstancia();

        setBorder(new EmptyBorder(0, 0, 0, 0));
        setLayout(null);
        setBackground(FONDO);

        setPreferredSize(
                new java.awt.Dimension(1050, 710)
        );

        setMinimumSize(
                new java.awt.Dimension(1050, 710)
        );

        // =========================================
        // ENCABEZADO
        // =========================================

        JPanel panelTitulo = new JPanel(null);
        panelTitulo.setBounds(25, 18, 1000, 75);
        panelTitulo.setBackground(BLANCO);
        panelTitulo.setBorder(
                new LineBorder(BORDE, 1, true)
        );
        add(panelTitulo);

        JLabel lblTitulo = new JLabel("Gestión de Contratos");
        lblTitulo.setBounds(25, 10, 650, 35);
        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 25)
        );
        lblTitulo.setForeground(AZUL_OSCURO);
        panelTitulo.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel(
                "Registre nuevos contratos y consulte los servicios contratados"
        );
        lblSubtitulo.setBounds(27, 45, 750, 20);
        lblSubtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );
        lblSubtitulo.setForeground(
                new Color(105, 117, 130)
        );
        panelTitulo.add(lblSubtitulo);

        // =========================================
        // TARJETA DE REGISTRO
        // =========================================

        JPanel panelRegistro = new JPanel(null);
        panelRegistro.setBounds(25, 110, 1000, 235);
        panelRegistro.setBackground(BLANCO);
        panelRegistro.setBorder(
                new LineBorder(BORDE, 1, true)
        );
        add(panelRegistro);

        JLabel lblRegistro = new JLabel(
                "Información para el nuevo contrato"
        );
        lblRegistro.setBounds(22, 12, 600, 28);
        lblRegistro.setFont(
                new Font("Segoe UI", Font.BOLD, 17)
        );
        lblRegistro.setForeground(AZUL_OSCURO);
        panelRegistro.add(lblRegistro);

        JLabel lblIndicacion = new JLabel(
                "Seleccione los datos correspondientes para formalizar el contrato"
        );
        lblIndicacion.setBounds(24, 39, 750, 20);
        lblIndicacion.setFont(
                new Font("Segoe UI", Font.PLAIN, 11)
        );
        lblIndicacion.setForeground(
                new Color(110, 120, 135)
        );
        panelRegistro.add(lblIndicacion);

        // -----------------------------------------
        // CLIENTE
        // -----------------------------------------

        agregarEtiqueta(
                panelRegistro,
                "Cliente:",
                25, 82, 110
        );

        comboCliente = crearCombo(
                panelRegistro,
                145, 78, 330, 36
        );

        comboCliente.setRenderer(
                new DefaultListCellRenderer() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                        if (value instanceof Cliente) {

                            Cliente cliente = (Cliente) value;

                            setText(
                                    cliente.getNombre() + " "
                                            + cliente.getApellido()
                            );
                        }

                        return this;
                    }
                }
        );

        // -----------------------------------------
        // PLAN
        // -----------------------------------------

        agregarEtiqueta(
                panelRegistro,
                "Plan:",
                520, 82, 110
        );

        comboPlan = crearCombo(
                panelRegistro,
                640, 78, 320, 36
        );

        comboPlan.setRenderer(
                new DefaultListCellRenderer() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                        if (value instanceof Plan) {

                            Plan plan = (Plan) value;

                            setText(plan.getNombre());
                        }

                        return this;
                    }
                }
        );

        // -----------------------------------------
        // COMERCIAL
        // -----------------------------------------

        agregarEtiqueta(
                panelRegistro,
                "Comercial:",
                25, 139, 110
        );

        comboComercial = crearCombo(
                panelRegistro,
                145, 135, 330, 36
        );

        comboComercial.setRenderer(
                new DefaultListCellRenderer() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                        if (value instanceof Comercial) {

                            Comercial comercial = (Comercial) value;

                            setText(
                                    comercial.getNombre() + " "
                                            + comercial.getApellido()
                            );
                        }

                        return this;
                    }
                }
        );

        // -----------------------------------------
        // DÍA DE CORTE
        // -----------------------------------------

        agregarEtiqueta(
                panelRegistro,
                "Día de corte:",
                520, 139, 110
        );

        txtDiaCorte = crearCampo(
                panelRegistro,
                640, 135, 150, 36
        );

        aplicarFiltroDiaCorte();

        JLabel lblAyudaCorte = new JLabel(
                "Del 1 al 28"
        );
        lblAyudaCorte.setBounds(800, 140, 130, 25);
        lblAyudaCorte.setFont(
                new Font("Segoe UI", Font.ITALIC, 11)
        );
        lblAyudaCorte.setForeground(
                new Color(115, 125, 140)
        );
        panelRegistro.add(lblAyudaCorte);

        // -----------------------------------------
        // BOTÓN ORIGINAL: CREAR CONTRATO
        // -----------------------------------------

        JButton btnCrear = new JButton("Crear contrato");
        btnCrear.setBounds(640, 188, 155, 35);
        configurarBoton(btnCrear, AZUL);
        panelRegistro.add(btnCrear);

        // -----------------------------------------
        // BOTÓN ORIGINAL: LIMPIAR
        // -----------------------------------------

        JButton btnLimpiar = new JButton("Limpiar campos");
        btnLimpiar.setBounds(810, 188, 150, 35);
        configurarBoton(btnLimpiar, GRIS);
        panelRegistro.add(btnLimpiar);

        // =========================================
        // TARJETA DE CONTRATOS REGISTRADOS
        // =========================================

        JPanel panelTabla = new JPanel(null);
        panelTabla.setBounds(25, 365, 1000, 315);
        panelTabla.setBackground(BLANCO);
        panelTabla.setBorder(
                new LineBorder(BORDE, 1, true)
        );
        add(panelTabla);

        JLabel lblContratos = new JLabel(
                "Contratos registrados"
        );
        lblContratos.setBounds(22, 12, 600, 28);
        lblContratos.setFont(
                new Font("Segoe UI", Font.BOLD, 17)
        );
        lblContratos.setForeground(AZUL_OSCURO);
        panelTabla.add(lblContratos);

        JLabel lblDetalle = new JLabel(
                "Listado general de contratos y sus condiciones"
        );
        lblDetalle.setBounds(24, 39, 650, 20);
        lblDetalle.setFont(
                new Font("Segoe UI", Font.PLAIN, 11)
        );
        lblDetalle.setForeground(
                new Color(110, 120, 135)
        );
        panelTabla.add(lblDetalle);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(18, 70, 964, 225);
        scrollPane.setBorder(
                new LineBorder(BORDE, 1)
        );
        panelTabla.add(scrollPane);

        tablaContratos = new JTable();
        configurarTabla(tablaContratos);

        scrollPane.setViewportView(tablaContratos);

        // =========================================
        // CARGA INICIAL
        // =========================================

        cargarCombos();
        cargarTabla();

        // =========================================
        // EVENTO ORIGINAL: CREAR
        // =========================================

        btnCrear.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                crearContrato();
            }
        });

        // =========================================
        // EVENTO ORIGINAL: LIMPIAR
        // =========================================

        btnLimpiar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
    }

    // =============================================
    // MÉTODOS DE PRESENTACIÓN
    // =============================================

    private void agregarEtiqueta(
            JPanel panel,
            String texto,
            int x,
            int y,
            int ancho) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setBounds(x, y, ancho, 27);

        etiqueta.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        etiqueta.setForeground(TEXTO);

        panel.add(etiqueta);
    }

    private JTextField crearCampo(
            JPanel panel,
            int x,
            int y,
            int ancho,
            int alto) {

        JTextField campo = new JTextField();

        campo.setBounds(x, y, ancho, alto);

        campo.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        campo.setForeground(TEXTO);
        campo.setBackground(BLANCO);

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDE, 1, true),
                        new EmptyBorder(5, 9, 5, 9)
                )
        );

        panel.add(campo);

        return campo;
    }

    private JComboBox crearCombo(
            JPanel panel,
            int x,
            int y,
            int ancho,
            int alto) {

        JComboBox combo = new JComboBox();

        combo.setBounds(x, y, ancho, alto);

        combo.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        combo.setForeground(TEXTO);
        combo.setBackground(BLANCO);

        combo.setBorder(
                new LineBorder(BORDE, 1, true)
        );

        panel.add(combo);

        return combo;
    }

    private void configurarBoton(
            JButton boton,
            Color color) {

        boton.setBackground(color);
        boton.setForeground(BLANCO);

        boton.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
    }

    private void configurarTabla(JTable tabla) {

        tabla.setFont(
                new Font("Segoe UI", Font.PLAIN, 11)
        );

        tabla.setForeground(TEXTO);
        tabla.setBackground(BLANCO);

        tabla.setRowHeight(29);

        tabla.setSelectionBackground(AZUL_CLARO);
        tabla.setSelectionForeground(AZUL_OSCURO);

        tabla.setGridColor(
                new Color(232, 238, 245)
        );

        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);

        tabla.setFillsViewportHeight(true);
        tabla.setAutoCreateRowSorter(true);

        JTableHeader encabezado = tabla.getTableHeader();

        encabezado.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        encabezado.setBackground(AZUL_OSCURO);
        encabezado.setForeground(BLANCO);

        encabezado.setPreferredSize(
                new java.awt.Dimension(0, 34)
        );

        tabla.setDefaultRenderer(
                Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column) {

                        Component componente =
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );

                        if (isSelected) {

                            componente.setBackground(AZUL_CLARO);
                            componente.setForeground(AZUL_OSCURO);

                        } else {

                            componente.setBackground(
                                    row % 2 == 0
                                            ? BLANCO
                                            : new Color(247, 250, 253)
                            );

                            componente.setForeground(TEXTO);
                        }

                        setBorder(
                                new EmptyBorder(0, 7, 0, 7)
                        );

                        return componente;
                    }
                }
        );
    }

    // =============================================
    // FILTRO DEL DÍA DE CORTE
    // =============================================

    private void aplicarFiltroDiaCorte() {

        AbstractDocument documento =
                (AbstractDocument) txtDiaCorte.getDocument();

        documento.setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(
                    FilterBypass fb,
                    int offset,
                    String texto,
                    AttributeSet attr)
                    throws BadLocationException {

                if (texto == null) return;

                String actual = fb.getDocument().getText(
                        0,
                        fb.getDocument().getLength()
                );

                String nuevo = new StringBuilder(actual)
                        .insert(offset, texto)
                        .toString();

                if (nuevo.matches("[0-9]{0,2}")) {
                    super.insertString(fb, offset, texto, attr);
                }
            }

            @Override
            public void replace(
                    FilterBypass fb,
                    int offset,
                    int length,
                    String texto,
                    AttributeSet attr)
                    throws BadLocationException {

                if (texto == null) texto = "";

                String actual = fb.getDocument().getText(
                        0,
                        fb.getDocument().getLength()
                );

                StringBuilder nuevo = new StringBuilder(actual);

                nuevo.replace(offset, offset + length, texto);

                if (nuevo.toString().matches("[0-9]{0,2}")) {
                    super.replace(fb, offset, length, texto, attr);
                }
            }
        });
    }

    // =============================================
    // CARGAR COMBOS
    // =============================================

    private void cargarCombos() {

        comboCliente.removeAllItems();
        comboPlan.removeAllItems();
        comboComercial.removeAllItems();

        // CLIENTES ACTIVOS

        ArrayList<Cliente> clientes = altice.obtenerClientes();

        for (Cliente cliente : clientes) {

            if (cliente.isActivo()) {
                comboCliente.addItem(cliente);
            }
        }

        // PLANES ACTIVOS CON SERVICIOS

        ArrayList<Plan> planes = altice.getLosPlanes();

        for (Plan plan : planes) {

            if (plan.isActivo()
                    && !plan.getLosServicios().isEmpty()) {

                comboPlan.addItem(plan);
            }
        }

        // COMERCIALES ACTIVOS

        ArrayList<Comercial> comerciales =
                altice.obtenerEmpleadosPorTipo(Comercial.class);

        for (Comercial comercial : comerciales) {

            if (comercial.isActivo()) {
                comboComercial.addItem(comercial);
            }
        }
    }

    // =============================================
    // CREAR CONTRATO
    // =============================================

    private void crearContrato() {

        if (comboCliente.getSelectedItem() == null
                || comboPlan.getSelectedItem() == null
                || comboComercial.getSelectedItem() == null
                || txtDiaCorte.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe completar todos los campos.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int diaCorte;

        try {

            diaCorte = Integer.parseInt(
                    txtDiaCorte.getText().trim()
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El día de corte debe ser un número.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (diaCorte < 1 || diaCorte > 28) {

            JOptionPane.showMessageDialog(
                    this,
                    "El día de corte debe estar entre 1 y 28.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // OBTENER LOS OBJETOS SELECCIONADOS

        Cliente clienteSeleccionado =
                (Cliente) comboCliente.getSelectedItem();

        Plan planSeleccionado =
                (Plan) comboPlan.getSelectedItem();

        Comercial comercialSeleccionado =
                (Comercial) comboComercial.getSelectedItem();

        // UTILIZAR IDENTIFICADORES INTERNOS

        String cedulaCliente =
                clienteSeleccionado.getCedula();

        String codigoPlan =
                planSeleccionado.getCodigo();

        String codigoComercial =
                comercialSeleccionado.getCodigo();

        Contrato contrato = altice.registrarContrato(
                cedulaCliente,
                codigoPlan,
                codigoComercial,
                diaCorte
        );

        if (contrato != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Contrato registrado correctamente.\n"
                            + "ID: " + contrato.getIdContrato(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el contrato.\n"
                            + "Verifique que los datos sean válidos "
                            + "y que el cliente no tenga ya un contrato con ese plan.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =============================================
    // CARGAR TABLA
    // =============================================

    private void cargarTabla() {

        DefaultTableModel modelo = new DefaultTableModel() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        modelo.addColumn("ID");
        modelo.addColumn("Cliente");
        modelo.addColumn("Plan");
        modelo.addColumn("Comercial");
        modelo.addColumn("Estado");
        modelo.addColumn("Cargo mensual");
        modelo.addColumn("Día corte");

        ArrayList<Contrato> contratos =
                altice.obtenerContratos();

        for (Contrato contrato : contratos) {

            modelo.addRow(new Object[] {

                    contrato.getIdContrato(),

                    contrato.getCliente().getNombre() + " "
                            + contrato.getCliente().getApellido(),

                    contrato.getPlan().getNombre(),

                    contrato.getComercialAsignado().getNombre() + " "
                            + contrato.getComercialAsignado().getApellido(),

                    contrato.getEstadoContrato(),

                    contrato.getCargoFijoMensual(),

                    contrato.getDiaCorte()

            });
        }

        tablaContratos.setModel(modelo);
        configurarTabla(tablaContratos);
    }

    // =============================================
    // LIMPIAR CAMPOS
    // =============================================

    private void limpiarCampos() {

        if (comboCliente.getItemCount() > 0) {
            comboCliente.setSelectedIndex(0);
        }

        if (comboPlan.getItemCount() > 0) {
            comboPlan.setSelectedIndex(0);
        }

        if (comboComercial.getItemCount() > 0) {
            comboComercial.setSelectedIndex(0);
        }

        txtDiaCorte.setText("");
        txtDiaCorte.requestFocus();
    }
}
