package fr.fleyg.banking;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * La classe FenAccueil permet de créer la fenêtre d'accueil
 */
public class FenAcceuil extends Stage {

    public FenAcceuil() throws IOException {
        FXMLControllerAcceuil fxmlController = new FXMLControllerAcceuil();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/pageDAccueil.fxml"));
        fxmlLoader.setController(fxmlController);
        Parent root1 = (Parent) fxmlLoader.load();
        Scene scene = new Scene(root1);
        this.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        this.sizeToScene();
    }

}
