
package ni.edu.uam.practicahilos;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProcesadorController {

    @FXML
    private Label lblClientes;

    @FXML
    private Label lblProductos;

    @FXML
    private ProgressBar barraClientes;

    @FXML
    private ProgressBar barraProductos;

    @FXML
    private Button btnIniciarClientes;

    @FXML
    private Button btnDetenerClientes;

    @FXML
    private Button btnIniciarProductos;

    @FXML
    private Button btnDetenerProductos;

    private Thread hiloClientes;
    private Thread hiloProductos;

    private volatile boolean clientesActivos;
    private volatile boolean productosActivos;

    private boolean clientesOcupados;
    private boolean productosOcupados;

    @FXML
    private void iniciarClientes() {

        if (clientesOcupados) {
            return;
        }

        clientesOcupados = true;
        clientesActivos = true;

        barraClientes.setProgress(0);
        lblClientes.setText("0% - Procesando clientes...");

        btnIniciarClientes.setDisable(true);
        btnDetenerClientes.setDisable(false);

        Runnable tareaClientes = () -> {

            try {
                for (int i = 1; i <= 100; i++) {

                    if (!clientesActivos ||
                            Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    Thread.sleep(100);

                    final int avance = i;

                    Platform.runLater(() -> {
                        if (clientesActivos) {
                            barraClientes.setProgress(avance / 100.0);
                            lblClientes.setText(
                                    avance + "% - Procesando clientes..."
                            );
                        }
                    });
                }

                Platform.runLater(() -> {
                    lblClientes.setText("100% - Clientes finalizados");
                    clientesActivos = false;
                    clientesOcupados = false;
                    btnIniciarClientes.setDisable(false);
                    btnDetenerClientes.setDisable(true);
                });

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                Platform.runLater(() -> {
                    lblClientes.setText("Proceso de clientes detenido");
                    clientesActivos = false;
                    clientesOcupados = false;
                    btnIniciarClientes.setDisable(false);
                    btnDetenerClientes.setDisable(true);
                });
            }
        };

        hiloClientes = new Thread(tareaClientes);
        hiloClientes.setName("Hilo-Clientes");
        hiloClientes.setDaemon(true);
        hiloClientes.start();
    }

    @FXML
    private void iniciarProductos() {

        if (productosOcupados) {
            return;
        }

        productosOcupados = true;
        productosActivos = true;

        barraProductos.setProgress(0);
        lblProductos.setText("0% - Procesando productos...");

        btnIniciarProductos.setDisable(true);
        btnDetenerProductos.setDisable(false);

        Runnable tareaProductos = () -> {

            try {
                for (int i = 1; i <= 50; i++) {

                    if (!productosActivos ||
                            Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    Thread.sleep(150);

                    final int avance = i;

                    Platform.runLater(() -> {
                        if (productosActivos) {
                            barraProductos.setProgress(avance / 50.0);
                            lblProductos.setText(
                                    (avance * 100 / 50) +
                                            "% - Procesando productos..."
                            );
                        }
                    });
                }

                Platform.runLater(() -> {
                    lblProductos.setText("100% - Productos finalizados");
                    productosActivos = false;
                    productosOcupados = false;
                    btnIniciarProductos.setDisable(false);
                    btnDetenerProductos.setDisable(true);
                });

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                Platform.runLater(() -> {
                    lblProductos.setText("Proceso de productos detenido");
                    productosActivos = false;
                    productosOcupados = false;
                    btnIniciarProductos.setDisable(false);
                    btnDetenerProductos.setDisable(true);
                });
            }
        };

        hiloProductos = new Thread(tareaProductos);
        hiloProductos.setName("Hilo-Productos");
        hiloProductos.setDaemon(true);
        hiloProductos.start();
    }

    @FXML
    private void iniciarAmbos() {
        iniciarClientes();
        iniciarProductos();
    }

    @FXML
    private void detenerClientes() {

        if (hiloClientes != null && clientesOcupados) {
            clientesActivos = false;
            btnDetenerClientes.setDisable(true);
            lblClientes.setText("Deteniendo clientes...");
            hiloClientes.interrupt();
        }
    }

    @FXML
    private void detenerProductos() {

        if (hiloProductos != null && productosOcupados) {
            productosActivos = false;
            btnDetenerProductos.setDisable(true);
            lblProductos.setText("Deteniendo productos...");
            hiloProductos.interrupt();
        }
    }

    @FXML
    private void reiniciarProceso() {

        if (clientesOcupados || productosOcupados) {
            return;
        }

        barraClientes.setProgress(0);
        barraProductos.setProgress(0);

        lblClientes.setText("0% - Esperando inicio");
        lblProductos.setText("0% - Esperando inicio");

        btnIniciarClientes.setDisable(false);
        btnDetenerClientes.setDisable(true);

        btnIniciarProductos.setDisable(false);
        btnDetenerProductos.setDisable(true);
    }

    public void detenerAlCerrar() {

        clientesActivos = false;
        productosActivos = false;

        if (hiloClientes != null && hiloClientes.isAlive()) {
            hiloClientes.interrupt();
        }

        if (hiloProductos != null && hiloProductos.isAlive()) {
            hiloProductos.interrupt();
        }
    }
}
