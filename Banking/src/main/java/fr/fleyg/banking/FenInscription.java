package fr.fleyg.banking;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * La classe FenInscription permet de créer la fenêtre d'inscription
 */
public class FenInscription extends Stage{

    public FenInscription()throws IOException{
        FXMLControllerInscription fxmlController = new FXMLControllerInscription();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Page_Inscription.fxml"));
        fxmlLoader.setController(fxmlController);
        Parent root1 = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root1);
        this.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        this.sizeToScene();
    }
    
}
