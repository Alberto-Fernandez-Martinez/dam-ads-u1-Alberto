package vista;

import servicio.ClubDeportivo;
import vista.views.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private ClubDeportivo club;
    private BorderPane root;
    private Label status;

    @Override
    public void start(Stage stage) {
        club = new ClubDeportivo();

        root = new BorderPane();
        root.setTop(buildMenuBar());
        status = new Label("Listo");
        status.setPadding(new Insets(4));
        root.setBottom(status);

        // Vista por defecto
        root.setCenter(new DashboardView(club));

        Scene scene = new Scene(root, 960, 640);
        stage.setTitle("Club DAMA Sports");
        stage.setScene(scene);
        stage.show();
    }

    private MenuBar buildMenuBar() {
        MenuBar mb = new MenuBar();

        Menu socios = new Menu("Socios");
        MenuItem altaSocio = new MenuItem("Alta socio");
        altaSocio.setOnAction(e -> root.setCenter(new SocioFormView(club)));
        MenuItem bajaSocio = new MenuItem("Baja socio");
        bajaSocio.setOnAction(e -> root.setCenter(new BajaSocioView(club)));
        socios.getItems().addAll(altaSocio, bajaSocio);

        Menu pistas = new Menu("Pistas");
        MenuItem altaPista = new MenuItem("Alta pista");
        altaPista.setOnAction(e -> root.setCenter(new PistaFormView(club)));
        MenuItem cambiarDisp = new MenuItem("Cambiar disponibilidad");
        cambiarDisp.setOnAction(e -> root.setCenter(new CambiarDisponibilidadView(club)));
        pistas.getItems().addAll(altaPista, cambiarDisp);

        Menu reservas = new Menu("Reservas");
        MenuItem crearReserva = new MenuItem("Crear reserva");
        crearReserva.setOnAction(e -> root.setCenter(new ReservaFormView(club)));
        MenuItem cancelarReserva = new MenuItem("Cancelar reserva");
        cancelarReserva.setOnAction(e -> root.setCenter(new CancelarReservaView(club)));
        reservas.getItems().addAll(crearReserva, cancelarReserva);

        Menu ver = new Menu("Ver");
        MenuItem dashboard = new MenuItem("Dashboard");
        dashboard.setOnAction(e -> root.setCenter(new DashboardView(club)));
        ver.getItems().addAll(dashboard);

        Menu archivo = new Menu("Archivo");

        MenuItem guardar = new MenuItem("Guardar");
        guardar.setOnAction(e -> {
            try {
                if (!club.hayCambiosPendientes()) {
                    showInfo("No hay cambios pendientes para guardar.");
                    status.setText("Sin cambios");
                    return;
                }
                club.guardarDatos();
                showInfo("Cambios guardados en los archivos JSON.");
                status.setText("Cambios guardados");
            } catch (Exception ex) {
                showError("Error guardando: " + ex.getMessage());
                status.setText("Error al guardar");
            }
        });

        MenuItem salir = new MenuItem("Salir");
        salir.setOnAction(e -> {
            if (club.hayCambiosPendientes()) {
                Alert confirm = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Hay cambios sin guardar. Quieres guardarlos antes de salir?",
                        ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
                );
                confirm.setHeaderText(null);
                ButtonType r = confirm.showAndWait().orElse(ButtonType.CANCEL);

                if (r == ButtonType.CANCEL) return;
                if (r == ButtonType.YES) {
                    try {
                        club.guardarDatos();
                        status.setText("Cambios guardados");
                    } catch (Exception ex) {
                        showError("Error guardando: " + ex.getMessage());
                        return; // no salimos si falla el guardado
                    }
                }
            }
            Platform.exit();
        });

        archivo.getItems().addAll(guardar, new SeparatorMenuItem(), salir);

        mb.getMenus().addAll(archivo, socios, pistas, reservas, ver);
        return mb;
    }

    public void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    public void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText("Error");
        a.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        // Si cierran la ventana con la X, preguntamos tambien
        if (club != null && club.hayCambiosPendientes()) {
            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Hay cambios sin guardar. Quieres guardarlos antes de salir?",
                    ButtonType.YES, ButtonType.NO
            );
            confirm.setHeaderText(null);
            ButtonType r = confirm.showAndWait().orElse(ButtonType.NO);

            if (r == ButtonType.YES) {
                try {
                    club.guardarDatos();
                } catch (Exception ignored) {}
            }
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
