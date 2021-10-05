package fr.fleyg.banking;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * La classe FenAjoutOp permet de créer la fenêtre d'ajout d'une opération
 */
public class FenAjoutOp extends Stage{

    public FenAjoutOp() throws IOException
    {
        this.initModality(Modality.APPLICATION_MODAL);  // fen modale
        FXMLAjoutOp fxmlController = new FXMLAjoutOp();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ajouterOperation.fxml"));
        fxmlLoader.setController(fxmlController);
        Parent root1 = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root1);
        this.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        this.sizeToScene();    
    }

}