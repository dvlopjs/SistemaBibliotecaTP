package view;

import controller.BibliotecaController;
import model.Libro;
import model.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BibliotecaView extends JFrame {
    private final BibliotecaController controller;

    // Componentes para agregar libros
    private JTextField tituloField;
    private JTextField autorField;
    private JTextField generoField;
    private JTextField anioField;
    private JButton agregarLibroButton;

    // Componentes para la tabla de libros
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Título", "Autor", "Género", "Año", "Estado"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JComboBox<String> filtroEstado;
    private JButton actualizarTablaButton;

    // Componentes para préstamos
    private JTextField estudianteField;
    private JSpinner fechaPrestamoSpinner;
    private JSpinner fechaDevolucionSpinner;
    private JButton registrarPrestamoButton;
    private JButton finalizarPrestamoButton;
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTablaPrestamo = new DefaultTableModel(
            new Object[]{"ID Préstamo", "Estudiante", "Título", "Fecha Préstamo", "Fecha Devolución"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public BibliotecaView(BibliotecaController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        configurarVentana();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Gestión de Libros", createLibrosPanel());
        tabbedPane.addTab("Préstamos", createPrestamosPanel());
        add(tabbedPane);

        // Inicializar las tablas al arrancar
        actualizarTablaLibros();
        actualizarTablaPrestamos();
    }

    private void configurarVentana() {
        setTitle("Sistema de Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createLibrosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createFormularioLibros(), BorderLayout.NORTH);
        panel.add(createTablaLibros(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormularioLibros() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Agregar Nuevo Libro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Inicialización de campos
        tituloField = new JTextField(20);
        autorField = new JTextField(20);
        generoField = new JTextField(20);
        anioField = new JTextField(6);
        agregarLibroButton = new JButton("Agregar Libro");
        agregarLibroButton.addActionListener(e -> agregarLibro());

        // Agregar componentes
        addFormField(formPanel, "Título:", tituloField, gbc, 0);
        addFormField(formPanel, "Autor:", autorField, gbc, 1);
        addFormField(formPanel, "Género:", generoField, gbc, 2);
        addFormField(formPanel, "Año:", anioField, gbc, 3);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(agregarLibroButton, gbc);

        return formPanel;
    }

    private JPanel createTablaLibros() {
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Libros Registrados"));

        tablaLibros = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);

        // Panel de filtros
        JPanel filtrosPanel = createFiltrosPanel();

        tablePanel.add(filtrosPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createFiltrosPanel() {
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        filtroEstado = new JComboBox<>(new String[]{"Todos", "Disponible", "Prestado"});
        actualizarTablaButton = new JButton("Actualizar");
        actualizarTablaButton.addActionListener(e -> actualizarTablaLibros());

        filtrosPanel.add(new JLabel("Filtrar por estado:"));
        filtrosPanel.add(filtroEstado);
        filtrosPanel.add(actualizarTablaButton);

        return filtrosPanel;
    }

    private JPanel createPrestamosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createFormularioPrestamos(), BorderLayout.NORTH);
        panel.add(createTablaPrestamos(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormularioPrestamos() {

        JPanel prestamoPanel = new JPanel(new GridBagLayout());
        prestamoPanel.setBorder(BorderFactory.createTitledBorder("Registrar Préstamo"));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);

        // Configuración de spinners
        configurarSpinnersFecha();


        estudianteField = new JTextField(20);

        // Agregar componentes al panel
        addFormField(prestamoPanel, "Estudiante:", estudianteField, gbc, 0);
        addFormField(prestamoPanel, "Fecha Préstamo:", fechaPrestamoSpinner, gbc, 1);
        addFormField(prestamoPanel, "Fecha Devolución:", fechaDevolucionSpinner, gbc, 2);

        registrarPrestamoButton = new JButton("Registrar Préstamo");
        registrarPrestamoButton.addActionListener(e -> registrarPrestamo());
        gbc.gridx = 1;
        gbc.gridy = 3;
        prestamoPanel.add(registrarPrestamoButton, gbc);

        return prestamoPanel;
    }

    private void configurarSpinnersFecha() {
        SpinnerDateModel modeloPrestamo = new SpinnerDateModel();
        SpinnerDateModel modeloDevolucion = new SpinnerDateModel();
        fechaPrestamoSpinner = new JSpinner(modeloPrestamo);
        fechaDevolucionSpinner = new JSpinner(modeloDevolucion);

        JSpinner.DateEditor editorPrestamo = new JSpinner.DateEditor(fechaPrestamoSpinner, "dd/MM/yyyy");
        JSpinner.DateEditor editorDevolucion = new JSpinner.DateEditor(fechaDevolucionSpinner, "dd/MM/yyyy");
        fechaPrestamoSpinner.setEditor(editorPrestamo);
        fechaDevolucionSpinner.setEditor(editorDevolucion);
    }

    private JPanel createTablaPrestamos() {
        JPanel prestamosPanel = new JPanel(new BorderLayout());
        prestamosPanel.setBorder(BorderFactory.createTitledBorder("Préstamos Activos"));

        tablaPrestamos = new JTable(modeloTablaPrestamo);
        JScrollPane scrollPane = new JScrollPane(tablaPrestamos);

        finalizarPrestamoButton = new JButton("Finalizar Préstamo Seleccionado");
        finalizarPrestamoButton.addActionListener(e -> finalizarPrestamo());

        prestamosPanel.add(scrollPane, BorderLayout.CENTER);
        prestamosPanel.add(finalizarPrestamoButton, BorderLayout.SOUTH);

        return prestamosPanel;
    }

    private void addFormField(JPanel panel, String label, JComponent component,
                              GridBagConstraints gbc, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }

    private void agregarLibro() {
        try {
            String titulo = tituloField.getText().trim();
            String autor = autorField.getText().trim();
            String genero = generoField.getText().trim();
            int anio = Integer.parseInt(anioField.getText().trim());

            if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty()) {
                mostrarError("Por favor, complete todos los campos", "Campos Incompletos");
                return;
            }

            if (controller.agregarLibro(titulo, autor, genero, anio)) {
                mostrarMensaje("Libro agregado correctamente");
                limpiarCampos();
                actualizarTablaLibros();
            } else {
                mostrarError("Error al agregar el libro", "Error");
            }
        } catch (NumberFormatException e) {
            mostrarError("El año debe ser un número válido", "Error de Formato");
        }
    }

    private void registrarPrestamo() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione un libro para prestar", "Selección Requerida");
            return;
        }

        int idLibro = (int) tablaLibros.getValueAt(filaSeleccionada, 0);

        Date fechaPrestamoDate = (Date) fechaPrestamoSpinner.getValue();
        Date fechaDevolucionDate = (Date) fechaDevolucionSpinner.getValue();
        String estudiante = estudianteField.getText().trim();


        LocalDate fechaPrestamo = fechaPrestamoDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate fechaDevolucion = fechaDevolucionDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Libro libroSelected = controller.getLibroId(idLibro);

        if(estudiante.isEmpty()){
            mostrarError("El nombre del estudiante no puede estar vacío", "Error");
            return;
        }

        if (libroSelected.getEstado().equals("prestado")) {
            mostrarError("El libro ya se encuentra prestado.", "Error");
            return;
        }

        if (!fechaDevolucion.isAfter(fechaPrestamo)) {
             mostrarError("La fecha de devolución tiene que ser mayor a la fecha de préstamo.", "Error");
             return;
        }



        if (controller.registrarPrestamo(idLibro, estudiante, fechaPrestamo, fechaDevolucion)) {
            mostrarMensaje("Préstamo registrado correctamente");
            actualizarTablaLibros();
            actualizarTablaPrestamos();
        } else {
            mostrarError("Error al registrar el préstamo", "Error");
        }
    }

    private void finalizarPrestamo() {
        int filaSeleccionada = tablaPrestamos.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione un préstamo para finalizar", "Selección Requerida");
            return;
        }

        int idPrestamo = (int) tablaPrestamos.getValueAt(filaSeleccionada, 0);
        if (controller.finalizarPrestamo(idPrestamo)) {
            mostrarMensaje("Préstamo finalizado correctamente");
            actualizarTablaPrestamos();
            actualizarTablaLibros();
        } else {
            mostrarError("Error al finalizar el préstamo", "Error");
        }
    }

    private void actualizarTablaLibros() {
        modeloTabla.setRowCount(0);
        String filtro = (String) filtroEstado.getSelectedItem();

        List<Libro> libros = controller.obtenerLibros();
        for (Libro libro : libros) {
            if (filtro.equals("Todos") ||
                    (filtro.equals("Disponible") && libro.getEstado().equals("disponible")) ||
                    (filtro.equals("Prestado") && libro.getEstado().equals("prestado"))) {
                modeloTabla.addRow(new Object[]{
                        libro.getIdLibro(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getGenero(),
                        libro.getAnio(),
                        libro.getEstado()
                });
            }
        }
    }

    private void actualizarTablaPrestamos() {
        modeloTablaPrestamo.setRowCount(0);
        List<Prestamo> prestamos = controller.obtenerPrestamos();
        List<Libro> libros = controller.obtenerLibros(); // Método para obtener todos los libros


        Map<Integer, String> mapaLibros = new HashMap<>();
        for (Libro libro : libros) {
            mapaLibros.put(libro.getIdLibro(), libro.getTitulo());
        }

        for (Prestamo prestamo : prestamos) {
            String nombreLibro = mapaLibros.get(prestamo.getIdLibro());

            modeloTablaPrestamo.addRow(new Object[]{
                    prestamo.getIdPrestamo(),
                    prestamo.getEstudiante(),
                    nombreLibro,
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion()
            });
        }
    }

    private void limpiarCampos() {
        tituloField.setText("");
        autorField.setText("");
        generoField.setText("");
        anioField.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void mostrarError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }
}