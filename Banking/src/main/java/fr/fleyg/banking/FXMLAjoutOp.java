package fr.fleyg.banking;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * La classe permet de contrôler le FXML de la fenêtre d'ajout d'une opération
 */

public class FXMLAjoutOp implements Initializable {

    @FXML 
    private TextField tfNomOp;
    @FXML 
    private TextField tfDateOp;
    @FXML 
    private TextField tfMontantOp;
    @FXML
    private CheckBox cRecuOp;
    @FXML
    private Button bAjoutOp;
    @FXML
    private Hyperlink bRetour;
    @FXML
    private Button bDepenseOp;
    @FXML
    private Button bRevenuOp;
    @FXML
    private GridPane gridCatPrin;
    @FXML
    private ListView<SousCatCell> lvSousCat;
    
    
    /** 
     * Méthode d'initialisation de la fenêtre
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // fen modale

        // action pour le clique des catégories
        for(Node n : gridCatPrin.getChildren())
        {
            n.setOnMouseClicked(e -> {
                Integer col = GridPane.getColumnIndex(n);
                if(GridPane.getColumnIndex(n) == null)
                {
                    col = 0;
                }
                Integer row = GridPane.getRowIndex(n);
                if(GridPane.getRowIndex(n) == null)
                {
                    row = 0;
                }
                CategoriePrincipale c = null;
                System.out.println(GridPane.getColumnIndex(n) + " " + GridPane.getRowIndex(n));
                gridCatPrin.setVisible(false); // on cache les catégories principales
                lvSousCat.setVisible(true); // on affiche les sous catégories correspondantes
                lvSousCat.setViewOrder(-1.0); // on le déplace devant
                lvSousCat.getItems().clear(); // on vide la liste
                if(col == 0 && row == 0)
                {
                    // 0 0
                    c = Utilisateur.getInstance().getCompte().getCatPrincipaleParNom("Transport");
                }
                else if(col == 0 && row == 1)
                {
                    // 0 1
                    c = Utilisateur.getInstance().getCompte().getCatPrincipaleParNom("Alimentation");
                }
                else if(col == 1 && row == 1)
                {
                    // 1 1
                    c = Utilisateur.getInstance().getCompte().getCatPrincipaleParNom("Soin");
                }
                else if(col == 2 && row == 1)
                {
                    // 2 1
                    c = Utilisateur.getInstance().getCompte().getCatPrincipaleParNom("Finance");
                }
                else if(col == 3 && row == 1)
                {
                    // 3 1
                    c = Utilisateur.getInstance().getCompte().getCatPrincipaleParNom("Autre");
                }
                else if(col == 3 && row == 0)
                {
                    // 3 0
                    c = Utilisateur.getInstance().getCompte().getCatPrincipaleParNom("Loisir");
                }
                else if(col == 2 && row == 0)
                {
                    // 2 0
                    c = Utilisateur.getInstance().getCompte().getCatPrincipaleParNom("Habillement");
                }
                else if(col == 1 && row == 0)
                {
                    // 1 0
                    c = Utilisateur.getInstance().getCompte().getCatPrincipaleParNom("Habitation");
                }

                for(SousCategorie sc : c.getlSousCat())
                {
                    lvSousCat.getItems().add(new SousCatCell(sc));
                }

            });


        }
        
    }

    /**
     * Cette classe permet de gérer les catégories dans la fenêtre ???
     */
    public static class SousCatCell extends VBox {
        // nom catégorie
        Label nomCat = new Label();
        // gestion image V
        Class<?> classe = this.getClass();
        ImageView imgView = new ImageView();

        SousCatCell(SousCategorie c) {
            super();
            
            InputStream input = classe.getResourceAsStream("/images/categories/"+c.getId()+".png");
            Image img = new Image(input);    
            HBox hb = new HBox();

            nomCat.setText(c.getNom());

            // taille image
            imgView.setImage(img);
            imgView.setFitHeight(50);
            imgView.setFitWidth(50);

            // gestion progressbar
            /*pb.setPrefHeight(35);
            pb.setPadding(new Insets(0,10,0,10));
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setStyle("-fx-accent: " + c.getCouleur()); // couleur de la barre
            pb.setProgress(soldeCat / 3000); // à changer par la suite...
            GridPane.setHgrow(pb, Priority.ALWAYS);
            GridPane.setHalignment(pb, HPos.CENTER);*/

            // taille du solde
            /*mSolde.setPrefHeight(35);
            mSolde.setPadding(new Insets(0,10,0,0));*/
            hb.getChildren().add(imgView);
            hb.getChildren().add(nomCat);
            this.getChildren().addAll(hb);
        }

