package fr.fleyg.banking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Cette classe permet de gérer le FXML de la fênetre d'modifier profil
 */
public class FXMLControllerModifCompte implements Initializable{

    private String nom;
    private String prenom;
    private String nomUtilisateur;
    private String mdp; 
    private Date date; 

    @FXML
    private TextField saisiNom;

    @FXML
    private TextField saisiPrenom;
    
    @FXML
    private TextField saisiNomUtilisateur;

    @FXML
    private ImageView flecheRetourmodif;

    @FXML
    private Label DateCréation;

    @FXML 
    private Label messageErreur;



    public void initialize(URL url, ResourceBundle resourceBundle) {
        recupCompte();
        afficherInfoCompte();

        this.saisiNomUtilisateur.setOnKeyReleased(e -> {
            verifIscrit(e);
        });

        this.flecheRetourmodif.setOnMouseClicked(e -> {
            System.out.println("Test1");
            try{
                retour(e);
            }catch(Exception exception){
                System.out.println(exception);
            }
        });
    }

    public void recupNomUtilisateur(String nomUtiliString, Date dateCrea){
        this.nomUtilisateur=nomUtiliString;
        this.date=dateCrea;
    }

    public void recupCompte(){
        File test = new File("Banking/src/main/resources/Data/" + this.nomUtilisateur);
        if(test.isFile()) 
        {
            try {
                BufferedReader br = new BufferedReader(new FileReader(test));
                // StringBuffer sb  = new StringBuffer();
                this.nom = br.readLine(); // chaque ligne correspond à une info
                this.prenom = br.readLine();
                this.nomUtilisateur = br.readLine();
                this.mdp = br.readLine();

                br.close();
            }catch(Exception exception){
                System.out.println("lecture impossible lors de l'ouverture de la page modif profil : " + exception);
            }
        }    
    }

    public void afficherInfoCompte(){
        this.saisiNom.setText(this.nom);
        this.saisiPrenom.setText(this.prenom);
        this.saisiNomUtilisateur.setText(this.nomUtilisateur);
        this.DateCréation.setText("Inscrit depuis le : "+this.date.getDay()+"/"+this.date.getMonth());
    }

    public boolean DejaInscrit() {

        String chemin = "Banking/src/main/resources/Data/";
        String saisi = this.saisiNomUtilisateur.getText();
        saisi = saisi.toLowerCase();

        String nomFic = chemin + saisi;

        File fichier = new File(nomFic);

        boolean res =  fichier.isFile();
        if(res && (this.saisiNomUtilisateur.getText().equals(this.nomUtilisateur))){
            res = false;
        }
        return res;
    }

    public void verifIscrit(KeyEvent e){
        if(DejaInscrit()){
            this.messageErreur.setText("Ce d'utilisateur est déjà utilisé");
        }else{
            this.messageErreur.setText("");
        }
    }

    public void retour(MouseEvent e) throws IOException{

        System.out.println("Test2");
        Stage fen = new FenMain(this.nomUtilisateur, this.mdp);// on affiche la page main 
        fen.show();

        ((Node) (e.getSource())).getScene().getWindow().hide(); // on ferme la fenêtre 
    }
}