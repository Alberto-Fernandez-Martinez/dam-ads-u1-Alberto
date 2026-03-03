package modelo;

public class Pista {
    private final String idPista;
    private String deporte;
    private String descripcion;
    private boolean disponible;

    public Pista(String idPista, String deporte, String descripcion, boolean disponible) throws IdObligatorioException {
        if (idPista == null || idPista.isBlank()) {
            throw new IdObligatorioException("El id de la pista no puede ser vacio");
        }
        if (deporte == null || deporte.isBlank()) {
            throw new IdObligatorioException("El deporte no puede ser vacio");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IdObligatorioException("La descripcion no puede ser vacia");
        }

        this.idPista = idPista;
        this.deporte = deporte;
        this.descripcion = descripcion;
        this.disponible = disponible;
    }

    public String getIdPista() {
        return idPista;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        if (deporte == null || deporte.isBlank()) {
            throw new IdObligatorioException("El deporte no puede ser vacio");
        }
        this.deporte = deporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IdObligatorioException("La descripcion no puede ser vacia");
        }
        this.descripcion = descripcion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
