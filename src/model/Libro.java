package  model;
public class Libro {
    private int idLibro;
    private String titulo;
    private String autor;
    private String genero;
    private int anio;
    private String estado;

    // Constructor para inicializar todos los campos
    public Libro(int idLibro, String titulo, String autor, String genero, int anio, String estado) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anio = anio;
        this.estado = estado;
    }

    // Constructor sin `idLibro` (para agregar un nuevo libro, ya que `id_libro` es autoincremental)
    public Libro(String titulo, String autor, String genero, int anio, String estado) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.anio = anio;
        this.estado = estado;
    }

    // Getters y setters
    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }



    @Override
    public String toString() {
        return "Libro{" +
                "idLibro=" + idLibro +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", genero='" + genero + '\'' +
                ", anio=" + anio +
                ", estado='" + estado + '\'' +
                '}';
    }
}