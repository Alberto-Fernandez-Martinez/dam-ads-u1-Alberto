package actividad02Ejercicios;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Actividad11 {
// Los atributos de la clase Actividad11
    private String NRE;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String fechaNacimiento;
    private String beca;
    
    // Creamos el costructor de la clase Actividad11

    public Actividad11(String NRE, String nombre, String apellidos, String telefono, String fechaNacimiento, String beca) {
        this.NRE = NRE;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.beca = beca;
    }

    // Loa getter de los atributos de la actividad11
    public String getNRE() {
        return NRE;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getBeca() {
        return beca;
    }

    //Metodo para crear un archivo Json desde datos insertados por un ArrayList pasandole el nombre del fichero
    public static void crearJson(ArrayList<Actividad11> alumnosListados) {
        //Creamos la variable con la Actividad11.json y la convertimos en fichero con el File
        String nombreFichero = "Actividad11.json";
        File f = new File(nombreFichero);
        StringBuilder jb = new StringBuilder();
        //Elemento raiz de nuestro archivo Json
        jb.append("{\n  \"Alumnos\": [\n");

        //Creamos un for para ir insertando todos los datos de nuestro ArrayList y darle forma y  estructura a nuestro fichero Json
        // Recorremos el ArrayList y le insertamos la estructura del fichero como queremos que se vea en el Json
        for (int i = 0; i < alumnosListados.size(); i++) {
            Actividad11 alumno = alumnosListados.get(i);
            jb.append("    {\n");
            jb.append("      \"NRE\": \"").append(alumno.getNRE()).append("\",\n");
            jb.append("      \"NOMBRE\": \"").append(alumno.getNombre()).append("\",\n");
            jb.append("      \"APELLIDOS\": \"").append(alumno.getApellidos()).append("\",\n");
            jb.append("      \"TELEFONO\": \"").append(alumno.getTelefono()).append("\",\n");
            jb.append("      \"FECHA NACIMIENTO\": \"").append(alumno.getFechaNacimiento()).append("\",\n");
            jb.append("      \"BECA\": \"").append(alumno.getBeca()).append("\"\n");
            jb.append("    }");

            // Agregar coma si no es el último elemento
            if (i < alumnosListados.size() - 1) {
                jb.append(",");
            }
            // agregamos un retorno de carro
            jb.append("\n");
        }
       // La terminacion de nuestra estructura del json
        jb.append("  ]\n}");

           // Escribimos los datos en el fichero JSON
        try (FileWriter escribimos = new FileWriter(f)) {
            escribimos.write(jb.toString());
            System.out.println("Información se ha guardado");
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    


public static void main(String[] args) {
    //Creamos nuestro ArrayLsit de nuestra clase Actividad11
    ArrayList<Actividad11> alumnosListados = new ArrayList<>();
    // Añadimos los datos manualmente a nuestro ArrayList desde nuestro constructor 
        alumnosListados.add(new Actividad11("48751048-h", "Juan", "Pérez", "123456789", "2000-01-15", "Sí tiene Beca")); 
        alumnosListados.add(new Actividad11("102934857-c", "Ana", "Martínez", "987654321", "2001-05-20", "No tiene Beca"));
        //Llamamos al metodo creado anteriormente
        crearJson(alumnosListados);
       
    }
    
}
