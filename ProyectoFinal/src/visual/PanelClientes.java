
package visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import logico.Cliente;

public class PanelClientes extends JPanel {

    private static final long serialVersionUID = 1L;

    // PALETA DE COLORES

    private final Color AZUL_OSCURO = new Color(13, 71, 161);
    private final Color AZUL_PRINCIPAL = new Color(25, 118, 210);
    private final Color AZUL_CLARO = new Color(227, 242, 253);
    private final Color FONDO = new Color(243, 247, 252);
    private final Color BLANCO = Color.WHITE;
    private final Color TEXTO = new Color(45, 55, 72);
    private final Color GRIS = new Color(110, 120, 135);
    private final Color BORDE = new Color(215, 225, 238);

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JTextField txtUsuario;
    private JPasswordField txtClave;

    private JTable tablaClientes;

    private Altice altice;

    public PanelClientes() {

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

        JLabel lblTitulo = new JLabel("GESTIÓN DE CLIENTES");
        lblTitulo.setBounds(30, 12, 500, 32);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        lblTitulo.setForeground(BLANCO);
        panelTitulo.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel(
                "Administración y registro de clientes de Altice"
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

        JLabel lblDatos = new JLabel("Información del cliente");
        lblDatos.setBounds(25, 12, 400, 28);
        lblDatos.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblDatos.setForeground(AZUL_OSCURO);
        panelFormulario.add(lblDatos);

        // COLUMNA IZQUIERDA

        agregarEtiqueta(panelFormulario, "Cédula:", 30, 61);
        txtCedula = crearCampo(panelFormulario, 145, 57, 325);

        agregarEtiqueta(panelFormulario, "Nombre:", 30, 108);
        txtNombre = crearCampo(panelFormulario, 145, 104, 325);

        agregarEtiqueta(panelFormulario, "Apellido:", 30, 155);
        txtApellido = crearCampo(panelFormulario, 145, 151, 325);

        agregarEtiqueta(panelFormulario, "Teléfono:", 30, 202);
        txtTelefono = crearCampo(panelFormulario, 145, 198, 325);

        // COLUMNA DERECHA

        agregarEtiqueta(panelFormulario, "Correo electrónico:", 510, 61);
        txtCorreo = crearCampo(panelFormulario, 650, 57, 320);

        agregarEtiqueta(panelFormulario, "Dirección:", 510, 108);
        txtDireccion = crearCampo(panelFormulario, 650, 104, 320);

        agregarEtiqueta(panelFormulario, "Usuario:", 510, 155);
        txtUsuario = crearCampo(panelFormulario, 650, 151, 320);

        agregarEtiqueta(panelFormulario, "Contraseña:", 510, 202);

        txtClave = new JPasswordField();
        configurarCampo(txtClave);
        txtClave.setBounds(650, 198, 320, 30);
        panelFormulario.add(txtClave);

        // VALIDACIONES AL ESCRIBIR

        configurarFiltros();

        // BOTONES

        JButton btnRegistrar = new JButton("Registrar cliente");
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

        JLabel lblTabla = new JLabel("Clientes registrados");
        lblTabla.setBounds(25, 12, 400, 28);
        lblTabla.setFont(new Font("SansSerif", Font.BOLD, 17));
        lblTabla.setForeground(AZUL_OSCURO);
        panelTabla.add(lblTabla);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(18, 48, 964, 180);
        scrollPane.setBorder(new LineBorder(BORDE, 1));
        panelTabla.add(scrollPane);

        tablaClientes = new JTable();
        configurarTabla();
        scrollPane.setViewportView(tablaClientes);

        // CARGAR DATOS

        cargarTabla();

        // EVENTO REGISTRAR

        btnRegistrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                registrarCliente();
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

        etiqueta.setBounds(x, y, 135, 30);
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

        tablaClientes.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        tablaClientes.setForeground(TEXTO);
        tablaClientes.setBackground(BLANCO);

        tablaClientes.setRowHeight(30);
        tablaClientes.setShowVerticalLines(false);
        tablaClientes.setShowHorizontalLines(true);

        tablaClientes.setGridColor(
                new Color(230, 236, 244)
        );

        tablaClientes.setSelectionBackground(AZUL_CLARO);
        tablaClientes.setSelectionForeground(AZUL_OSCURO);

        tablaClientes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaClientes.setFillsViewportHeight(true);

        JTableHeader encabezado = tablaClientes.getTableHeader();

        encabezado.setBackground(AZUL_OSCURO);
        encabezado.setForeground(BLANCO);
        encabezado.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        encabezado.setPreferredSize(
                new Dimension(0, 35)
        );

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

        tablaClientes.setDefaultRenderer(
                Object.class,
                renderizador
        );
    }

    // CONFIGURAR FILTROS DE LOS CAMPOS

    private void configurarFiltros() {

        aplicarFiltro(txtCedula, "[0-9/-]*", 13);

        aplicarFiltro(txtNombre, "[\\p{L} .'-]*", 40);
        aplicarFiltro(txtApellido, "[\\p{L} .'-]*", 40);

        aplicarFiltro(txtTelefono, "[0-9/-]*", 12);

        aplicarFiltro(txtCorreo, "[A-Za-z0-9._%+@-]*", 100);

        aplicarFiltro(txtDireccion, "[\\p{L}\\p{N} .,/#'-]*", 150);

        aplicarFiltro(txtUsuario, "[A-Za-z0-9._-]*", 30);
    }

    // MÉTODO GENERAL PARA FILTRAR ENTRADAS

    private void aplicarFiltro(
            JTextField campo,
            String expresion,
            int longitudMaxima) {

        AbstractDocument documento =
                (AbstractDocument) campo.getDocument();

        documento.setDocumentFilter(
                new FiltroEntrada(expresion, longitudMaxima)
        );
    }

    // FILTRO PERSONALIZADO

    private class FiltroEntrada extends DocumentFilter {

        private Pattern patron;
        private int longitudMaxima;

        public FiltroEntrada(
                String expresion,
                int longitudMaxima) {

            patron = Pattern.compile(expresion);
            this.longitudMaxima = longitudMaxima;
        }

        @Override
        public void insertString(
                FilterBypass fb,
                int offset,
                String texto,
                AttributeSet attr)
                throws BadLocationException {

            reemplazar(fb, offset, 0, texto, attr);
        }

        @Override
        public void replace(
                FilterBypass fb,
                int offset,
                int length,
                String texto,
                AttributeSet attrs)
                throws BadLocationException {

            reemplazar(fb, offset, length, texto, attrs);
        }

        private void reemplazar(
                FilterBypass fb,
                int offset,
                int length,
                String texto,
                AttributeSet attrs)
                throws BadLocationException {

            if (texto == null) {
                return;
            }

            String actual = fb.getDocument().getText(
                    0,
                    fb.getDocument().getLength()
            );

            String nuevo = actual.substring(0, offset)
                    + texto
                    + actual.substring(offset + length);

            if (nuevo.length() <= longitudMaxima
                    && patron.matcher(nuevo).matches()) {

                fb.replace(offset, length, texto, attrs);
            }
        }
    }

    // REGISTRAR CLIENTE

    private void registrarCliente() {

        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String usuario = txtUsuario.getText().trim();

        char[] claveCaracteres = txtClave.getPassword();
        String clave = new String(claveCaracteres).trim();

        if (cedula.isEmpty() || nombre.isEmpty()
                || apellido.isEmpty() || telefono.isEmpty()
                || correo.isEmpty() || direccion.isEmpty()
                || usuario.isEmpty() || clave.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe completar todos los campos.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!validarCedulaDominicana(cedula)) {

            JOptionPane.showMessageDialog(
                    this,
                    "La cédula dominicana no es válida.\n"
                            + "Debe contener 11 dígitos y tener "
                            + "un dígito verificador correcto.",
                    "Cédula incorrecta",
                    JOptionPane.ERROR_MESSAGE
            );

            txtCedula.requestFocus();
            return;
        }

        if (!validarNombre(nombre)
                || !validarNombre(apellido)) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre y el apellido deben contener "
                            + "al menos una letra y tener un formato válido.",
                    "Nombre incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (!validarTelefonoDominicano(telefono)) {

            JOptionPane.showMessageDialog(
                    this,
                    "El teléfono debe tener 10 dígitos "
                            + "y comenzar con 809, 829 o 849.",
                    "Teléfono incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );

            txtTelefono.requestFocus();
            return;
        }

        if (!validarCorreo(correo)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Introduzca un correo electrónico válido.",
                    "Correo incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );

            txtCorreo.requestFocus();
            return;
        }

        if (direccion.length() < 5) {

            JOptionPane.showMessageDialog(
                    this,
                    "La dirección debe contener al menos 5 caracteres.",
                    "Dirección incorrecta",
                    JOptionPane.ERROR_MESSAGE
            );

            txtDireccion.requestFocus();
            return;
        }

        if (usuario.length() < 4) {

            JOptionPane.showMessageDialog(
                    this,
                    "El usuario debe tener al menos 4 caracteres.",
                    "Usuario incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );

            txtUsuario.requestFocus();
            return;
        }

        if (!validarClave(clave)) {

            JOptionPane.showMessageDialog(
                    this,
                    "La contraseña debe tener al menos 8 caracteres, "
                            + "una letra y un número.",
                    "Contraseña incorrecta",
                    JOptionPane.ERROR_MESSAGE
            );

            txtClave.requestFocus();
            return;
        }

        // NORMALIZAR DATOS

        cedula = formatearCedula(cedula);
        telefono = formatearTelefono(telefono);

        // GENERAR CÓDIGO

        int id = Altice.getGeneradorIdPersona();
        String codigo;

        boolean repetido;

        do {

            codigo = "CLI-" + id;
            repetido = false;

            for (Cliente c : altice.obtenerClientes()) {

                if (c.getCodigo().equalsIgnoreCase(codigo)) {

                    id++;
                    repetido = true;
                    break;
                }
            }

        } while (repetido);

        Altice.setGeneradorIdPersona(id + 1);

        // CREAR CLIENTE

        Cliente cliente = new Cliente(
                cedula,
                nombre,
                apellido,
                telefono,
                correo,
                direccion,
                codigo,
                new Date()
        );

        // CONFIGURAR CREDENCIALES

        cliente.setUsuario(usuario);
        cliente.setClave(clave);

        // REGISTRAR EN ALTICE

        boolean registrado = altice.registrarCliente(cliente);

        if (registrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente registrado correctamente.\n"
                            + "Código: " + cliente.getCodigo(),
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el cliente.\n"
                            + "Verifique que la cédula, usuario "
                            + "o código no estén repetidos.",
                    "Error de registro",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        java.util.Arrays.fill(claveCaracteres, '\0');
    }

    // VALIDAR CÉDULA DOMINICANA

    private boolean validarCedulaDominicana(String cedula) {

        String numero = cedula.replaceAll("[/-]", "");

        if (!numero.matches("[0-9]{11}")) {
            return false;
        }

        if (numero.matches("(\\d)\\1{10}")) {
            return false;
        }

        int suma = 0;

        for (int i = 0; i < 10; i++) {

            int digito = Character.getNumericValue(
                    numero.charAt(i)
            );

            int multiplicador = (i % 2 == 0) ? 1 : 2;

            int resultado = digito * multiplicador;

            if (resultado > 9) {
                resultado -= 9;
            }

            suma += resultado;
        }

        int verificador = (10 - (suma % 10)) % 10;

        int ultimoDigito = Character.getNumericValue(
                numero.charAt(10)
        );

        return verificador == ultimoDigito;
    }

    // FORMATEAR CÉDULA

    private String formatearCedula(String cedula) {

        String numero = cedula.replaceAll("[/-]", "");

        return numero.substring(0, 3)
                + "-"
                + numero.substring(3, 10)
                + "-"
                + numero.substring(10);
    }

    // VALIDAR TELÉFONO DOMINICANO

    private boolean validarTelefonoDominicano(String telefono) {

        String numero = telefono.replaceAll("[/-]", "");

        if (!numero.matches("[0-9]{10}")) {
            return false;
        }

        String prefijo = numero.substring(0, 3);

        return prefijo.equals("809")
                || prefijo.equals("829")
                || prefijo.equals("849");
    }

    // FORMATEAR TELÉFONO

    private String formatearTelefono(String telefono) {

        String numero = telefono.replaceAll("[/-]", "");

        return numero.substring(0, 3)
                + "-"
                + numero.substring(3, 6)
                + "-"
                + numero.substring(6);
    }

    // VALIDAR NOMBRE Y APELLIDO

    private boolean validarNombre(String nombre) {

        return nombre.matches("[\\p{L}]+([ .'-][\\p{L}]+)*");
    }

    // VALIDAR CORREO

    private boolean validarCorreo(String correo) {

        if (correo.length() > 100) {
            return false;
        }

        return Pattern.matches(
                "^[A-Za-z0-9_%+.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                correo
        );
    }

    // VALIDAR CONTRASEÑA

    private boolean validarClave(String clave) {

        if (clave.length() < 8 || clave.length() > 64) {
            return false;
        }

        boolean tieneLetra = false;
        boolean tieneNumero = false;

        for (int i = 0; i < clave.length(); i++) {

            char caracter = clave.charAt(i);

            if (Character.isLetter(caracter)) {
                tieneLetra = true;
            }

            if (Character.isDigit(caracter)) {
                tieneNumero = true;
            }
        }

        return tieneLetra && tieneNumero;
    }

    // CARGAR TABLA

    private void cargarTabla() {

        DefaultTableModel modelo = new DefaultTableModel() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        modelo.addColumn("Código");
        modelo.addColumn("Cédula");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Correo");
        modelo.addColumn("Usuario");
        modelo.addColumn("Estado");
        modelo.addColumn("Fecha registro");

        ArrayList<Cliente> clientes = altice.obtenerClientes();

        SimpleDateFormat formatoFecha =
                new SimpleDateFormat("dd/MM/yyyy");

        for (Cliente cliente : clientes) {

            modelo.addRow(new Object[] {

                    cliente.getCodigo(),
                    cliente.getCedula(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getTelefono(),
                    cliente.getCorreo(),
                    cliente.getUsuario(),
                    cliente.isActivo() ? "Activo" : "Inactivo",
                    cliente.getFechaRegistro() != null
                            ? formatoFecha.format(cliente.getFechaRegistro())
                            : "Sin fecha"

            });
        }

        tablaClientes.setModel(modelo);

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
        txtUsuario.setText("");
        txtClave.setText("");

        txtCedula.requestFocus();
    }
}
