package fr.fleyg.banking;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Cette classe permet de lancer l'application 
 */
public class MainApp extends Application {

    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
        System.setProperty("file.encoding", "UTF-8");
        s = new FenMain("admin", "admin");
        s.setMinWidth(1280);
        s.setMinHeight(720);
        s.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
