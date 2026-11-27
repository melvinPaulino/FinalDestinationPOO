
package visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import logico.Altice;
import logico.Plan;
import logico.Servicio;

public class PanelPlan extends JPanel {

    private static final long serialVersionUID = 1L;

    // COLORES CORPORATIVOS

    private static final Color AZUL_OSCURO = new Color(13, 71, 161);
    private static final Color AZUL = new Color(25, 118, 210);
    private static final Color AZUL_CLARO = new Color(227, 242, 253);
    private static final Color FONDO = new Color(243, 247, 252);
    private static final Color BLANCO = Color.WHITE;
    private static final Color TEXTO = new Color(45, 55, 72);
    private static final Color BORDE = new Color(210, 222, 235);

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtDescuento;

    private JComboBox<OpcionCombo> comboPlan;
    private JComboBox<OpcionCombo> comboServicio;

    private JTable tablaPlanes;
    private JTable tablaServiciosPlan;

    private Altice altice;

    private boolean cargandoComboPlan = false;

    public PanelPlan() {

        altice = Altice.getInstancia();

        setBorder(new EmptyBorder(0, 0, 0, 0));
        setLayout(null);
        setBackground(FONDO);
        setPreferredSize(new java.awt.Dimension(1050, 710));
        setMinimumSize(new java.awt.Dimension(1050, 710));

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

        JLabel lblTitulo = new JLabel("Gestión de Planes");
        lblTitulo.setBounds(25, 12, 600, 32);
        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 25)
        );
        lblTitulo.setForeground(AZUL_OSCURO);
        panelTitulo.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel(
                "Registre planes y administre los servicios asociados"
        );
        lblSubtitulo.setBounds(27, 45, 700, 20);
        lblSubtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );
        lblSubtitulo.setForeground(new Color(105, 117, 130));
        panelTitulo.add(lblSubtitulo);

        // =========================================
        // TARJETA DE REGISTRO
        // =========================================

        JPanel panelRegistro = new JPanel(null);
        panelRegistro.setBounds(25, 110, 1000, 205);
        panelRegistro.setBackground(BLANCO);
        panelRegistro.setBorder(
                new LineBorder(BORDE, 1, true)
        );
        add(panelRegistro);

        JLabel lblRegistro = new JLabel("Información del plan");
        lblRegistro.setBounds(22, 12, 400, 27);
        lblRegistro.setFont(
                new Font("Segoe UI", Font.BOLD, 17)
        );
        lblRegistro.setForeground(AZUL_OSCURO);
        panelRegistro.add(lblRegistro);

        // NOMBRE

        agregarEtiqueta(
                panelRegistro, "Nombre del plan:",
                25, 57, 140
        );

        txtNombre = crearCampo(
                panelRegistro, 170, 53, 450, 34
        );

        // DESCRIPCIÓN

        agregarEtiqueta(
                panelRegistro, "Descripción:",
                25, 111, 140
        );

        txtDescripcion = crearCampo(
                panelRegistro, 170, 107, 450, 34
        );

        // DESCUENTO

        agregarEtiqueta(
                panelRegistro, "Descuento (%):",
                25, 165, 140
        );

        txtDescuento = crearCampo(
                panelRegistro, 170, 161, 180, 34
        );

        // BOTÓN ORIGINAL: REGISTRAR

        JButton btnRegistrar = new JButton("Registrar plan");
        btnRegistrar.setBounds(720, 65, 220, 42);
        configurarBoton(btnRegistrar, AZUL);
        panelRegistro.add(btnRegistrar);

        // BOTÓN ORIGINAL: LIMPIAR

        JButton btnLimpiar = new JButton("Limpiar campos");
        btnLimpiar.setBounds(720, 120, 220, 42);
        configurarBoton(btnLimpiar, new Color(96, 125, 139));
        panelRegistro.add(btnLimpiar);

        // =========================================
        // TARJETA DE ASOCIACIÓN DE SERVICIOS
        // =========================================

        JPanel panelAsociacion = new JPanel(null);
        panelAsociacion.setBounds(25, 330, 1000, 115);
        panelAsociacion.setBackground(BLANCO);
        panelAsociacion.setBorder(
                new LineBorder(BORDE, 1, true)
        );
        add(panelAsociacion);

        JLabel lblAsociacion = new JLabel(
                "Administrar servicios del plan"
        );
        lblAsociacion.setBounds(20, 8, 450, 25);
        lblAsociacion.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );
        lblAsociacion.setForeground(AZUL_OSCURO);
        panelAsociacion.add(lblAsociacion);

        agregarEtiqueta(
                panelAsociacion, "Plan:",
                20, 53, 65
        );

        comboPlan = crearCombo(
                panelAsociacion, 85, 49, 235, 34
        );

        agregarEtiqueta(
                panelAsociacion, "Servicio:",
                345, 53, 80
        );

        comboServicio = crearCombo(
                panelAsociacion, 425, 49, 210, 34
        );

        // BOTÓN ORIGINAL: AGREGAR SERVICIO

        JButton btnAgregarServicio =
                new JButton("Agregar servicio");

        btnAgregarServicio.setBounds(650, 48, 155, 36);
        configurarBoton(btnAgregarServicio, AZUL);
        panelAsociacion.add(btnAgregarServicio);

        // BOTÓN ORIGINAL: QUITAR SERVICIO

        JButton btnQuitarServicio =
                new JButton("Quitar servicio");

        btnQuitarServicio.setBounds(815, 48, 155, 36);
        configurarBoton(
                btnQuitarServicio,
                new Color(198, 40, 40)
        );
        panelAsociacion.add(btnQuitarServicio);

        // BOTÓN ORIGINAL: ACTUALIZAR SERVICIOS

        JButton btnActualizarServicios =
                new JButton("Actualizar servicios disponibles");

        btnActualizarServicios.setBounds(650, 87, 320, 23);
        configurarBoton(
                btnActualizarServicios,
                new Color(96, 125, 139)
        );
        btnActualizarServicios.setFont(
                new Font("Segoe UI", Font.PLAIN, 11)
        );
        panelAsociacion.add(btnActualizarServicios);

        // =========================================
        // TARJETA DE TABLAS
        // =========================================

        JPanel panelTablas = new JPanel(null);
        panelTablas.setBounds(25, 460, 1000, 220);
        panelTablas.setBackground(BLANCO);
        panelTablas.setBorder(
                new LineBorder(BORDE, 1, true)
        );
        add(panelTablas);

        // TABLA DE PLANES

        JLabel lblPlanes = new JLabel("Planes registrados");
        lblPlanes.setBounds(20, 10, 430, 25);
        lblPlanes.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );
        lblPlanes.setForeground(AZUL_OSCURO);
        panelTablas.add(lblPlanes);

        JScrollPane scrollPlanes = new JScrollPane();
        scrollPlanes.setBounds(18, 42, 465, 160);
        scrollPlanes.setBorder(
                new LineBorder(BORDE, 1)
        );
        panelTablas.add(scrollPlanes);

        tablaPlanes = new JTable();
        configurarTabla(tablaPlanes);
        scrollPlanes.setViewportView(tablaPlanes);

        // TABLA DE SERVICIOS ASOCIADOS

        JLabel lblServiciosPlan = new JLabel(
                "Servicios del plan seleccionado"
        );
        lblServiciosPlan.setBounds(505, 10, 460, 25);
        lblServiciosPlan.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );
        lblServiciosPlan.setForeground(AZUL_OSCURO);
        panelTablas.add(lblServiciosPlan);

        JScrollPane scrollServicios = new JScrollPane();
        scrollServicios.setBounds(500, 42, 482, 160);
        scrollServicios.setBorder(
                new LineBorder(BORDE, 1)
        );
        panelTablas.add(scrollServicios);

        tablaServiciosPlan = new JTable();
        configurarTabla(tablaServiciosPlan);
        scrollServicios.setViewportView(tablaServiciosPlan);

        // =========================================
        // FILTROS DE ENTRADA
        // =========================================

        aplicarFiltro(txtNombre, "[^\\p{Cntrl}]*", 80);
        aplicarFiltro(txtDescripcion, "[^\\p{Cntrl}]*", 500);
        aplicarFiltro(txtDescuento, "\\d*(\\.\\d{0,2})?", 6);

        // =========================================
        // EVENTOS ORIGINALES
        // =========================================

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPlan();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        btnAgregarServicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarServicio();
            }
        });

        btnQuitarServicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitarServicio();
            }
        });

        btnActualizarServicios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarServiciosDisponibles();
            }
        });

        comboPlan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!cargandoComboPlan) {
                    cargarServiciosAsociados();
                }
            }
        });

        // =========================================
        // CARGA INICIAL
        // =========================================

        cargarTablaPlanes();
        cargarCombosPlanes();
        cargarServiciosDisponibles();
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
        etiqueta.setBounds(x, y, ancho, 25);
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

    private JComboBox<OpcionCombo> crearCombo(
            JPanel panel,
            int x,
            int y,
            int ancho,
            int alto) {

        JComboBox<OpcionCombo> combo =
                new JComboBox<OpcionCombo>();

        combo.setBounds(x, y, ancho, alto);
        combo.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );
        combo.setBackground(BLANCO);
        combo.setForeground(TEXTO);
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
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );
    }

    private void configurarTabla(JTable tabla) {

        tabla.setFont(
                new Font("Segoe UI", Font.PLAIN, 11)
        );
        tabla.setForeground(TEXTO);
        tabla.setBackground(BLANCO);
        tabla.setRowHeight(25);
        tabla.setSelectionBackground(AZUL_CLARO);
        tabla.setSelectionForeground(AZUL_OSCURO);
        tabla.setGridColor(new Color(232, 238, 245));
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setFillsViewportHeight(true);

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );
        encabezado.setBackground(AZUL_OSCURO);
        encabezado.setForeground(BLANCO);
        encabezado.setPreferredSize(
                new java.awt.Dimension(0, 30)
        );

        DefaultTableCellRenderer centrado =
                new DefaultTableCellRenderer();

        centrado.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        tabla.setDefaultRenderer(
                Object.class,
                new DefaultTableCellRenderer() {

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
                                new EmptyBorder(0, 6, 0, 6)
                        );

                        return componente;
                    }
                }
        );
    }

    // =============================================
    // REGISTRAR PLAN
    // =============================================

    private void registrarPlan() {

        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String textoDescuento = txtDescuento.getText().trim();

        if (nombre.isEmpty()
                || descripcion.isEmpty()
                || textoDescuento.isEmpty()) {

            mostrarError("Debe completar todos los campos.");
            return;
        }

        if (nombre.length() < 2 || nombre.length() > 80
                || !nombre.matches(".*\\p{L}.*")) {

            mostrarError(
                    "El nombre debe tener entre 2 y 80 caracteres e incluir letras."
            );
            return;
        }

        if (descripcion.length() < 5 || descripcion.length() > 500) {

            mostrarError(
                    "La descripción debe tener entre 5 y 500 caracteres."
            );
            return;
        }

        BigDecimal descuentoDecimal;

        try {

            descuentoDecimal = new BigDecimal(textoDescuento);

        } catch (NumberFormatException e) {

            mostrarError("El descuento debe ser un número válido.");
            return;
        }

        if (descuentoDecimal.compareTo(BigDecimal.ZERO) < 0
                || descuentoDecimal.compareTo(new BigDecimal("100")) > 0) {

            mostrarError("El descuento debe estar entre 0 y 100 %.");
            return;
        }

        float descuento = descuentoDecimal.floatValue();

        if (!Float.isFinite(descuento)) {

            mostrarError("El descuento introducido no es válido.");
            return;
        }

        String codigo = generarCodigoPlan();

        Plan plan = new Plan(
                codigo,
                nombre,
                descripcion,
                descuento
        );

        boolean registrado = altice.registrarPlan(plan);

        if (registrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Plan registrado correctamente.\n"
                            + "Código generado: " + codigo,
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();

            cargarTablaPlanes();
            cargarCombosPlanes();

            seleccionarPlan(codigo);

        } else {

            mostrarError(
                    "No se pudo registrar el plan. "
                            + "Compruebe los datos e inténtelo nuevamente."
            );
        }
    }

    // =============================================
    // GENERAR CÓDIGO DEL PLAN
    // =============================================

    private String generarCodigoPlan() {

        ArrayList<Plan> planes = altice.getLosPlanes();

        Set<String> codigos = new HashSet<>();

        int siguiente = 1;

        Pattern patron = Pattern.compile(
                "PLN-(\\d+)",
                Pattern.CASE_INSENSITIVE
        );

        for (Plan plan : planes) {

            if (plan.getCodigo() == null) {
                continue;
            }

            String codigoExistente =
                    plan.getCodigo().trim().toUpperCase();

            codigos.add(codigoExistente);

            Matcher matcher = patron.matcher(codigoExistente);

            if (matcher.matches()) {

                try {

                    int numero = Integer.parseInt(matcher.group(1));

                    if (numero >= siguiente && numero < Integer.MAX_VALUE) {
                        siguiente = numero + 1;
                    }

                } catch (NumberFormatException e) {
                    // Ignorar códigos antiguos no numéricos.
                }
            }
        }

        String codigo;

        do {

            codigo = String.format("PLN-%04d", siguiente);
            siguiente++;

        } while (codigos.contains(codigo));

        return codigo;
    }

    // =============================================
    // CARGAR TABLA DE PLANES
    // =============================================

    private void cargarTablaPlanes() {

        DefaultTableModel modelo = new DefaultTableModel() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripción");
        modelo.addColumn("Descuento");
        modelo.addColumn("Servicios");
        modelo.addColumn("Precio");
        modelo.addColumn("Estado");

        ArrayList<Plan> planes = altice.getLosPlanes();

        for (Plan plan : planes) {

            int cantidadServicios =
                    altice.obtenerServiciosPlan(plan.getCodigo()).size();

            modelo.addRow(new Object[] {
                    plan.getCodigo(),
                    plan.getNombre(),
                    plan.getDescripcion(),
                    plan.getDescuento(),
                    cantidadServicios,
                    plan.calcularPrecio(),
                    plan.isActivo() ? "Activo" : "Inactivo"
            });
        }

        tablaPlanes.setModel(modelo);
        configurarTabla(tablaPlanes);
    }

    // =============================================
    // CARGAR COMBO DE PLANES
    // =============================================

    private void cargarCombosPlanes() {

        String codigoSeleccionado = obtenerCodigoSeleccionado(comboPlan);

        cargandoComboPlan = true;

        comboPlan.removeAllItems();

        ArrayList<Plan> planes = altice.getLosPlanes();

        for (Plan plan : planes) {

            comboPlan.addItem(
                    new OpcionCombo(
                            plan.getCodigo(),
                            plan.getNombre()
                    )
            );
        }

        if (codigoSeleccionado != null) {
            seleccionarPlan(codigoSeleccionado);
        }

        cargandoComboPlan = false;

        cargarServiciosAsociados();
    }

    // =============================================
    // CARGAR SERVICIOS DISPONIBLES
    // =============================================

    private void cargarServiciosDisponibles() {

        comboServicio.removeAllItems();

        ArrayList<Servicio> servicios =
                altice.obtenerServiciosActivos();

        for (Servicio servicio : servicios) {

            comboServicio.addItem(
                    new OpcionCombo(
                            servicio.getCodigo(),
                            servicio.getNombre()
                    )
            );
        }
    }

    // =============================================
    // AGREGAR SERVICIO AL PLAN
    // =============================================

    private void agregarServicio() {

        OpcionCombo planSeleccionado =
                (OpcionCombo) comboPlan.getSelectedItem();

        OpcionCombo servicioSeleccionado =
                (OpcionCombo) comboServicio.getSelectedItem();

        if (planSeleccionado == null) {

            mostrarError("Primero debe registrar y seleccionar un plan.");
            return;
        }

        if (servicioSeleccionado == null) {

            mostrarError(
                    "No hay servicios activos disponibles para agregar."
            );
            return;
        }

        boolean agregado = altice.agregarServicioPlan(
                planSeleccionado.getCodigo(),
                servicioSeleccionado.getCodigo()
        );

        if (agregado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Servicio agregado correctamente al plan.",
                    "Operación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarServiciosAsociados();
            cargarTablaPlanes();

        } else {

            mostrarError(
                    "No se pudo agregar el servicio. "
                            + "Compruebe que el plan y el servicio estén activos "
                            + "y que el servicio no esté agregado anteriormente."
            );
        }
    }

    // =============================================
    // QUITAR SERVICIO DEL PLAN
    // =============================================

    private void quitarServicio() {

        OpcionCombo planSeleccionado =
                (OpcionCombo) comboPlan.getSelectedItem();

        int fila = tablaServiciosPlan.getSelectedRow();

        if (planSeleccionado == null) {

            mostrarError("Seleccione un plan.");
            return;
        }

        if (fila == -1) {

            mostrarError("Seleccione un servicio de la tabla.");
            return;
        }

        String codigoServicio =
                tablaServiciosPlan.getValueAt(fila, 0).toString();

        boolean eliminado = altice.quitarServicioPlan(
                planSeleccionado.getCodigo(),
                codigoServicio
        );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Servicio retirado del plan correctamente.",
                    "Operación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarServiciosAsociados();
            cargarTablaPlanes();

        } else {

            mostrarError("No se pudo retirar el servicio.");
        }
    }

    // =============================================
    // MOSTRAR SERVICIOS ASOCIADOS AL PLAN
    // =============================================

    private void cargarServiciosAsociados() {

        DefaultTableModel modelo = new DefaultTableModel() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripción");
        modelo.addColumn("Precio");

        OpcionCombo planSeleccionado =
                (OpcionCombo) comboPlan.getSelectedItem();

        if (planSeleccionado != null) {

            ArrayList<Servicio> servicios =
                    altice.obtenerServiciosPlan(
                            planSeleccionado.getCodigo()
                    );

            for (Servicio servicio : servicios) {

                modelo.addRow(new Object[] {
                        servicio.getCodigo(),
                        servicio.getNombre(),
                        servicio.getDescripcion(),
                        servicio.getPrecio()
                });
            }
        }

        tablaServiciosPlan.setModel(modelo);
        configurarTabla(tablaServiciosPlan);
    }

    // =============================================
    // SELECCIONAR PLAN POR CÓDIGO
    // =============================================

    private void seleccionarPlan(String codigo) {

        for (int i = 0; i < comboPlan.getItemCount(); i++) {

            OpcionCombo opcion = comboPlan.getItemAt(i);

            if (opcion.getCodigo().equals(codigo)) {
                comboPlan.setSelectedIndex(i);
                return;
            }
        }
    }

    // =============================================
    // OBTENER CÓDIGO DEL ELEMENTO SELECCIONADO
    // =============================================

    private String obtenerCodigoSeleccionado(
            JComboBox<OpcionCombo> combo) {

        OpcionCombo seleccionado =
                (OpcionCombo) combo.getSelectedItem();

        return seleccionado == null
                ? null
                : seleccionado.getCodigo();
    }

    // =============================================
    // FILTRO DE ENTRADA
    // =============================================

    private void aplicarFiltro(
            JTextField campo,
            String expresion,
            int maximoCaracteres) {

        AbstractDocument documento =
                (AbstractDocument) campo.getDocument();

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

                StringBuilder nuevo = new StringBuilder(actual);
                nuevo.insert(offset, texto);

                String resultado = nuevo.toString();

                if (resultado.length() <= maximoCaracteres
                        && resultado.matches(expresion)) {

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

                String resultado = nuevo.toString();

                if (resultado.length() <= maximoCaracteres
                        && resultado.matches(expresion)) {

                    super.replace(fb, offset, length, texto, attr);
                }
            }
        });
    }

    // =============================================
    // MOSTRAR ERRORES
    // =============================================

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =============================================
    // LIMPIAR CAMPOS
    // =============================================

    private void limpiarCampos() {

        txtNombre.setText("");
        txtDescripcion.setText("");
        txtDescuento.setText("");

        txtNombre.requestFocus();
    }

    // =============================================
    // ELEMENTO INTERNO PARA LOS COMBOBOX
    // =============================================

    private class OpcionCombo {

        private String codigo;
        private String nombre;

        public OpcionCombo(String codigo, String nombre) {
            this.codigo = codigo;
            this.nombre = nombre;
        }

        public String getCodigo() {
            return codigo;
        }

        @Override
        public String toString() {
            return nombre + " (" + codigo + ")";
        }
    }
}
