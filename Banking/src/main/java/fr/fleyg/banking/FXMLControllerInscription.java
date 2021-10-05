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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;

/**
 * Cette classe permet de contrôler le FXML de la page d'inscription
 */
public class FXMLControllerInscription implements Initializable{

    @FXML
    private TextField SaisiNom;

    @FXML
    private TextField SaisiPrenom;

    @FXML
    private TextField SaisiNUtilisateur;

    @FXML
    private Button BTNInscrir;

    @FXML 
    private PasswordField saisiMDP;

    @FXML
    private Button BTNQuitter;

    @FXML
    private Label messageErreur;

    @FXML 
    private Label messageValidation;

    @FXML 
    private Rectangle RectMdp1;

    @FXML
    private Rectangle RectMdp2;

    @FXML
    private Rectangle RectMdp3;

    @FXML 
    private ImageView flecheRetour;

    private Color Fondgris = Color.web("#F4F4F4");
    
    
    /** 
     * Méthode pour initialiser la fenêtre
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        this.saisiMDP.setOnKeyPressed(e -> {
            ForceMotPasse(e);
        });

        this.flecheRetour.setOnMouseClicked(e -> {
            BTNRetourInscription(e);
        });
    }

    
    /** 
     * Méthode pour quitter la fenêtre
     * @param event
     */
    public void BTNQuitterPageInscription(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        // ferme le programme
    }

    
    /** 
     * Méthode pour retourner à la page d'accueil
     * @param e
     */
    public void BTNRetourInscription(MouseEvent e) {
        try {
            Stage FenAcceuil = new FenAcceuil();
            FenAcceuil.show();
            ((Node) (e.getSource())).getScene().getWindow().hide(); 
        } catch (Exception exception) {
            System.out.println("Erreur de retour  page inscription vers Acceuil" + exception);
        }
    }

    
    /**
     * Méthode qui teste si l'inscription est valide, si oui, l'utilisateur est inscrit, et passe sur la fenêtre du tableau de bord
     * @param e
     */
    public void BTNInscription(ActionEvent e){
        if(!SaisiCorrect(this.SaisiNom.getText())){
            this.messageErreur.setText("Erreur lors de la saisi du Nom");
        }else{
             this.messageErreur.setText("");
            if(!SaisiCorrect(this.SaisiPrenom.getText())){
                this.messageErreur.setText("Erreur lors de la saisie du Prénom");
            }else if(DejaInscrit()){
                this.messageErreur.setText("Vous êtes déjà inscrit");
                //delai(200);
                try{
                    Stage fen = new FenAcceuil();
                    fen.show(); //On lance la fenêtre de connexion
                    ((Node) (e.getSource())).getScene().getWindow().hide(); // on ferme la fenêtre inscription
                } catch (Exception exption){
                    System.out.println("Impossible de lancer la fenêtre de connexion");
                    exption.printStackTrace();
                }
            }else{
                this.messageErreur.setText("");
                if (!SaisiCorrect(this.SaisiNUtilisateur.getText())) {
                    this.messageErreur.setText("Erreur lors de la saisie du Nom d'utilisateur");
                }else{
                    this.messageErreur.setText("");
                    if (!SaisiCorrect(this.SaisiNUtilisateur.getText())) {
                        this.messageErreur.setText("Erreur lors de la saisie du Mot de passe");
                    }else{
                        // saisie d'un utilisateur
                        String chemin = "src/main/resources/Data/";

                        String nomUtilisateur = this.SaisiNUtilisateur.getText();
                        nomUtilisateur = nomUtilisateur.toLowerCase();
                
                        String nomFic = chemin + nomUtilisateur;
                        Path fic = Paths.get(nomFic);
                        
                        byte[] Nom_Binaire = (this.SaisiNom.getText()+"\n").getBytes();//nom >> binaire pour pouvoir écrir en binaire dans le fichier
                        byte[] Prenom_Binaire = (this.SaisiPrenom.getText()+"\n").getBytes();
                        byte[] NomUtilisateur_Binaire = (this.SaisiNUtilisateur.getText()+"\n").getBytes();
                        byte[] Mdp_Binaire = (this.saisiMDP.getText()+"\n").getBytes();
                        

                        OutputStream output = null;
                        try{
                            output = new BufferedOutputStream(Files.newOutputStream(fic, CREATE)); //création du fichier 
                            
                            output.write(Nom_Binaire);
                            output.write(Prenom_Binaire);
                            output.write(NomUtilisateur_Binaire);
                            output.write(Mdp_Binaire);
                            
                            output.flush();//on vide le tampon >> on arrête se qui est en cour
                            output.close();//on ferme le fichier
                            // à partir de la ligne 5 du fic se sont les opérations
                            messageValidation.setText("Inscription réussi ! Bienvenue "+ this.SaisiPrenom.getText() +" sur Banking !");
                            
                            //Thread.sleep(1000); //attendre 1 sec
                            
                            // TODO: faire une instance d'utilisateur
                            Stage fen = new FenMain(this.SaisiNUtilisateur.getText(), this.saisiMDP.getText()); // on affiche le main
                            fen.show();

                            ((Node)(e.getSource())).getScene().getWindow().hide(); //on ferme la fenêtre inscription
                        
                        } catch (Exception exption) {
                            System.out.println("Erreur de création & écriture fichier lors de l'insciption" + exption);
                        }
                    }
                }
            }
        }
    }

    
    /** 
     * Fonction pour savoir si la saisie est correcte
     * @param str
     * @return boolean
     */
    public boolean SaisiCorrect(String str){
        return str.matches("^[^\s](\\pL*)(\s(\\pL*))?");
    }

    
    /** 
     * Fonction pour savoir si l'utilisateur est déjà inscrit
     * @return boolean
     */
    public boolean DejaInscrit(){

        String chemin = "Banking/src/main/resources/Data/";

        String nomUtilisateur = this.SaisiNUtilisateur.getText();
        nomUtilisateur = nomUtilisateur.toLowerCase();

        String nomFic = chemin + nomUtilisateur;

        File fichier = new File(nomFic);
        
        return fichier.exists();
    }

    
    /** 
     * Méthode pour savoir la force du mot de passe
     * @param e
     */
    public void ForceMotPasse(KeyEvent e){
        if(!this.saisiMDP.getText().isEmpty()){
            this.RectMdp1.setFill(Color.RED);
            if(this.saisiMDP.getText().matches("((.*)\\pL+(.*)[0-9]+(.*))||(.*)([0-9]+(.*)\\pL+)(.*)")){
                System.out.println("ok nv1");
                this.RectMdp2.setFill(Color.ORANGE);
                if((this.saisiMDP.getText().matches("(.*)-(.*)"))&&this.saisiMDP.getText().length()>=8){
                    this.RectMdp3.setFill(Color.GREEN);
                }
            }	
        }if(this.saisiMDP.getText().isEmpty()){
            this.RectMdp1.setFill(Fondgris);
            this.RectMdp2.setFill(Fondgris);
            this.RectMdp3.setFill(Fondgris);

        }
    }


    
    /** 
     * Méthode pour avoir du délai
     * @param temps
     */
    public void delai(int temps){
        /*long maintenant = System.currentTimeMillis();
        while(maintenant * temps < System.currentTimeMillis()){
            maintenant=System.currentTimeMillis();
        }*/
        /*try {
            wait(temps);
        } 
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }*/
        try{
            this.messageErreur.setText("Vous êtes déjà inscrit");
            Thread.sleep(temps);  
        }catch(Exception exception){
            System.out.println(exception);
        }
    }
}
