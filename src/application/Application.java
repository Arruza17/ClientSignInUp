package application;

import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import view.controllers.OwnerWindowController;
import view.controllers.SignInController;

/**
 * Entry point for the java application
 *
 * @author BluRoof
 */
public class Application extends javafx.application.Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    /**
     * Entry point for the java application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Entry pont for the JavaFX application. Load, sets and shows the primary
     * window
     *
     * @param primaryStage The primary window of the applicarion
     * @throws Exception the Exception to be thrown
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/fxml/OwnerWindow.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        OwnerWindowController controller = ((OwnerWindowController) fxmlLoader.getController());
        controller.setStage(primaryStage);
        //Initialize stage
        LOGGER.info("Openning SignIn Window");
        controller.initStage(root);

    }
}
