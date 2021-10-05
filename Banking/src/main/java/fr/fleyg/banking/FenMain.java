package fr.fleyg.banking;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * La classe FenMain permet de créer la fenêtre principale
 */
public class FenMain extends Stage{


    public FenMain(String nom, String mdp) throws IOException
    {
        FXMLController fxmlController = new FXMLController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlController.initUtilisateur(nom, mdp);
        fxmlLoader.setController(fxmlController);
        Parent root1 = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root1);
        this.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        this.sizeToScene();    
    }

    


}
