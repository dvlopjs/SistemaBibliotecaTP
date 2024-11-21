package view;

import controller.BibliotecaController;
import model.Libro;
import model.Prestamo;
import view.utils.ColorEstadoCellRender;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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
    private DefaultTableModel modeloTablaLibros;
    private JComboBox<String> filtroEstado;
    private JButton actualizarTablaButton;
    private JTextField filtroNombreField;

    private void configurarRenderizadoresTabla() {

        int columnaEstadoIndex = 5;
        tablaLibros.getColumnModel().getColumn(columnaEstadoIndex).setCellRenderer(new ColorEstadoCellRender());
    }



    // Componentes para préstamos
    private JTextField estudianteField;
    private JSpinner fechaPrestamoSpinner;
    private JSpinner fechaDevolucionSpinner;
    private JButton registrarPrestamoButton;
    private JButton finalizarPrestamoButton;
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTablaPrestamos;

    private static final String[] COLUMNAS_LIBROS = {"ID", "Título", "Autor", "Género", "Año", "Estado"};
    private static final String[] COLUMNAS_PRESTAMOS = {"ID Préstamo", "Estudiante", "Título", "Fecha Préstamo", "Fecha Devolución"};


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
        GridBagConstraints gbc = createGridBagConstraints();

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
        tablePanel.setBorder(BorderFactory.createTitledBorder("Libros Registr ados"));

        modeloTablaLibros = new DefaultTableModel(COLUMNAS_LIBROS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLibros = new JTable(modeloTablaLibros);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);

        configurarRenderizadoresTabla();

        // Panel de filtros
        JPanel filtrosPanel = createFiltrosPanel();

        tablePanel.add(filtrosPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createFiltrosPanel() {
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        filtroNombreField = new JTextField(15);

        JButton buscarNombreButton = new JButton("Buscar");
        buscarNombreButton.addActionListener(e -> actualizarTablaLibros());


        filtroEstado = new JComboBox<>(new String[]{"Todos", "Disponible", "Prestado"});
        filtrosPanel.add(new JLabel("Buscar por nombre:"));
        filtrosPanel.add(filtroNombreField);
        filtrosPanel.add(buscarNombreButton);

        filtrosPanel.add(new JLabel("Filtrar por estado:"));
        filtrosPanel.add(filtroEstado);

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
        GridBagConstraints gbc = createGridBagConstraints();

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
        fechaPrestamoSpinner = new JSpinner(new SpinnerDateModel());
        fechaDevolucionSpinner = new JSpinner(new SpinnerDateModel());

        JSpinner.DateEditor editorPrestamo = new JSpinner.DateEditor(fechaPrestamoSpinner, "dd/MM/yyyy");
        JSpinner.DateEditor editorDevolucion = new JSpinner.DateEditor(fechaDevolucionSpinner, "dd/MM/yyyy");
        fechaPrestamoSpinner.setEditor(editorPrestamo);
        fechaDevolucionSpinner.setEditor(editorDevolucion);
    }

    private JPanel createTablaPrestamos() {
        JPanel prestamosPanel = new JPanel(new BorderLayout());
        prestamosPanel.setBorder(BorderFactory.createTitledBorder("Préstamos Activos"));

        modeloTablaPrestamos = new DefaultTableModel(COLUMNAS_PRESTAMOS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPrestamos = new JTable(modeloTablaPrestamos);
        JScrollPane scrollPane = new JScrollPane(tablaPrestamos);

        finalizarPrestamoButton = new JButton("Finalizar Préstamo Seleccionado");
        finalizarPrestamoButton.addActionListener(e -> finalizarPrestamo());

        prestamosPanel.add(scrollPane, BorderLayout.CENTER);
        prestamosPanel.add(finalizarPrestamoButton, BorderLayout.SOUTH);

        return prestamosPanel;
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    private void addFormField(JPanel panel, String label, JComponent component, GridBagConstraints gbc, int gridy) {
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

        if (estudiante.isEmpty()) {
            mostrarError("El nombre del estudiante no puede estar vacío", "Error");
            return;
        }

        Libro libroSelected = controller.getLibroId(idLibro);
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
            limpiarFormularioPrestamo();
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
        modeloTablaLibros.setRowCount(0);
        String filtroEstadoSeleccionado = (String) filtroEstado.getSelectedItem();
        String filtroNombre = filtroNombreField.getText().trim().toLowerCase();

        List<Libro> libros = controller.obtenerLibros(filtroNombre, filtroEstadoSeleccionado );
        for (Libro libro : libros) {
            boolean coincideEstado = filtroEstadoSeleccionado.equals("Todos") ||
                    (filtroEstadoSeleccionado.equals("Disponible") && libro.getEstado().equalsIgnoreCase("disponible")) ||
                    (filtroEstadoSeleccionado.equals("Prestado") && libro.getEstado().equalsIgnoreCase("prestado"));

            boolean coincideNombre = filtroNombre.isEmpty() || libro.getTitulo().toLowerCase().contains(filtroNombre);

            if (coincideEstado && coincideNombre) {
                modeloTablaLibros.addRow(new Object[]{
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
        modeloTablaPrestamos.setRowCount(0);
        List<Prestamo> prestamos = controller.obtenerPrestamos();
        for (Prestamo prestamo : prestamos) {
            String nombreLibro = controller.getLibroId(prestamo.getIdLibro()).getTitulo();
            modeloTablaPrestamos.addRow(new Object[]{
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
        estudianteField.setText("");
    }

    private void limpiarFormularioPrestamo(){
        fechaPrestamoSpinner.setValue(new Date());
        fechaDevolucionSpinner.setValue(new Date());

        estudianteField.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void mostrarError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }
}