package vista.views;

import modelo.*;
import servicio.ClubDeportivo;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaFormView extends GridPane {

    public ReservaFormView(ClubDeportivo club) {
        setPadding(new Insets(12));
        setHgap(8);
        setVgap(8);

        TextField id = new TextField();
        ComboBox<Socio> comboSocio = new ComboBox<>();
        ComboBox<Pista> comboPista = new ComboBox<>();
        DatePicker fecha = new DatePicker(LocalDate.now());
        TextField hora = new TextField("10:00");
        Spinner<Integer> duracion = new Spinner<>(30, 300, 60, 30);
        TextField precio = new TextField("10.0");
        Button crear = new Button("Reservar");

        comboSocio.getItems().setAll(club.getSocios());
        comboPista.getItems().setAll(club.getPistas());

        comboSocio.setConverter(new StringConverter<>() {
            @Override public String toString(Socio s) {
                if (s == null) return "";
                return s.getIdSocio() + " - " + s.getNombre();
            }
            @Override public Socio fromString(String string) { return null; }
        });

        comboPista.setConverter(new StringConverter<>() {
            @Override public String toString(Pista p) {
                if (p == null) return "";
                return p.getIdPista() + " - " + p.getDeporte() + " (disp: " + p.isDisponible() + ")";
            }
            @Override public Pista fromString(String string) { return null; }
        });

        addRow(0, new Label("idReserva*"), id);
        addRow(1, new Label("Socio*"), comboSocio);
        addRow(2, new Label("Pista*"), comboPista);
        addRow(3, new Label("Fecha*"), fecha);
        addRow(4, new Label("Hora inicio* (HH:mm)"), hora);
        addRow(5, new Label("Duracion (min)"), duracion);
        addRow(6, new Label("Precio (EUR)"), precio);
        add(crear, 1, 7);

        crear.setOnAction(e -> {
            try {
                String idTxt = t(id);
                if (idTxt.isBlank()) { showError("idReserva es obligatorio"); return; }

                Socio s = comboSocio.getValue();
                if (s == null) { showError("Selecciona un socio"); return; }

                Pista p = comboPista.getValue();
                if (p == null) { showError("Selecciona una pista"); return; }

                LocalDate f = fecha.getValue();
                if (f == null) { showError("Fecha obligatoria"); return; }

                LocalTime t;
                try { t = LocalTime.parse(hora.getText().trim()); }
                catch (Exception parse) { showError("Hora invalida. Usa HH:mm (por ejemplo 10:00)"); return; }

                double pr;
                try { pr = Double.parseDouble(precio.getText().trim()); }
                catch (Exception parse) { showError("Precio invalido (ej: 10.0)"); return; }

                Reserva r = new Reserva(idTxt, s.getIdSocio(), p.getIdPista(), f, t, duracion.getValue(), pr);

                club.crearReserva(r);

                showInfo("Operacion realizada. Pulsa Archivo -> Guardar para guardar los cambios en archivos JSON.");
                id.clear();

            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

    private String t(TextField tf) { return tf.getText() == null ? "" : tf.getText().trim(); }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText("Error");
        a.showAndWait();
    }
    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
