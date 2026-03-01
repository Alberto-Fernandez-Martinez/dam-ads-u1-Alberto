package vista.views;

import modelo.Socio;
import servicio.ClubDeportivo;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class SocioFormView extends GridPane {

    public SocioFormView(ClubDeportivo club) {
        setPadding(new Insets(12));
        setHgap(8);
        setVgap(8);

        TextField id = new TextField();
        TextField dni = new TextField();
        TextField nombre = new TextField();
        TextField apellidos = new TextField();
        TextField tel = new TextField();
        TextField email = new TextField();
        Button crear = new Button("Crear");

        // Solo numeros en telefono
        tel.textProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) return;
            if (!newV.matches("\\d*")) tel.setText(newV.replaceAll("\\D", ""));
        });

        addRow(0, new Label("idSocio*"), id);
        addRow(1, new Label("DNI*"), dni);
        addRow(2, new Label("Nombre*"), nombre);
        addRow(3, new Label("Apellidos*"), apellidos);
        addRow(4, new Label("Telefono*"), tel);
        addRow(5, new Label("Email*"), email);
        add(crear, 1, 6);

        crear.setOnAction(e -> {
            try {
                String idTxt = t(id);
                String dniTxt = t(dni);
                String nombreTxt = t(nombre);
                String apellidosTxt = t(apellidos);
                String telTxt = t(tel);
                String emailTxt = t(email);

                if (idTxt.isBlank() || dniTxt.isBlank() || nombreTxt.isBlank() || apellidosTxt.isBlank()
                        || telTxt.isBlank() || emailTxt.isBlank()) {
                    showError("Todos los campos son obligatorios");
                    return;
                }

                Socio s = new Socio(idTxt, dniTxt, nombreTxt, apellidosTxt, telTxt, emailTxt);
                club.altaSocio(s);

                showInfo("Operacion realizada. Pulsa Archivo -> Guardar para guardar los cambios en archivos JSON.");
                id.clear(); dni.clear(); nombre.clear(); apellidos.clear(); tel.clear(); email.clear();

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
