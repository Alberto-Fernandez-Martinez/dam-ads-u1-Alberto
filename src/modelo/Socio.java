package modelo;

public class Socio {
    private final String idSocio;
    private String dni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String email;

    public Socio(String idSocio, String dni, String nombre, String apellidos, String telefono, String email) throws IdObligatorioException {
        if (idSocio == null || idSocio.isBlank()) {
            throw new IdObligatorioException("El id del socio no puede ser vacio");
        }
        if (dni == null || dni.isBlank()) {
            throw new IdObligatorioException("El DNI no puede ser vacio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IdObligatorioException("El nombre no puede ser vacio");
        }
        if (apellidos == null || apellidos.isBlank()) {
            throw new IdObligatorioException("Los apellidos no pueden ser vacios");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new IdObligatorioException("El telefono no puede ser vacio");
        }
        if (!telefono.matches("\\d+")) {
            throw new IdObligatorioException("El telefono solo puede contener numeros");
        }
        if (email == null || email.isBlank()) {
            throw new IdObligatorioException("El email no puede ser vacio");
        }

        this.idSocio = idSocio;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
    }

    public String getIdSocio() { return idSocio; }

    public String getDni() { return dni; }
    public void setDni(String dni) {
        if (dni == null || dni.isBlank()) throw new IdObligatorioException("El DNI no puede ser vacio");
        this.dni = dni;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IdObligatorioException("El nombre no puede ser vacio");
        this.nombre = nombre;
    }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) {
        if (apellidos == null || apellidos.isBlank()) throw new IdObligatorioException("Los apellidos no pueden ser vacios");
        this.apellidos = apellidos;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) throw new IdObligatorioException("El telefono no puede ser vacio");
        if (!telefono.matches("\\d+")) throw new IdObligatorioException("El telefono solo puede contener numeros");
        this.telefono = telefono;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.isBlank()) throw new IdObligatorioException("El email no puede ser vacio");
        this.email = email;
    }
}
