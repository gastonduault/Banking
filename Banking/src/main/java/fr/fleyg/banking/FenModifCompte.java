package fr.fleyg.banking;

import java.io.IOException;
import java.util.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * La classe FenMain permet de créer la fenêtre principale
 */
public class FenModifCompte extends Stage{


    public FenModifCompte(String nomUtilisateur, Date dateCréa) throws IOException
    {
        FXMLControllerModifCompte fxmlController = new FXMLControllerModifCompte(); 
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/modifierProfil.fxml"));
        fxmlLoader.setController(fxmlController);
        fxmlController.recupNomUtilisateur(nomUtilisateur, dateCréa);
        Parent root1 = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root1);
        this.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        this.sizeToScene();    
    }

}
