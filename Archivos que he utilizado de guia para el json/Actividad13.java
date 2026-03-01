package actividad02Ejercicios;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class Actividad13 {

    public static void main(String[] args) throws IOException {
        // Cargar el contenido del fichero JSON
        String content = new String(Files.readAllBytes(Paths.get("jugadores.json")));
        JSONObject jO = new JSONObject(content);
        JSONArray jugadoresArray = jO.getJSONArray("jugadores");

        // Contadores y variables para realizar las tareas solicitadas
   
        int numeroJugadores = jugadoresArray.length(); // Contar los jugadores en el jugadoresArray y almacenarlo en la variable numeroJugadores
        int golesTotales = 0;
        String nombreMasGoles = "";
        String equipoMasGoles = "";
        int maxGoles = -1;

        String nombreMenosAsistencias = "";
        String equipoMenosAsistencias = "";
        int minAsistencias = Integer.MAX_VALUE; // Para sacar el valor minimo en un conjunto de datos. Se predetermina el valor mas alto y cuando se quiera sacar el menor valor se hace una busqueda

        // Procesar cada jugador en el array
        for (int i = 0; i < jugadoresArray.length(); i++) {
            JSONObject jugador = jugadoresArray.getJSONObject(i);
            String nombre = jugador.getString("nombre");
            String equipo = jugador.getString("equipo");

            // Verificar si el jugador tiene estadísticas de goles y asistencias
            if (jugador.has("estadisticas")) {
                JSONObject estadisticas = jugador.getJSONObject("estadisticas");

                // Calcular el total de goles
                if (estadisticas.has("goles")) {
                    int goles = estadisticas.getInt("goles");
                    golesTotales += goles;

                    // Verificar si es el jugador con más goles
                    if (goles > maxGoles) {
                        maxGoles = goles;
                        nombreMasGoles = nombre;
                        equipoMasGoles = equipo;
                    }
                }

                // Calcular el jugador con menos asistencias
                if (estadisticas.has("asistencias")) {
                    int asistencias = estadisticas.getInt("asistencias");

                    // Verificar si es el jugador con menos asistencias
                    if (asistencias < minAsistencias) {
                        minAsistencias = asistencias;
                        nombreMenosAsistencias = nombre;
                        equipoMenosAsistencias = equipo;
                    }
                }
            }
        }

        // Crear la carpeta "Actividad13" si no existe
        File carpeta = new File("Actividad13");
        if (!carpeta.exists()) {
            carpeta.mkdirs(); // Crearmos la carpeta
        }

        // Crear y escribir en el archivo Fernandez.txt
        File archivoFernandez = new File(carpeta, "Fernandez.txt");
        try (FileWriter EscribirF = new FileWriter(archivoFernandez)) {
            EscribirF.write("Número de jugadores: " + numeroJugadores + "\n");
            EscribirF.write("Goles totales de todos los jugadores: " + golesTotales + "\n");
            EscribirF.write("Jugador con más goles: " + nombreMasGoles + " (" + equipoMasGoles + ") con " + maxGoles + " goles\n");
            EscribirF.write("Jugador con menos asistencias: " + nombreMenosAsistencias + " (" + equipoMenosAsistencias + ") con " + minAsistencias + " asistencias\n");

            System.out.println("Datos escritos en el archivo Fernandez.txt");
        } catch (IOException ex) {
            System.out.println("Ocurrió un error al escribir en el archivo Fernandez.txt.");
            
        }

        // Crear y escribir en el archivo Martinez.txt
        File archivoMartinez = new File(carpeta, "Martinez.txt");
        try (FileWriter EscribirM = new FileWriter(archivoMartinez)) {
            // Procesar y escribir información agrupada por equipo sin usar Map
            for (int i = 0; i < jugadoresArray.length(); i++) {
                JSONObject jugador = jugadoresArray.getJSONObject(i);
                String equipoActual = jugador.getString("equipo");

                // Verificar si ya hemos escrito este equipo en el archivo
                boolean equipoYaEscrito = false;
                for (int j = 0; j < i; j++) {
                    if (jugadoresArray.getJSONObject(j).getString("equipo").equals(equipoActual)) {
                        equipoYaEscrito = true;
                        break;
                    }
                }
                // Si el equipo ya está escrito, saltamos al siguiente jugador
                if (equipoYaEscrito) continue; 

                // Escribir el nombre del equipo y listar a sus jugadores
                EscribirM.write("Equipo: " + equipoActual + "\n");
                   // Contador de jugadores por equipo
                int contadorJugadores = 0; 
                EscribirM.write("Jugadores:\n");

                for (int k = 0; k < jugadoresArray.length(); k++) {
                    JSONObject jugadorEquipo = jugadoresArray.getJSONObject(k);
                    String equipo = jugadorEquipo.getString("equipo");

                    if (equipo.equals(equipoActual)) {
                        // Escribir el nombre del jugador en el equipo actual
                        EscribirM.write(" - " + jugadorEquipo.getString("nombre") + "\n");
                        contadorJugadores++;
                    }
                }

                // Escribir el total de jugadores en el equipo actual
                EscribirM.write("Total de jugadores: " + contadorJugadores + "\n\n");
            }

            System.out.println("Datos agrupados por equipo escritos en el archivo Martinez.txt");
        } catch (IOException e) {
            System.out.println("Ocurrió un error al escribir en el archivo Martinez.txt.");
            
        }
    }
}


