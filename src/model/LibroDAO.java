package model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LibroDAO {
    private Connection connection;

    public LibroDAO(Connection connection) {
        this.connection = connection;

    }
    private static final Logger LOGGER = Logger.getLogger(LibroDAO.class.getName());


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

    public List<Libro> obtenerLibros(String nombreFiltro, String estadoFiltro) {
        String query = "SELECT * FROM Libro WHERE 1=1";
        if (nombreFiltro != null && !nombreFiltro.isEmpty()) {
            query += " AND LOWER(titulo) LIKE ?";
        }
        if (estadoFiltro != null && !estadoFiltro.equals("Todos")) {
            query += " AND estado = ?";
        }

        List<Libro> libros = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            int paramIndex = 1;
            if (nombreFiltro != null && !nombreFiltro.isEmpty()) {
                stmt.setString(paramIndex++, "%" + nombreFiltro.toLowerCase() + "%");
            }
            if (estadoFiltro != null && !estadoFiltro.equals("Todos")) {
                stmt.setString(paramIndex++, estadoFiltro.toLowerCase());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                libros.add(new Libro(
                        rs.getInt("id_libro"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("genero"),
                        rs.getInt("anio"),
                        rs.getString("estado")
                ));
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


}