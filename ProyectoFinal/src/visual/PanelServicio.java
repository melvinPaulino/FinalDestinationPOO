
package visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import logico.Altice;
import logico.Servicio;

public class PanelServicio extends JPanel {

    private static final long serialVersionUID = 1L;

    // PALETA DE COLORES

    private final Color AZUL_OSCURO = new Color(13, 71, 161);
    private final Color AZUL_PRINCIPAL = new Color(25, 118, 210);
    private final Color AZUL_CLARO = new Color(227, 242, 253);
    private final Color FONDO = new Color(243, 247, 252);
    private final Color BLANCO = Color.WHITE;
    private final Color TEXTO = new Color(45, 55, 72);
    private final Color BORDE = new Color(215, 225, 238);

    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;

    private JTable tablaServicios;

    private Altice altice;

    public PanelServicio() {

        altice = Altice.getInstancia();

        setPreferredSize(new Dimension(1050, 710));
        setMinimumSize(new Dimension(1050, 710));
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setLayout(null);
        setBackground(FONDO);

        // ENCABEZADO

        JPanel panelTitulo = new JPanel(null);
        panelTitulo.setBounds(25, 18, 1000, 75);
        panelTitulo.setBackground(AZUL_OSCURO);
        add(panelTitulo);

        JLabel lblTitulo = new JLabel("GESTIÓN DE SERVICIOS");
        lblTitulo.setBounds(30, 12, 500, 32);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        lblTitulo.setForeground(BLANCO);
        panelTitulo.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel(
                "Administración y registro de servicios de Altice"
        );
        lblSubtitulo.setBounds(32, 45, 600, 20);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(210, 225, 245));
        panelTitulo.add(lblSubtitulo);

        // PANEL DEL FORMULARIO

        JPanel panelFormulario = new JPanel(null);
        panelFormulario.setBounds(25, 110, 1000, 250);
        panelFormulario.setBackground(BLANCO);
        panelFormulario.setBorder(new LineBorder(BORDE, 1, true));
        add(panelFormulario);

        JLabel lblDatos = new JLabel("Información del servicio");
        lblDatos.setBounds(25, 12, 400, 28);
        lblDatos.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblDatos.setForeground(AZUL_OSCURO);
        panelFormulario.add(lblDatos);

        // NOMBRE

        agregarEtiqueta(
                panelFormulario,
                "Nombre del servicio:",
                30,
                61
        );

        txtNombre = crearCampo(
                panelFormulario,
                190,
                57,
                780
        );

        // DESCRIPCION

        agregarEtiqueta(
                panelFormulario,
                "Descripción:",
                30,
                125
        );

        txtDescripcion = crearCampo(
                panelFormulario,
                190,
                121,
                780
        );

        // PRECIO

        agregarEtiqueta(
                panelFormulario,
                "Precio:",
                30,
                189
        );

        txtPrecio = crearCampo(
                panelFormulario,
                190,
                185,
                300
        );

        // VALIDACIONES AL ESCRIBIR

        aplicarFiltro(txtNombre, "[^\\p{Cntrl}]*", 80);

        aplicarFiltro(txtDescripcion, "[^\\p{Cntrl}]*", 500);

        aplicarFiltro(txtPrecio, "\\d*(\\.\\d{0,2})?", 12);

        // BOTONES

        JButton btnRegistrar = new JButton("Registrar servicio");
        btnRegistrar.setBounds(695, 375, 175, 40);
        configurarBotonPrincipal(btnRegistrar);
        add(btnRegistrar);

        JButton btnLimpiar = new JButton("Limpiar campos");
        btnLimpiar.setBounds(885, 375, 140, 40);
        configurarBotonSecundario(btnLimpiar);
        add(btnLimpiar);

        // PANEL DE LA TABLA

        JPanel panelTabla = new JPanel(null);
        panelTabla.setBounds(25, 435, 1000, 245);
        panelTabla.setBackground(BLANCO);
        panelTabla.setBorder(new LineBorder(BORDE, 1, true));
        add(panelTabla);

