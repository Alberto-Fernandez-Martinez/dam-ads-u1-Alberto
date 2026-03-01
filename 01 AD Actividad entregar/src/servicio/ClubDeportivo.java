package servicio;

import modelo.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servicio principal del club deportivo.
 * Gestiona socios, pistas, reservas y persistencia en JSON.
 */
public class ClubDeportivo {

    private ArrayList<Socio> socios;
    private ArrayList<Pista> pistas;
    private ArrayList<Reserva> reservas;

    public final String FICHERO_DATOS_SOCIOS = "src/datos/FICHERO_DATOS_SOCIOS.json";
    public final String FICHERO_DATOS_PISTA = "src/datos/FICHERO_DATOS_PISTA.json";
    public final String FICHERO_DATOS_RESERVA = "src/datos/FICHERO_DATOS_RESERVA.json";

    private boolean cambiosPendientes = false;

    /**
     * Inicializamos las colecciones internas y carga datos desde JSON.
     */
    public ClubDeportivo() {
        socios = new ArrayList<>();
        pistas = new ArrayList<>();
        reservas = new ArrayList<>();

        cargarSociosDesdeJson();
        cargarPistasDesdeJson();
        cargarReservasDesdeJson();

        cambiosPendientes = false;
    }

    /**
     * Devuelve la lista actual de socios.
     * @return lista de socios en memoria
     */
    public ArrayList<Socio> getSocios() {
        return socios;
    }

    /**
     * Devuelve la lista actual de pistas.
     * @return lista de pistas en memoria
     */
    public ArrayList<Pista> getPistas() {
        return pistas;
    }

    /**
     * Devuelve la lista actual de reservas.
     * @return lista de reservas en memoria
     */
    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    /**
     * Indica si existen cambios pendientes de guardar.
     * @return true si hay cambios sin persistir
     */
    public boolean hayCambiosPendientes() {
        return cambiosPendientes;
    }

    /**
     * Marca que el estado interno fue modificado.
     */
    private void marcarCambios() {
        cambiosPendientes = true;
    }

    /**
     * Guarda socios, pistas y reservas en JSON.
     * @throws RuntimeException si ocurre un error de escritura
     */
    public void guardarDatos() {
        guardarSociosEnJson();
        guardarPistasEnJson();
        guardarReservasEnJson();
        cambiosPendientes = false;
    }


    //////////////////////////////////////////////////// SOCIOS /////////////////////////////////////////////////////////


    /**
     * Carga socios desde JSON.
     * @throws RuntimeException si falla la lectura o el formato
     */
    private void cargarSociosDesdeJson() {
        File f = new File(FICHERO_DATOS_SOCIOS);
        if (!f.exists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(FICHERO_DATOS_SOCIOS))).trim();
            if (content.isEmpty()) return;

            JSONObject root = new JSONObject(content);
            if (!root.has("Socios")) return;

            JSONArray arr = root.getJSONArray("Socios");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                socios.add(new Socio(
                        o.getString("idSocio"),
                        o.getString("dni"),
                        o.getString("nombre"),
                        o.getString("apellidos"),
                        o.getString("telefono"),
                        o.getString("email")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo socios JSON: " + e.getMessage());
        }
    }

    /**
     * Da de alta un socio en el club.
     * @param socio socio a registrar
     * @return true si se registra correctamente
     * @throws RuntimeException si el id es nulo, vacio o ya existe
     */
    public boolean altaSocio(Socio socio) {
        if (socio == null) throw new RuntimeException("Socio null");
        if (socio.getIdSocio() == null || socio.getIdSocio().isBlank()) {
            throw new RuntimeException("idSocio obligatorio");
        }

        for (Socio s : socios) {
            if (s.getIdSocio().equals(socio.getIdSocio())) {
                throw new RuntimeException("Ya existe un socio con id: " + socio.getIdSocio());
            }
        }

        socios.add(socio);
        marcarCambios();
        return true;
    }

    /**
     * Da de baja un socio si no tiene reservas asociadas.
     * @param idSocio identificador del socio
     * @throws RuntimeException si el id es invalido, el socio no existe o tiene reservas asociadas
     */
    public void bajaSocio(String idSocio) {
        if (idSocio == null || idSocio.isBlank()) throw new RuntimeException("idSocio obligatorio");

        for (Reserva r : reservas) {
            if (r.getIdSocio().equals(idSocio)) {
                throw new RuntimeException("No se puede dar de baja: el socio tiene reservas asociadas");
            }
        }

        Socio encontrado = null;
        for (Socio s : socios) {
            if (s.getIdSocio().equals(idSocio)) {
                encontrado = s;
                break;
            }
        }
        if (encontrado == null) throw new RuntimeException("Socio no encontrado: " + idSocio);

        socios.remove(encontrado);
        marcarCambios();
    }

