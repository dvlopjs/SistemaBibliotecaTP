package model;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private Connection connection;

    public PrestamoDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para registrar un nuevo préstamo
    public boolean registrarPrestamo(int idLibro, String estudiante, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        String query = "INSERT INTO Prestamo (id_libro, estudiante, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?)";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaPrestamoFormateada = fechaPrestamo.format(formatter);
        String fechaDevolucionFormateada = fechaDevolucion.format(formatter);

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idLibro);
            stmt.setString(2, estudiante);
            stmt.setString(3, fechaPrestamoFormateada);
            stmt.setString(4, fechaDevolucionFormateada);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener todos los préstamos activos
    public List<Prestamo> obtenerPrestamosActivos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String query = "SELECT * FROM Prestamo";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_libro"),
                        rs.getString("estudiante"),
                        rs.getString("fecha_prestamo"),
                        rs.getString("fecha_devolucion")
                );
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    public Prestamo obtenerPrestamoPorId(int idPrestamo) {
        String sql = "SELECT * FROM Prestamo WHERE id_prestamo = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPrestamo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                int id = resultSet.getInt("id_prestamo");
                int idLibro = resultSet.getInt("id_libro");
                String estudiante = resultSet.getString("estudiante");
                String fechaPrestamo = resultSet.getString("fecha_prestamo");
                String fechaDevolucion = resultSet.getString("fecha_devolucion");



                return new Prestamo(id, idLibro,estudiante, fechaPrestamo, fechaDevolucion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra el préstamo
    }

    // Método para finalizar un préstamo (devolver un libro)
    public boolean finalizarPrestamo(int idPrestamo) {
        String query = "DELETE FROM Prestamo WHERE id_prestamo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPrestamo); // Asegúrate de que esté en el índice 1
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
