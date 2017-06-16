package apps;

import apps.model.NetServer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AppServer extends Application implements PropertyChangeListener {

    private NetServer server;

    private final Button btnStartServer;
    private final Button btnStopServer;
    private final TextField tfPort;
    private final Label lblPort;
    private final Label lblStatus;
    private final VBox root;

    public AppServer() {
        super();
        this.server = null;
        this.btnStartServer = new Button("Start server");
        this.btnStopServer = new Button("Stop server");
        this.tfPort = new TextField();
        this.lblPort = new Label("Enter server port:");
        this.lblStatus = new Label("Server status");
        this.root = new VBox();

        this.btnStartServer.setOnAction((ActionEvent event) -> {
            startServer();
        });

        this.btnStopServer.setOnAction((ActionEvent event) -> {
            stopServer();
        });
    }

    protected synchronized void startServer() {
        this.stopServer();
        this.server = new NetServer();
        this.server.addPropertyChangeListener(this);
        try {
            int port = Integer.parseInt(this.tfPort.getText());
            this.server.start(port);
        } catch (NumberFormatException ex) {
            Logger.getLogger(AppServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (this.server.isServerRunning()) {
            this.setRunning();
        } else {
            this.stopServer();
        }
    }

    public synchronized void stopServer() {
        if (this.server != null) {
            this.server.stop();
            this.server = null;
        }
        this.setNotRunning();
    }

    protected synchronized void setRunning() {
        this.tfPort.setText(String.valueOf(this.server.getPort()));
        this.tfPort.setDisable(true);
        this.btnStartServer.setDisable(true);
        this.btnStopServer.setDisable(false);
        this.lblStatus.setText("Server is running");
        this.lblStatus.setTextFill(Color.GREEN);
    }

    protected synchronized void setNotRunning() {
        this.tfPort.setDisable(false);
        this.btnStartServer.setDisable(false);
        this.btnStopServer.setDisable(true);
        this.lblStatus.setText("Server is NOT running");
        this.lblStatus.setTextFill(Color.RED);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.root.getChildren().clear();
        this.root.setAlignment(Pos.CENTER);
        this.root.setSpacing(10);
        this.root.setPadding(new Insets(25));
        this.root.getChildren().addAll(this.lblStatus, this.lblPort, this.tfPort, this.btnStartServer, this.btnStopServer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            this.stopServer();
            Platform.exit();
        });
        primaryStage.show();
        this.setNotRunning();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "isServerRunning": {
                boolean isRunning = (boolean) evt.getNewValue();
                if (isRunning) {
                    this.setRunning();
                } else {
                    this.setNotRunning();
                }
            }
            break;
            default: {

            }
            break;
        }
    }
}
