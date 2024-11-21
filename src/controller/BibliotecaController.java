package controller;
import model.Libro;
import model.LibroDAO;
import model.Prestamo;
import model.PrestamoDAO;
import java.time.LocalDate;
import java.util.List;

public class BibliotecaController {
    private LibroDAO libroDAO;
    private PrestamoDAO prestamoDAO;

    public BibliotecaController(LibroDAO libroDAO, PrestamoDAO prestamoDAO) {
        this.libroDAO = libroDAO;
        this.prestamoDAO = prestamoDAO;
    }

    // Método para agregar un nuevo libro
    public boolean agregarLibro(String titulo, String autor, String genero, int anio) {
        Libro nuevoLibro = new Libro(titulo, autor, genero, anio, "disponible");
        return libroDAO.agregarLibro(nuevoLibro);
    }

    // Método para obtener todos los libros
    public List<Libro> obtenerLibros(String filtroNombre, String filtroEstadoSeleccionado) {
        return libroDAO.obtenerLibros(filtroNombre, filtroEstadoSeleccionado);
    }

    public List<Prestamo> obtenerPrestamos(){
        return prestamoDAO.obtenerPrestamosActivos();
    }

    // Método para registrar un préstamo
    public boolean registrarPrestamo(int idLibro,String estudiante, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        // Aquí se podría verificar si el libro está disponible antes de registrar el préstamo
        Libro libro = libroDAO.obtenerLibroPorId(idLibro);
        if (libro != null && libro.getEstado().equals("disponible")) {
            if (prestamoDAO.registrarPrestamo(idLibro, estudiante, fechaPrestamo, fechaDevolucion)) {
                // Actualizamos el estado del libro a "prestado"
                return libroDAO.actualizarEstadoLibro(idLibro, "prestado");
            }
        }
        return false;
    }

    // Método para finalizar un préstamo
    public boolean finalizarPrestamo(int idPrestamo) {
        Prestamo prestamo = prestamoDAO.obtenerPrestamoPorId(idPrestamo);
        if (prestamo != null) {
            if (prestamoDAO.finalizarPrestamo(idPrestamo)) {
                // Cambiamos el estado del libro a "disponible"
                return libroDAO.actualizarEstadoLibro(prestamo.getIdLibro(), "disponible");
            }
        }
        return false;
    }

    public Libro getLibroId(int idLibro){
        return libroDAO.obtenerLibroPorId(idLibro);
    }


}
