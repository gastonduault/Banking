package fr.fleyg.banking;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.*;
/**
 * Cette classe permet de contrôler le FXML de la page d'accueil  
 */

public class FXMLControllerAcceuil implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML 
    private Circle rond1;

    @FXML
    private Circle rond2;
    
    @FXML
    private Circle rond3;

    @FXML
    private Circle rond4;

    @FXML
    private Circle rond5;

    @FXML 
    private ImageView flecheGauche;

    @FXML
    private ImageView flecheDroite;

    @FXML
    private Label txtCaroussel;

    @FXML
    private VBox PageConnexion;

    @FXML 
    private BorderPane pageAcceuil;

    @FXML
    private ImageView CroixConnexion;

    @FXML
    private Button BTNseConnecter;

    @FXML
    private PasswordField saisiMotDePasse;

    @FXML
    private TextField saisiNomUtilisateur;

    @FXML
    private Label messageErreurConnexion;


    private String text1;
    private String text2;
    private String text3;
    private String text4;
    
    private int indice;

    private Color Fondgris = Color.web("#F4F4F4");
    private Color Noir = Color.web("#000");




    
    /** 
     * Méthode pour initialiser la fenêtre d'accueil
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.indice=1;

        this.text1 = "Avec Banking, déterminer facilement vos principales sources de dépense !";
        this.text2 = "Vous pouvez inscrire vos dépenses et vos revenus simplement selon des catégories déterminées";
        this.text3 = "Un système de compte permet de privatiser ses opérations";
        this.text4 = "Vous pouvez garder une trace de vos opérations sur plusieurs années.";

        this.flecheDroite.setOnMouseClicked(e -> {
            caroussel(e);
        });
        this.flecheGauche.setOnMouseClicked(e -> {
            caroussel(e);
        });
        this.CroixConnexion.setOnMouseClicked(e -> {
            BTNQuitterConnexion(e);
        });
    }




    
    /** 
     * Méthode pour fermer le programme
     * @param event
     */
    public void BTNQuitterPageAcceuil(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        // ferme le programme
    }

    
    /** 
     * Méthode pour acceder à la page d'inscription
     * @param e
     */
    public void BTNInscriptionAcceuil(ActionEvent e){
        try{
            Stage fenInscription = new FenInscription(); 
            fenInscription.show();
            ((Node) (e.getSource())).getScene().getWindow().hide(); // on ferme la fenêtre acceuil
        }catch(Exception exception){
            System.out.println("Erreur d'ouverture de page inscription dans page accueil"+exception);
        }
    }

    
    /** 
     * Méthode qui affiche l'overlay pour se connecter
     * @param event
     */
    public void BTNConnexionAcceuil(ActionEvent event){
        this.PageConnexion.setVisible(true);
        
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(20);
        boxBlur.setHeight(20);
        boxBlur.setIterations(3);
        this.pageAcceuil.setEffect(boxBlur);
    }

    
    /** 
     * Méthode pour quitter l'overlay pour se connecter
     * @param e
     */
    public void BTNQuitterConnexion(MouseEvent e){
        this.PageConnexion.setVisible(false);
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(0);
        boxBlur.setHeight(0);
        boxBlur.setIterations(0);
        this.pageAcceuil.setEffect(boxBlur);
    }

    
    /** 
     * Méthode pour valider la connexion
     * @param e
     */
    public void Connexion(ActionEvent e){
        
        File file = Inscrit();
        int i = 0;
        String motDePasse = null;

        if(file!=null){
            try{

                // Créer l'objet File Reader
                FileReader fr = new FileReader(file);
                // Créer l'objet BufferedReader
                BufferedReader br = new BufferedReader(fr);
                // StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    i++;
                    if(i==4){
                        motDePasse=line;
                    }
                }
                fr.close();

                if(this.saisiMotDePasse.getText().equals(motDePasse)){

                    try {
                        // ouverture de la page main
                        Stage stage = new FenMain(this.saisiNomUtilisateur.getText(), this.saisiMotDePasse.getText());
                        stage.show();
                        ((Stage) ap.getScene().getWindow()).close();
                    } catch (Exception exception) {
                        System.out.println("Erreur d'ouverture de page acceuil lors de la connexion réussi : " + exception);
                    }
                }else{
                    this.messageErreurConnexion.setText("Mot de passe invalide");
                }
            }
            catch(Exception exception){
                System.out.println("Erreur de lecture de fichier pour la connexion"+exception);
            }

        }else{
            this.messageErreurConnexion.setText("Vous n'êtes pas inscris");
        }
        
    }

    
    /** 
     * Méthode qui renvoie True si l'utilisateur est bien inscrit
     * @return File
     */
    public File Inscrit() {  //True si inscris sinon false

        String chemin = "Banking/src/main/resources/Data/";

        String nom = this.saisiNomUtilisateur.getText();

        File file = new File(chemin + nom);
        
        if(!file.exists()){
            file=null;
        }
        return file;
    }



    
    /** 
     * Méthode pour faire fonctionner le carrousel 
     * @param e
     */
    public void caroussel(MouseEvent e){
        if(e.getSource()==this.flecheDroite){
            indice++;
            if(indice>4){
                indice=1;
            }
        }else if(e.getSource()==this.flecheGauche){
            indice--;
            if (indice < 1) {
                indice = 4;
            }
        }

        this.rond1.setFill(Fondgris);
        this.rond2.setFill(Fondgris);
        this.rond3.setFill(Fondgris);
        this.rond4.setFill(Fondgris);

        switch (indice) {
            case(1):
                this.txtCaroussel.setText(this.text1);
                this.rond1.setFill(Noir);
                break;
            case (2):
                this.txtCaroussel.setText(this.text2);
                this.rond2.setFill(Noir);
                break;
            case (3):
                this.txtCaroussel.setText(this.text3);
                this.rond3.setFill(Noir);
                break;
            case (4):
                this.txtCaroussel.setText(this.text4);
                this.rond4.setFill(Noir);
                break;
        }
    }


}
