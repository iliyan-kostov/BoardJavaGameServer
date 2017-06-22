package apps;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class AppClient extends Application implements PropertyChangeListener {

    private NetClient client;

    private final Label lblStatus;
    private final Label lblHostname;
    private final Label lblPort;
    private final Label lblUsername;
    private final Label lblPassword;
    private final Circle lblStatus2;
    private final TextField tfHostname;
    private final TextField tfPort;
    private final TextField tfUsername;
    private final TextField tfPassword;

    private final Button btnConnect;
    private final Button btnDisconnect;

    private final GridPane root;

    public AppClient() {
        super();
        this.client = null;
        this.btnConnect = new Button("Connect");
        this.btnDisconnect = new Button("Disconnect");
        this.lblStatus = new Label("Status");
        this.lblHostname = new Label("Hostname");
        this.lblPort = new Label("Port");
        this.lblUsername = new Label("Username");
        this.lblPassword = new Label("Password");
        this.tfHostname = new TextField();
        this.tfPort = new TextField();
        this.tfUsername = new TextField();
        this.tfPassword = new TextField();
        this.lblStatus2 = new Circle(5);

        this.root = new GridPane();

        this.btnConnect.setOnAction((ActionEvent event) -> {
            startClient();
        });

        this.btnDisconnect.setOnAction((ActionEvent event) -> {
            stopClient();
        });
    }

    protected synchronized void startClient() {
        this.stopClient();
        String username = this.tfUsername.getText();
        String password = this.tfPassword.getText();
        try {
            String hostname = this.tfHostname.getText();
            int port = Integer.parseInt(this.tfPort.getText());
            try {
                Socket socket = new Socket(hostname, port);
                {
                    this.client = new NetClient(socket, username, password);
                    this.client.addPropertyChangeListener(this);
                    this.client.startConnection();
                }
            } catch (IOException ex) {
                Logger.getLogger(AppClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(AppClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void stopClient() {
        if (this.client != null) {
            this.client.stopConnection();
        }
        this.client = null;
        this.setNotRunning();
    }

    protected synchronized void setRunning() {
        this.tfHostname.setDisable(true);
        this.tfPort.setDisable(true);
        this.tfUsername.setDisable(true);
        this.tfPassword.setDisable(true);
        this.btnConnect.setDisable(true);
        this.btnDisconnect.setDisable(false);
        this.lblStatus2.setFill(Color.GREEN);
    }

    protected synchronized void setNotRunning() {
        this.tfHostname.setDisable(false);
        this.tfPort.setDisable(false);
        this.tfUsername.setDisable(false);
        this.tfPassword.setDisable(false);
        this.btnConnect.setDisable(false);
        this.btnDisconnect.setDisable(true);
        this.lblStatus2.setFill(Color.RED);
    }

    @Override
    public void start(Stage primaryStage) {
        this.root.getChildren().clear();
        this.root.setAlignment(Pos.CENTER);
        this.root.setPadding(new Insets(25));
        Node[] nodes = {
            this.lblStatus, this.lblStatus2,
            this.lblHostname, this.tfHostname,
            this.lblPort, this.tfPort,
            this.lblUsername, this.tfUsername,
            this.lblPassword, this.tfPassword,
            this.btnConnect, this.btnDisconnect};
        for (int i = 0; i < nodes.length; i++) {
            this.root.add(nodes[i], i % 2, i / 2);
        }
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            this.stopClient();
            Platform.exit();
        });
        primaryStage.show();
        this.setNotRunning();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case NetClientsideConnection.EVENT_IS_CLIENT_RUNNING: {
                boolean isRunning = (boolean) evt.getNewValue();
                if (isRunning) {
                    this.setRunning();
                    // show alert in FX UI:
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // Update UI here:
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Connected to server!");
                            alert.showAndWait();
                        }
                    });
                } else {
                    this.setNotRunning();
                    // show alert in FX UI:
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // Update UI here:
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Disconnected from server!");
                            alert.showAndWait();
                        }
                    });
                }
            }
            break;
            default: {
            }
            break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
