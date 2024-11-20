import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private static final String URL = "jdbc:sqlite:library.db";



//    static {
//        // Registrar shutdown hook cuando la clase se carga
//        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseConnection::closeConnection));
//    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Conectarse a la base de datos
                connection = DriverManager.getConnection(URL);
                System.out.println("Conexión a SQLite establecida.");
            } catch (SQLException e) {
                System.out.println("Error al conectar con la base de datos SQLite: " + e.getMessage());
            }
        }
        return connection;
    }


    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a SQLite cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión a la base de datos SQLite: " + e.getMessage());
        }
    }

    }
