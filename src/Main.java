import controller.BibliotecaController;
import model.LibroDAO;
import model.PrestamoDAO;
import view.BibliotecaView;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
            Connection conexion = DatabaseConnection.getConnection();
            LibroDAO libroDAO = new LibroDAO(conexion);
            PrestamoDAO prestamoDAO = new PrestamoDAO(conexion);
            BibliotecaController controller = new BibliotecaController(libroDAO, prestamoDAO);
            BibliotecaView view = new BibliotecaView(controller);
            view.setVisible(true);
    }
}