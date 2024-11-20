package model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    private Connection connection;

    public LibroDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para agregar un nuevo libro
    public boolean agregarLibro(Libro libro) {
        String sql = "INSERT INTO Libro (titulo, autor, genero, anio) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, libro.getTitulo());
                stmt.setString(2, libro.getAutor());
                stmt.setString(3, libro.getGenero());
                stmt.setInt(4, libro.getAnio());

                int resultado = stmt.executeUpdate();
                connection.commit();
                return resultado > 0;
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Error al agregar libro: " + e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error en la transacción: " + e.getMessage());
            return false;
        }
    }

    // Método para obtener todos los libros
    public List<Libro> obtenerLibros() {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT * FROM Libro";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("id_libro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getInt("anio"),
                        rs.getString("estado"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public Libro obtenerLibroPorId(int idLibro) {
        String sql = "SELECT * FROM Libro WHERE id_libro = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idLibro);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Crear un objeto Libro con los datos obtenidos
                int id = resultSet.getInt("id_libro");
                String titulo = resultSet.getString("titulo");
                String autor = resultSet.getString("autor");
                String genero = resultSet.getString("genero");
                int anio = resultSet.getInt("anio");
                String estado = resultSet.getString("estado");

                return new Libro(id, titulo, autor, genero, anio, estado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra el libro
    }

    // Método para actualizar el estado de un libro (prestado o disponible)
    public boolean actualizarEstadoLibro(int idLibro, String nuevoEstado) {
        String query = "UPDATE Libro SET estado = ? WHERE id_libro = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idLibro);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar libros por estado (disponible o prestado)
    public List<Libro> obtenerLibrosPorEstado(String estado) {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT * FROM Libro WHERE estado = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, estado);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getInt("anio"),
                        rs.getString("estado")
                );
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }
}