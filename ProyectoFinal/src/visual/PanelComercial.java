
package visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.JPasswordField;
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
import logico.Comercial;
import logico.Empleado;

public class PanelComercial extends JPanel {

    private static final long serialVersionUID = 1L;

    // PALETA DE COLORES

    private final Color AZUL_OSCURO = new Color(13, 71, 161);
    private final Color AZUL_PRINCIPAL = new Color(25, 118, 210);
    private final Color AZUL_CLARO = new Color(227, 242, 253);
    private final Color FONDO = new Color(243, 247, 252);
    private final Color BLANCO = Color.WHITE;
    private final Color TEXTO = new Color(45, 55, 72);
    private final Color BORDE = new Color(215, 225, 238);

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;

    private JTextField txtSalarioBase;
    private JTextField txtMetaMensual;
    private JTextField txtComision;

    private JTextField txtUsuario;
    private JPasswordField txtClave;

    private JTable tablaComerciales;

    private Altice altice;

    public PanelComercial() {

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

        JLabel lblTitulo = new JLabel("GESTIÓN DE COMERCIALES");
        lblTitulo.setBounds(30, 12, 600, 32);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        lblTitulo.setForeground(BLANCO);
        panelTitulo.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel(
                "Administración, registro y gestión del personal comercial de Altice"
        );
        lblSubtitulo.setBounds(32, 45, 750, 20);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(210, 225, 245));
        panelTitulo.add(lblSubtitulo);

        // PANEL DEL FORMULARIO

        JPanel panelFormulario = new JPanel(null);
        panelFormulario.setBounds(25, 110, 1000, 330);
        panelFormulario.setBackground(BLANCO);
        panelFormulario.setBorder(new LineBorder(BORDE, 1, true));
        add(panelFormulario);

        JLabel lblDatos = new JLabel("Información del comercial");
        lblDatos.setBounds(25, 12, 450, 28);
        lblDatos.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblDatos.setForeground(AZUL_OSCURO);
        panelFormulario.add(lblDatos);

        // COLUMNA IZQUIERDA: DATOS PERSONALES

        agregarEtiqueta(panelFormulario, "Cédula:", 30, 62);
        txtCedula = crearCampo(panelFormulario, 165, 58, 305);

        agregarEtiqueta(panelFormulario, "Nombre:", 30, 106);
        txtNombre = crearCampo(panelFormulario, 165, 102, 305);

        agregarEtiqueta(panelFormulario, "Apellido:", 30, 150);
        txtApellido = crearCampo(panelFormulario, 165, 146, 305);

        agregarEtiqueta(panelFormulario, "Teléfono:", 30, 194);
        txtTelefono = crearCampo(panelFormulario, 165, 190, 305);

        agregarEtiqueta(panelFormulario, "Correo electrónico:", 30, 238);
        txtCorreo = crearCampo(panelFormulario, 165, 234, 305);

        agregarEtiqueta(panelFormulario, "Dirección:", 30, 282);
        txtDireccion = crearCampo(panelFormulario, 165, 278, 305);

        // COLUMNA DERECHA: DATOS LABORALES Y ACCESO

        agregarEtiqueta(panelFormulario, "Salario base:", 510, 62);
        txtSalarioBase = crearCampo(panelFormulario, 665, 58, 305);

        agregarEtiqueta(panelFormulario, "Meta mensual:", 510, 106);
        txtMetaMensual = crearCampo(panelFormulario, 665, 102, 305);

        agregarEtiqueta(panelFormulario, "Comisión (%):", 510, 150);
        txtComision = crearCampo(panelFormulario, 665, 146, 305);

        agregarEtiqueta(panelFormulario, "Usuario:", 510, 194);
        txtUsuario = crearCampo(panelFormulario, 665, 190, 305);

        agregarEtiqueta(panelFormulario, "Contraseña:", 510, 238);

        txtClave = new JPasswordField();
        configurarCampo(txtClave);
        txtClave.setBounds(665, 234, 305, 30);
        panelFormulario.add(txtClave);

        // VALIDACIONES AL ESCRIBIR