        String getNomCat()
        {
            return this.nomCat.getText();
        }
    }

    
    /** 
     * Méthode pour changer du mode dépense au mode revenu
     * @param event
     */
    public void bRevenuOpOnAction(ActionEvent event) {

        bRevenuOp.getStyleClass().remove("bNonSelectionne");
        bRevenuOp.getStyleClass().add("bSelectionne");
        bDepenseOp.getStyleClass().remove("bSelectionne");
        bDepenseOp.getStyleClass().add("bNonSelectionne");
        lvSousCat.getItems().clear(); // on vide la liste
        lvSousCat.setVisible(true);
        gridCatPrin.setVisible(false); // on cache la liste des catégories principales de dépense
        lvSousCat.setViewOrder(-1.0); // on le déplace devant
        for(SousCategorie c : Utilisateur.getInstance().getCompte().getCatPrincipaleParNom("Revenu").getlSousCat())
        {
            // on récupère la liste des sous cat de revenu
            lvSousCat.getItems().add(new SousCatCell(c));
        }
    }

    
    /** 
     * Méthode pour passer du mode revenu au mode dépense
     * @param event
     */
    public void bDepenseOpOnAction(ActionEvent event) {
        bDepenseOp.getStyleClass().remove("bNonSelectionne");
        bDepenseOp.getStyleClass().add("bSelectionne");
        bRevenuOp.getStyleClass().remove("bSelectionne");
        bRevenuOp.getStyleClass().add("bNonSelectionne");
        gridCatPrin.setVisible(true); // on affiche la liste des cat princ
        lvSousCat.setVisible(false);
    } 

    
    /** 
     * Fonction pour obtenir le type de l'opération
     * @return String
     */
    public String obtenirTypeOperation()
    {
        String bouton = "";
        if(rechercherElement(bRevenuOp.getStyleClass(), "bSelectionne"))
        {
            bouton = "revenu";
        }
        else if(rechercherElement(bDepenseOp.getStyleClass(), "bSelectionne"))
        {
            bouton = "dépense";
        }
        else
        {
            System.out.println("erreur");
        }
        return bouton;
    }

    
    /** 
     * Méthode pour rechercher des éléments
     * @param obl
     * @param motRecherche
     * @return boolean
     */
    // méthode pour rechercher des éléments
    public boolean rechercherElement(ObservableList<String> obl, String motRecherche)
    {
        boolean estDans = false;
        if(obl.contains(motRecherche))
        {
            estDans = true;
        }
        return estDans;
    }

    
    /** 
     * Méthode pour fermer le programme
     * @param event
     */
    public void bRetourOpOnAction(ActionEvent event) {
        ((Stage)(((Hyperlink)event.getSource()).getScene().getWindow())).close();
        // ferme le programme
    }

    
    /** 
     * Méthode qui ajoute l'enregistrement d'une opération et qui ferme la fenêtre
     * @param event
     */
    public void bAjoutOpOnAction(ActionEvent event)
    {
        // ajout enregistrement d'une opération
        Operation op;
        // on trouve le genre de l'opération (récurrente ou ponctuelle)
        if(cRecuOp.isArmed())
        { 
            // si checké, c'est une op Récurrente
            // TODO !
            op = null;
        }
        else
        {
            // opération ponctuelle
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try
            {
                if(lvSousCat.getSelectionModel().getSelectedItem() != null)
                {
                    // si aucune catégorie sélectionnée, on proposera une caté automatiquement en fonction d'une nom de l'op
                    op = new OperationPonctuelle(
                        obtenirTypeOperation(),
                        tfNomOp.getText(),
                        Double.valueOf(tfMontantOp.getText()), sdf.parse(tfDateOp.getText()),
                        Utilisateur.getInstance().getCompte().getSousCatParNom(lvSousCat.getSelectionModel().getSelectedItem().getNomCat()), 
                        true);

                    System.out.println("Opération " + tfNomOp.getText() + " du "+ tfDateOp.getText() + " de catégorie " + lvSousCat.getSelectionModel().getSelectedItem().getNomCat() + " enregistrée");
    
                }
                else
                {
                    System.out.println("Catégorie non sélectionnée");
                }
            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }
        }

        
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close(); // fermeture de la fenêtre

    }

}