    /**
     * Guarda la lista de socios en JSON.
     * @throws RuntimeException si ocurre un error de escritura
     */
    private void guardarSociosEnJson() {
        File f = new File(FICHERO_DATOS_SOCIOS);
        File parent = f.getParentFile();
        if (parent != null) parent.mkdirs();

        StringBuilder jb = new StringBuilder();
        jb.append("{\n  \"Socios\": [\n");

        for (int i = 0; i < socios.size(); i++) {
            Socio socio = socios.get(i);

            jb.append("    {\n");
            jb.append("      \"idSocio\": \"").append(socio.getIdSocio()).append("\",\n");
            jb.append("      \"dni\": \"").append(socio.getDni()).append("\",\n");
            jb.append("      \"nombre\": \"").append(socio.getNombre()).append("\",\n");
            jb.append("      \"apellidos\": \"").append(socio.getApellidos()).append("\",\n");
            jb.append("      \"telefono\": \"").append(socio.getTelefono()).append("\",\n");
            jb.append("      \"email\": \"").append(socio.getEmail()).append("\"\n");
            jb.append("    }");

            if (i < socios.size() - 1) jb.append(",");
            jb.append("\n");
        }

        jb.append("  ]\n}");

        try (FileWriter w = new FileWriter(f)) {
            w.write(jb.toString());
        } catch (IOException ex) {
            throw new RuntimeException("Error guardando socios: " + ex.getMessage());
        }
    }

    //////////////////////////////////////////////////// Pistas /////////////////////////////////////////////////////////

    /**
     * Carga pistas desde JSON.
     * @throws RuntimeException si falla la lectura o el formato
     */
    private void cargarPistasDesdeJson() {
        File f = new File(FICHERO_DATOS_PISTA);
        if (!f.exists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(FICHERO_DATOS_PISTA))).trim();
            if (content.isEmpty()) return;

            JSONObject root = new JSONObject(content);
            if (!root.has("Pistas")) return;

            JSONArray arr = root.getJSONArray("Pistas");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                pistas.add(new Pista(
                        o.getString("idPista"),
                        o.getString("deporte"),
                        o.getString("descripcion"),
                        o.getBoolean("disponible")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo pistas JSON: " + e.getMessage());
        }
    }

    /**
     * Da de alta una pista en el club.
     * @param pista pista a registrar
     * @throws RuntimeException si la pista es nula, el id es invalido o ya existe
     */
    public void altaPista(Pista pista) {
        if (pista == null) throw new RuntimeException("Pista null");
        if (pista.getIdPista() == null || pista.getIdPista().isBlank()) {
            throw new RuntimeException("idPista obligatorio");
        }

        for (Pista p : pistas) {
            if (p.getIdPista().equals(pista.getIdPista())) {
                throw new RuntimeException("Ya existe una pista con id: " + pista.getIdPista());
            }
        }

        pistas.add(pista);
        marcarCambios();
    }

    /**
     * Cambia la disponibilidad de una pista existente.
     * @param idPista identificador de la pista
     * @param disponible nuevo estado de disponibilidad
     * @throws RuntimeException si el id es invalido o la pista no existe
     */
    public void cambiarDisponibilidadPista(String idPista, boolean disponible) {
        if (idPista == null || idPista.isBlank()) throw new RuntimeException("idPista obligatorio");

        Pista encontrada = null;
        for (Pista p : pistas) {
            if (p.getIdPista().equals(idPista)) {
                encontrada = p;
                break;
            }
        }
        if (encontrada == null) throw new RuntimeException("Pista no encontrada: " + idPista);

        encontrada.setDisponible(disponible);
        marcarCambios();
    }

    /**
     * Guarda la lista de pistas en JSON.
     * @throws RuntimeException si ocurre un error de escritura
     */
    private void guardarPistasEnJson() {
        File f = new File(FICHERO_DATOS_PISTA);
        File parent = f.getParentFile();
        if (parent != null) parent.mkdirs();

        StringBuilder jb = new StringBuilder();
        jb.append("{\n  \"Pistas\": [\n");

        for (int i = 0; i < pistas.size(); i++) {
            Pista pista = pistas.get(i);

            jb.append("    {\n");
            jb.append("      \"idPista\": \"").append(pista.getIdPista()).append("\",\n");
            jb.append("      \"deporte\": \"").append(pista.getDeporte()).append("\",\n");
            jb.append("      \"descripcion\": \"").append(pista.getDescripcion()).append("\",\n");
            jb.append("      \"disponible\": ").append(pista.isDisponible()).append("\n");
            jb.append("    }");

            if (i < pistas.size() - 1) jb.append(",");
            jb.append("\n");
        }

        jb.append("  ]\n}");

        try (FileWriter w = new FileWriter(f)) {
            w.write(jb.toString());
        } catch (IOException ex) {
            throw new RuntimeException("Error guardando pistas: " + ex.getMessage());
        }
    }

    //////////////////////////////////////////////////// Reservas /////////////////////////////////////////////////////////