        aplicarFiltro(txtCedula, "[0-9/\\- ]*");
        aplicarFiltro(txtNombre, "[\\p{L} '\\-]*");
        aplicarFiltro(txtApellido, "[\\p{L} '\\-]*");
        aplicarFiltro(txtTelefono, "[0-9/()+\\- ]*");
        aplicarFiltro(txtCorreo, "[a-zA-Z0-9@._%+\\-]*");
        aplicarFiltro(txtSalarioBase, "[0-9.]*");
        aplicarFiltro(txtMetaMensual, "[0-9.]*");
        aplicarFiltro(txtComision, "[0-9.]*");
        aplicarFiltro(txtUsuario, "[a-zA-Z0-9._\\-]*");
        aplicarFiltroClave();

        // BOTONES

        JButton btnRegistrar = new JButton("Registrar comercial");
        btnRegistrar.setBounds(685, 455, 190, 40);
        configurarBotonPrincipal(btnRegistrar);
        add(btnRegistrar);

        JButton btnLimpiar = new JButton("Limpiar campos");
        btnLimpiar.setBounds(890, 455, 135, 40);
        configurarBotonSecundario(btnLimpiar);
        add(btnLimpiar);

        // PANEL DE LA TABLA

        JPanel panelTabla = new JPanel(null);
        panelTabla.setBounds(25, 510, 1000, 170);
        panelTabla.setBackground(BLANCO);
        panelTabla.setBorder(new LineBorder(BORDE, 1, true));
        add(panelTabla);

        JLabel lblTabla = new JLabel("Comerciales registrados");
        lblTabla.setBounds(25, 10, 450, 28);
        lblTabla.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblTabla.setForeground(AZUL_OSCURO);
        panelTabla.add(lblTabla);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(18, 45, 964, 110);
        scrollPane.setBorder(new LineBorder(BORDE, 1));
        panelTabla.add(scrollPane);

        tablaComerciales = new JTable();
        configurarTabla();
        scrollPane.setViewportView(tablaComerciales);

        // CARGAR DATOS

        cargarTabla();

        // EVENTO REGISTRAR

        btnRegistrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                registrarComercial();
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

        etiqueta.setBounds(x, y, 130, 30);
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

        campo.setBorder(new LineBorder(BORDE, 1, true));

