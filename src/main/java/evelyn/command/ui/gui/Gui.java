package evelyn.command.ui.gui;

import evelyn.Evelyn;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Gui extends Application{
    private static final String DEFAULT_FILE_PATH = "src/main/data/evelyn.txt";
    private String filePath;
    private Scene scene;
    private Evelyn evelyn = new Evelyn();

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    public Gui(String filePath) {
        this.filePath = filePath;
    }

    public Gui() {
        this(DEFAULT_FILE_PATH);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setEvelyn(evelyn);  // inject the Duke instance
            stage.show();
        } catch (IOException e) {
            System.err.println("Error starting application: " + e);
        }
    }
}