    /**
     * Carga reservas desde JSON.
     * @throws RuntimeException si falla la lectura o el formato
     */
    private void cargarReservasDesdeJson() {
        File f = new File(FICHERO_DATOS_RESERVA);
        if (!f.exists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(FICHERO_DATOS_RESERVA))).trim();
            if (content.isEmpty()) return;

            JSONObject root = new JSONObject(content);
            if (!root.has("Reservas")) return;

            JSONArray arr = root.getJSONArray("Reservas");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                reservas.add(new Reserva(
                        o.getString("idReserva"),
                        o.getString("idSocio"),
                        o.getString("idPista"),
                        LocalDate.parse(o.getString("fecha")),
                        LocalTime.parse(o.getString("horaInicio")),
                        o.getInt("duracionMin"),
                        o.getDouble("precio")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo reservas JSON: " + e.getMessage());
        }
    }

    /**
     * Creamos una nueva reserva en el club.
     * @param r reserva a crear
     * @return true si la reserva se registra correctamente
     * @throws RuntimeException si la reserva es invalida, hay ids repetidos, la pista no existe,
     * la pista no esta operativa.
     */
    public boolean crearReserva(Reserva r) {
        if (r == null) throw new RuntimeException("Reserva null");

        for (Reserva x : reservas) {
            if (x.getIdReserva().equals(r.getIdReserva())) {
                throw new RuntimeException("Ya existe una reserva con id: " + r.getIdReserva());
            }
        }

        boolean socioExiste = false;
        for (Socio s : socios) {
            if (s.getIdSocio().equals(r.getIdSocio())) {
                socioExiste = true;
                break;
            }
        }
        if (!socioExiste) throw new RuntimeException("El socio no existe: " + r.getIdSocio());

        Pista pista = null;
        for (Pista p : pistas) {
            if (p.getIdPista().equals(r.getIdPista())) {
                pista = p;
                break;
            }
        }
        if (pista == null) throw new RuntimeException("La pista no existe: " + r.getIdPista());
        if (!pista.isDisponible()) throw new RuntimeException("La pista no esta operativa (no disponible)");

        LocalDateTime inicioNueva = LocalDateTime.of(r.getFecha(), r.getHoraInicio());
        LocalDateTime finNueva = inicioNueva.plusMinutes(r.getDuracionMin());
        for (Reserva existente : reservas) {
            if (!existente.getIdPista().equals(r.getIdPista())) continue;

            LocalDateTime inicioExistente = LocalDateTime.of(existente.getFecha(), existente.getHoraInicio());
            LocalDateTime finExistente = inicioExistente.plusMinutes(existente.getDuracionMin());
            boolean seSolapan = inicioExistente.isBefore(finNueva) && inicioNueva.isBefore(finExistente);
            if (seSolapan) {
                throw new RuntimeException("La pista ya tiene una reserva que se solapa en ese horario");
            }
        }

        reservas.add(r);
        marcarCambios();
        return true;
    }

    /**
     * Cancela una reserva existente por su identificador.
     * @param idReserva identificador de la reserva
     * @throws RuntimeException si el id es invalido o la reserva no existe
     */
    public void cancelarReserva(String idReserva) {
        if (idReserva == null || idReserva.isBlank()) throw new RuntimeException("idReserva obligatorio");

        Reserva encontrada = null;
        for (Reserva r : reservas) {
            if (r.getIdReserva().equals(idReserva)) {
                encontrada = r;
                break;
            }
        }
        if (encontrada == null) throw new RuntimeException("Reserva no encontrada: " + idReserva);

        reservas.remove(encontrada);
        marcarCambios();
    }

    /**
     * Guarda la lista de reservas en JSON.
     * @throws RuntimeException si ocurre un error de escritura
     */
    private void guardarReservasEnJson() {
        File f = new File(FICHERO_DATOS_RESERVA);
        File parent = f.getParentFile();
        if (parent != null) parent.mkdirs();

        StringBuilder jb = new StringBuilder();
        jb.append("{\n  \"Reservas\": [\n");

        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);

            jb.append("    {\n");
            jb.append("      \"idReserva\": \"").append(r.getIdReserva()).append("\",\n");
            jb.append("      \"idSocio\": \"").append(r.getIdSocio()).append("\",\n");
            jb.append("      \"idPista\": \"").append(r.getIdPista()).append("\",\n");
            jb.append("      \"fecha\": \"").append(r.getFecha()).append("\",\n");
            jb.append("      \"horaInicio\": \"").append(r.getHoraInicio()).append("\",\n");
            jb.append("      \"duracionMin\": ").append(r.getDuracionMin()).append(",\n");
            jb.append("      \"precio\": ").append(r.getPrecio()).append("\n");
            jb.append("    }");

            if (i < reservas.size() - 1) jb.append(",");
            jb.append("\n");
        }

        jb.append("  ]\n}");

        try (FileWriter w = new FileWriter(f)) {
            w.write(jb.toString());
        } catch (IOException ex) {
            throw new RuntimeException("Error guardando reservas: " + ex.getMessage());
        }
    }
}