        campo.setMargin(new java.awt.Insets(4, 10, 4, 10));
    }

    // BOTON PRINCIPAL

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

    // BOTON SECUNDARIO

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

        tablaComerciales.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        tablaComerciales.setForeground(TEXTO);
        tablaComerciales.setBackground(BLANCO);

        tablaComerciales.setRowHeight(29);
        tablaComerciales.setShowVerticalLines(false);
        tablaComerciales.setShowHorizontalLines(true);

        tablaComerciales.setGridColor(
                new Color(230, 236, 244)
        );

        tablaComerciales.setSelectionBackground(AZUL_CLARO);
        tablaComerciales.setSelectionForeground(AZUL_OSCURO);

        tablaComerciales.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaComerciales.setFillsViewportHeight(true);
        tablaComerciales.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader encabezado = tablaComerciales.getTableHeader();

        encabezado.setBackground(AZUL_OSCURO);
        encabezado.setForeground(BLANCO);
        encabezado.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        encabezado.setPreferredSize(new Dimension(0, 34));
        encabezado.setReorderingAllowed(false);

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

                setBorder(new EmptyBorder(0, 8, 0, 8));

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

        tablaComerciales.setDefaultRenderer(
                Object.class,
                renderizador
        );
    }

    // REGISTRAR COMERCIAL

    private void registrarComercial() {

        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();

        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());

        if (cedula.isEmpty() || nombre.isEmpty()
                || apellido.isEmpty() || telefono.isEmpty()
                || correo.isEmpty() || direccion.isEmpty()
                || usuario.isEmpty() || clave.isEmpty()
                || txtSalarioBase.getText().trim().isEmpty()
                || txtMetaMensual.getText().trim().isEmpty()
                || txtComision.getText().trim().isEmpty()) {

            mostrarError("Debe completar todos los campos.");
            return;
        }

        if (!validarCedula(cedula)) {
            mostrarError("La cédula dominicana no es válida.");
            return;
        }

        cedula = formatearCedula(cedula);

        if (!validarNombre(nombre)) {
            mostrarError("El nombre contiene caracteres inválidos.");
            return;
        }

        if (!validarNombre(apellido)) {
            mostrarError("El apellido contiene caracteres inválidos.");
            return;
        }

        if (!validarTelefono(telefono)) {
            mostrarError("El teléfono debe tener 10 dígitos y comenzar con 809, 829 o 849.");
            return;
        }

        telefono = formatearTelefono(telefono);

        if (!validarCorreo(correo)) {
            mostrarError("Introduzca un correo electrónico válido.");
            return;
        }

        if (direccion.length() < 5 || direccion.length() > 150) {
            mostrarError("La dirección debe tener entre 5 y 150 caracteres.");
            return;
        }

        if (!usuario.matches("[a-zA-Z0-9._-]{4,20}")) {
            mostrarError("El usuario debe tener entre 4 y 20 caracteres y no puede contener espacios.");
            return;
        }

        if (!clave.matches("(?=.*\\p{L})(?=.*\\d)[^\\s]{8,64}")) {
            mostrarError("La contraseña debe tener entre 8 y 64 caracteres, incluir letras y números y no contener espacios.");
            return;
        }

        BigDecimal salario;
        BigDecimal meta;
        BigDecimal comision;

        try {
            salario = new BigDecimal(txtSalarioBase.getText().trim());
            meta = new BigDecimal(txtMetaMensual.getText().trim());
            comision = new BigDecimal(txtComision.getText().trim());

        } catch (NumberFormatException e) {
            mostrarError("Salario, meta y comisión deben contener números válidos.");
            return;
        }

        if (salario.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarError("El salario base debe ser mayor que cero.");
            return;
        }

        if (meta.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarError("La meta mensual debe ser mayor que cero.");
            return;
        }

        if (comision.compareTo(BigDecimal.ZERO) < 0
                || comision.compareTo(new BigDecimal("100")) > 0) {
            mostrarError("La comisión debe estar entre 0 y 100.");
            return;
        }

        float salarioBase = salario.floatValue();
        float metaMensual = meta.floatValue();
        float porcientoComision = comision.floatValue();

        if (!Float.isFinite(salarioBase)
                || !Float.isFinite(metaMensual)
                || !Float.isFinite(porcientoComision)) {
            mostrarError("Los valores numéricos son demasiado grandes.");
            return;
        }

        String codigo = generarCodigoComercial();

        Comercial comercial = new Comercial(
                cedula,
                nombre,
                apellido,
                telefono,
                correo,
                direccion,
                codigo,
                salarioBase,
                new Date(),
                metaMensual,
                porcientoComision
        );

        try {
            comercial.setUsuario(usuario);
            comercial.setClave(clave);

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
            return;
        }

        boolean registrado = altice.registrarEmpleado(comercial);

        if (registrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Comercial registrado correctamente.\nCódigo generado: " + codigo,
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarTabla();

        } else {

            mostrarError("No se pudo registrar el comercial. Verifique que la cédula, usuario o código no estén repetidos.");
        }
    }

    // GENERAR CODIGO AUTOMÁTICO

    private String generarCodigoComercial() {

        ArrayList<Empleado> empleados =
                altice.obtenerEmpleadosPorTipo(Empleado.class);

        Set<String> codigos = new HashSet<>();

        int siguiente = 1;

        Pattern patron = Pattern.compile("COM-(\\d+)", Pattern.CASE_INSENSITIVE);

        for (Empleado empleado : empleados) {

            if (empleado.getCodigo() == null) {
                continue;
            }

            String codigoExistente = empleado.getCodigo().trim();
            codigos.add(codigoExistente.toUpperCase());

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
            codigo = String.format("COM-%04d", siguiente);
            siguiente++;
        } while (codigos.contains(codigo));

        return codigo;
    }

    // VALIDAR CEDULA

    private boolean validarCedula(String cedula) {

        if (cedula == null || !cedula.matches("[0-9\\-/ ]+")) {
            return false;
        }

        String digitos = cedula.replaceAll("[^0-9]", "");

        if (!digitos.matches("\\d{11}")
                || digitos.matches("(\\d)\\1{10}")) {
            return false;
        }

        int suma = 0;

        for (int i = 0; i < 10; i++) {

            int numero = Character.getNumericValue(digitos.charAt(i));

            if (i % 2 == 1) {
                numero *= 2;
            }

            if (numero > 9) {
                numero -= 9;
            }

            suma += numero;
        }

        int verificador = (10 - (suma % 10)) % 10;

        return verificador ==
                Character.getNumericValue(digitos.charAt(10));
    }

    // FORMATEAR CÉDULA

    private String formatearCedula(String cedula) {

        String digitos = cedula.replaceAll("[^0-9]", "");

        return digitos.substring(0, 3) + "-"
                + digitos.substring(3, 10) + "-"
                + digitos.substring(10);
    }

    // VALIDAR NOMBRE Y APELLIDO

    private boolean validarNombre(String nombre) {

        return nombre != null
                && nombre.length() >= 2
                && nombre.length() <= 50
                && nombre.matches("[\\p{L}]+([ '-][\\p{L}]+)*");
    }

    // VALIDAR TELÉFONO

    private boolean validarTelefono(String telefono) {

        if (telefono == null || !telefono.matches("[0-9/()+\\- ]+")) {
            return false;
        }

        String digitos = telefono.replaceAll("[^0-9]", "");

        return digitos.matches("(809|829|849)\\d{7}");
    }

    // FORMATEAR TELÉFONO

    private String formatearTelefono(String telefono) {

        String digitos = telefono.replaceAll("[^0-9]", "");

        return digitos.substring(0, 3) + "-"
                + digitos.substring(3, 6) + "-"
                + digitos.substring(6);
    }

    // VALIDAR CORREO

    private boolean validarCorreo(String correo) {

        if (correo == null || correo.length() > 254) {
            return false;
        }

        return correo.matches(
                "^[A-Za-z0-9]+([._%+-][A-Za-z0-9]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*"
                + "\\.[A-Za-z]{2,}$"
        );
    }

    // MOSTRAR ERROR

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // FILTROS DE ENTRADA

    private void aplicarFiltro(JTextField campo, String expresion) {

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
                        0, fb.getDocument().getLength());

                String nuevo = new StringBuilder(actual)
                        .insert(offset, texto)
                        .toString();

                if (nuevo.matches(expresion)) {
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
                        0, fb.getDocument().getLength());

                StringBuilder nuevo = new StringBuilder(actual);
                nuevo.replace(offset, offset + length, texto);

                if (nuevo.toString().matches(expresion)) {
                    super.replace(fb, offset, length, texto, attr);
                }
            }
        });
    }

    // FILTRO DE CONTRASEÑA

    private void aplicarFiltroClave() {

        AbstractDocument documento =
                (AbstractDocument) txtClave.getDocument();

        documento.setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(
                    FilterBypass fb,
                    int offset,
                    String texto,
                    AttributeSet attr)
                    throws BadLocationException {

                if (texto == null || !texto.matches("\\S*")) return;

                super.insertString(fb, offset, texto, attr);
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

                if (!texto.matches("\\S*")) return;

                super.replace(fb, offset, length, texto, attr);
            }
        });
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
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Salario");
        modelo.addColumn("Meta mensual");
        modelo.addColumn("% Comisión");
        modelo.addColumn("Usuario");
        modelo.addColumn("Estado");

        ArrayList<Comercial> comerciales =
                altice.obtenerEmpleadosPorTipo(Comercial.class);

        for (Comercial comercial : comerciales) {

            modelo.addRow(new Object[] {
                    comercial.getCodigo(),
                    comercial.getCedula(),
                    comercial.getNombre(),
                    comercial.getApellido(),
                    comercial.getSalarioBase(),
                    comercial.getMetaMensual(),
                    comercial.getPorcientoComision(),
                    comercial.getUsuario(),
                    comercial.isActivo() ? "Activo" : "Inactivo"
            });
        }

        tablaComerciales.setModel(modelo);

        configurarTabla();
    }

    // LIMPIAR CAMPOS

    private void limpiarCampos() {

        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");

        txtSalarioBase.setText("");
        txtMetaMensual.setText("");
        txtComision.setText("");

        txtUsuario.setText("");
        txtClave.setText("");

        txtCedula.requestFocus();
    }
}
