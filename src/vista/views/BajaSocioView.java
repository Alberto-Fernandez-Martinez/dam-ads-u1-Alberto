package vista.views;

import servicio.ClubDeportivo;
import modelo.Socio;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class BajaSocioView extends GridPane {

    public BajaSocioView(ClubDeportivo club) {
        setPadding(new Insets(12));
        setHgap(8); setVgap(8);

        ComboBox<Socio> comboSocio = new ComboBox<>();
        Button baja = new Button("Dar de baja");

        comboSocio.getItems().setAll(club.getSocios());
        comboSocio.setConverter(new StringConverter<>() {
            @Override public String toString(Socio s) {
                if (s == null) return "";
                return s.getIdSocio() + " - " + s.getNombre() + " " + s.getApellidos();
            }
            @Override public Socio fromString(String string) { return null; }
        });

        addRow(0, new Label("Socio"), comboSocio);
        add(baja, 1, 1);

        baja.setOnAction(e -> {
            try {
                Socio sel = comboSocio.getValue();
                if (sel == null) { showError("Selecciona un socio."); return; }

                club.bajaSocio(sel.getIdSocio());

                showInfo("Operacion realizada. Pulsa Archivo -> Guardar para guardar los cambios en archivos JSON.");
                comboSocio.getItems().setAll(club.getSocios());

            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });
    }

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