        JLabel lblTabla = new JLabel("Servicios registrados");
        lblTabla.setBounds(25, 12, 400, 28);
        lblTabla.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblTabla.setForeground(AZUL_OSCURO);
        panelTabla.add(lblTabla);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(18, 48, 964, 180);
        scrollPane.setBorder(new LineBorder(BORDE, 1));
        panelTabla.add(scrollPane);

        tablaServicios = new JTable();
        configurarTabla();
        scrollPane.setViewportView(tablaServicios);

        // CARGAR DATOS

        cargarTabla();

        // EVENTO REGISTRAR

        btnRegistrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                registrarServicio();
            }
        });

        // EVENTO LIMPIAR

        btnLimpiar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
    }

    // CREAR ETIQUETAS

    private void agregarEtiqueta(
            JPanel panel,
            String texto,
            int x,
            int y) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setBounds(x, y, 155, 30);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));
        etiqueta.setForeground(TEXTO);

        panel.add(etiqueta);
    }

    // CREAR CAMPOS

    private JTextField crearCampo(
            JPanel panel,
            int x,
            int y,
            int ancho) {

        JTextField campo = new JTextField();

        configurarCampo(campo);

        campo.setBounds(x, y, ancho, 30);

        panel.add(campo);

        return campo;
    }

    // ESTILO DE LOS CAMPOS

    private void configurarCampo(JTextField campo) {

        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setForeground(TEXTO);
        campo.setBackground(new Color(250, 252, 255));

        campo.setBorder(
                new LineBorder(BORDE, 1, true)
        );

        campo.setMargin(new java.awt.Insets(4, 10, 4, 10));
    }

    // BOTÓN PRINCIPAL

    private void configurarBotonPrincipal(JButton boton) {

        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setForeground(BLANCO);
        boton.setBackground(AZUL_PRINCIPAL);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(
                new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)
        );
    }

    // BOTÓN SECUNDARIO

    private void configurarBotonSecundario(JButton boton) {

        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setForeground(AZUL_OSCURO);
        boton.setBackground(AZUL_CLARO);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(
                new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)
        );
    }

    // CONFIGURAR TABLA

    private void configurarTabla() {

        tablaServicios.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        tablaServicios.setForeground(TEXTO);
        tablaServicios.setBackground(BLANCO);

        tablaServicios.setRowHeight(30);
        tablaServicios.setShowVerticalLines(false);
        tablaServicios.setShowHorizontalLines(true);

        tablaServicios.setGridColor(
                new Color(230, 236, 244)
        );

        tablaServicios.setSelectionBackground(AZUL_CLARO);
        tablaServicios.setSelectionForeground(AZUL_OSCURO);

        tablaServicios.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaServicios.setFillsViewportHeight(true);

        JTableHeader encabezado = tablaServicios.getTableHeader();

        encabezado.setBackground(AZUL_OSCURO);
        encabezado.setForeground(BLANCO);
        encabezado.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        encabezado.setPreferredSize(
                new Dimension(0, 35)
        );

        encabezado.setReorderingAllowed(false);

        // FILAS ALTERNADAS

        DefaultTableCellRenderer renderizador =
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

                super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );

                setBorder(
                        new EmptyBorder(0, 8, 0, 8)
                );

                if (isSelected) {

                    setBackground(AZUL_CLARO);
                    setForeground(AZUL_OSCURO);

                } else {

                    if (row % 2 == 0) {
                        setBackground(BLANCO);
                    } else {
                        setBackground(new Color(246, 249, 253));
                    }

                    setForeground(TEXTO);
                }

                return this;
            }
        };

        tablaServicios.setDefaultRenderer(
                Object.class,
                renderizador
        );
    }

    // REGISTRAR SERVICIO

    private void registrarServicio() {

        String nombre = txtNombre.getText().trim();

        String descripcion = txtDescripcion.getText().trim();

        String textoPrecio = txtPrecio.getText().trim();

        // VALIDAR CAMPOS VACÍOS

        if (nombre.isEmpty()
                || descripcion.isEmpty()
                || textoPrecio.isEmpty()) {

            mostrarError("Debe completar todos los campos.");
            return;
        }

        // VALIDAR NOMBRE

        if (nombre.length() < 2 || nombre.length() > 80
                || !nombre.matches(".*\\p{L}.*")) {

            mostrarError(
                    "El nombre debe tener entre 2 y 80 caracteres e incluir letras."
            );
            return;
        }

        // VALIDAR DESCRIPCIÓN

        if (descripcion.length() < 5 || descripcion.length() > 500) {

            mostrarError(
                    "La descripción debe tener entre 5 y 500 caracteres."
            );
            return;
        }

        // VALIDAR PRECIO

        BigDecimal precioDecimal;

        try {

            precioDecimal = new BigDecimal(textoPrecio);

        } catch (NumberFormatException e) {

            mostrarError("El precio debe ser un número válido.");
            return;
        }

        if (precioDecimal.compareTo(BigDecimal.ZERO) < 0) {

            mostrarError("El precio no puede ser negativo.");
            return;
        }

        if (precioDecimal.scale() > 2) {

            mostrarError(
                    "El precio solamente puede tener dos decimales."
            );
            return;
        }

        float precio = precioDecimal.floatValue();

        if (!Float.isFinite(precio)) {

            mostrarError(
                    "El precio introducido es demasiado grande."
            );
            return;
        }

        // GENERAR CÓDIGO AUTOMÁTICAMENTE

        String codigo = generarCodigoServicio();

        // CREAR SERVICIO

        Servicio servicio = new Servicio(
                codigo,
                nombre,
                descripcion,
                precio,
                true
        );

        // REGISTRAR SERVICIO

        boolean registrado = altice.registrarServicio(servicio);

        if (registrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Servicio registrado correctamente.\n"
                            + "Código generado: " + codigo,
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();

            cargarTabla();

        } else {

            mostrarError(
                    "No se pudo registrar el servicio. "
                            + "Verifique los datos e inténtelo nuevamente."
            );
        }
    }

    // GENERAR CÓDIGO AUTOMÁTICAMENTE

    private String generarCodigoServicio() {

        ArrayList<Servicio> servicios = altice.obtenerServicios();

        Set<String> codigos = new HashSet<>();

        int siguiente = 1;

        Pattern patron = Pattern.compile(
                "SER-(\\d+)",
                Pattern.CASE_INSENSITIVE
        );

        for (Servicio servicio : servicios) {

            if (servicio.getCodigo() == null) {
                continue;
            }

            String codigoExistente =
                    servicio.getCodigo().trim().toUpperCase();

            codigos.add(codigoExistente);

            Matcher matcher = patron.matcher(codigoExistente);

            if (matcher.matches()) {

                try {

                    int numero = Integer.parseInt(matcher.group(1));

                    if (numero >= siguiente && numero < Integer.MAX_VALUE) {
                        siguiente = numero + 1;
                    }

                } catch (NumberFormatException e) {
                    // Ignorar codigos antiguos que no sean numéricos.
                }
            }
        }

        String codigo;

        do {

            codigo = String.format("SER-%04d", siguiente);
            siguiente++;

        } while (codigos.contains(codigo));

        return codigo;
    }

    // FILTROS DE ENTRADA

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

    // MOSTRAR ERRORES

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // CARGAR TABLA

    private void cargarTabla() {

        DefaultTableModel modelo = new DefaultTableModel() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripción");
        modelo.addColumn("Precio");
        modelo.addColumn("Estado");

        ArrayList<Servicio> servicios = altice.obtenerServicios();

        for (Servicio servicio : servicios) {

            modelo.addRow(new Object[] {

                    servicio.getCodigo(),
                    servicio.getNombre(),
                    servicio.getDescripcion(),
                    servicio.getPrecio(),
                    servicio.isEstado() ? "Activo" : "Inactivo"

            });
        }

        tablaServicios.setModel(modelo);

        // RESTAURAR EL ESTILO DESPUÉS DE ACTUALIZAR EL MODELO

        configurarTabla();
    }

    // LIMPIAR CAMPOS

    private void limpiarCampos() {

        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");

        txtNombre.requestFocus();
    }
}
