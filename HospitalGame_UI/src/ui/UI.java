package ui;

import common.IBusiness;
import common.IUI;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Starts the UI application
 * 
 * @author Frederik Schultz Rosenberg
 * @author Andreas Bøgh Mølgaard-Andersen
 * @author Tobias Ahrenschneider Sztuk
 * @author Lars Bjerregaard Jørgensen
 * @author Robert Francisti
 */
public class UI extends Application implements IUI{
    
    /**
     * Reference to the UI instance that contains IBusiness reference.
     */
    private static UI ui;
    
    /**
     * 
     * @return the instance of the UI that contains a reference to business
     */
    public static UI getInstance(){
        return ui;
    }
    
    /**
     * Reference to the business layer
     */
    private IBusiness business;
    
    /**
     * The current stage.
     */
    private Stage stage;
    
    /**
     * All images used in the application
     */
    private Sprites sprites;
    
    /**
     * Called at the start of the application. Creates and loads the menu controller
     * @param primaryStage of the window
     * @throws IOException if someone wrong with loading the file for the controller.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        UI.getInstance().stage = primaryStage;
        UI.getInstance().sprites = new Sprites();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Menu.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(1000);
        primaryStage.show();
    }

    /**
     * Sets the reference to business
     * @param business the business layer
     */
    @Override
    public void injectBusiness(IBusiness business) {
        this.business = business;
    }

    /**
     * Starts the application and sets the static instance of UI.
     * @param args from the command line.
     */
    @Override
    public void startApplication(String[] args) {
        ui = this;
        launch(args);
    }

    /**
     * 
     * @return the reference to the business layer
     */
    IBusiness getBusiness() {
        return business;
    }
    
    /**
     * 
     * @return the main stage of the program
     */
    Stage getStage() {
        return stage;
    }
    
    /**
     * 
     * @return the class with all images for the application
     */
    Sprites getSprites() {
        return sprites;
    }
    
}
