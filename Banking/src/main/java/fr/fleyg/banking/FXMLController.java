package fr.fleyg.banking;

import static java.nio.file.StandardOpenOption.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.Node;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Cette classe permet de gérer le FXML de la fênetre d'accueil
 */
public class FXMLController implements Initializable{
    
    @FXML
    private Label montantSolde;

    /* ---------------------- NODE TABLEAU DE BORD ---------------------- */ 
    @FXML
    private Button bDepenseBord;
    @FXML
    private Button bRevenuBord;
    @FXML
    private Button bSemaineBord;
    @FXML
    private Button bAnneeBord;
    @FXML
    private Button bMoisBord;

    /* ---------------------- NODE TABLEAU DE BORD ---------------------- */ 
    @FXML
    private TabPane tabPane;
    @FXML
    private Hyperlink refresh;
    @FXML
    private Button bDepenseEvo;
    @FXML
    private Button bRevenuEvo;
    @FXML
    private Button bSemaineEvo;
    @FXML
    private Button bAnneeEvo;
    @FXML
    private Button bMoisEvo;
    @FXML
    private StackedBarChart<String, Double> diagEvo;
    @FXML
    private CategoryAxis axeCategorie;
    @FXML
    private NumberAxis axeNombre;
    @FXML
    private Button bQuitter;
    @FXML
    private Tab tabBord;
    @FXML
    private Tab tabBudget;
    @FXML
    private Tab tabPeriode;
    @FXML
    private Tab tabTransaction;
    @FXML
    private AnchorPane ap;
    private Utilisateur u;
    @FXML
    private PieChart diagTabBord;
    @FXML
    private Label lInfoTypeOperation;

    // gestion des pop up d'affichage du profil et pour ajouter une opération
    @FXML
    private Hyperlink bProfil;
    @FXML
    private Hyperlink bPlus;
    @FXML
    private AnchorPane paneProfil;
    @FXML
    private AnchorPane panePlus;
    @FXML
    private Button bProfilDeco;
    @FXML
    private Button bProfilAcceder;
    @FXML
    private Button bProfilPref;
    @FXML
    private Hyperlink bPlusRetour;    
    @FXML
    private Button bPlusAjouterOp;
    @FXML 
    private ListView<VBoxCell> listeBord;

    /* ------------------------- NODE TAB RESUlTAT PAR PERIODE  ------------------------- */
    @FXML
    private Button bSemainePerio;
    @FXML
    private Button bMoisPerio;
    @FXML
    private Button bAnneePerio;
    @FXML
    private CategoryAxis axeCatPerio;
    @FXML
    private NumberAxis axeNumPerio;
    @FXML
    private StackedBarChart<String, Number> diagPerio;
  // ajout ou modification des seuils
    @FXML
    private VBox pageModifSeuil;
    @FXML
    private Label LabelpageModif;
    @FXML
    private ImageView flecheRetour_pageModifSeuil;
    @FXML
    private Slider slider_pageModifSeuil;
    @FXML
    private TextField TextField_pageModifSeuil;
    @FXML
    private Label MessageErreurSeuil;
    @FXML
    private Button BTNValiderSeuil;
    @FXML
    private Label CategoriePageModif;

    /*--------------- Page transaction----------------------------------------------------*/
    @FXML
    private Label montantSolde1;
    @FXML
    ListView<VBox> listViewTransactions;


    
    /** 
     * Méthode d'initialisation de la fenêtre
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) 
    {

        actualiserSolde();
        actualiserDiagBord();

        // on met l'anneé courante par défaut et la périodicité définie de préférence par l'utilisateur
        actualiserDiagEvo(Calendar.getInstance().get(Calendar.YEAR), "mois");

        List<VBoxCell> list = new ArrayList<>();

        // ---------------------- GESTION LISTEVIEW ----------------------
        ArrayList<CategoriePrincipale> lCatAvecSeuil = new ArrayList<>();
        for(Seuil s : Utilisateur.getInstance().getCompte().getListeSeuil())
        {
            // ajout de la liste
            if(!s.getCategorie().getNom().equals("Revenu"))
            {
                list.add(new VBoxCell(Utilisateur.getInstance().getCompte().getSoldePourUneCatPrincipale(s.getCategorie()), s.getMontantSeuil(), s.getCategorie()));
                System.out.println("ajout seuil");
                lCatAvecSeuil.add(s.getCategorie());    
            }
        }

        for(CategoriePrincipale cp : Utilisateur.getInstance().getCompte().getListeCategoriePrincipale())
        {
            if(!lCatAvecSeuil.contains(cp))
            {
                if(!cp.getNom().equals("Revenu"))
                {
                    // on ne traite pas les revenus
                    list.add(new VBoxCell(0.0, 0.0, cp));
                }
                // si ne contient pas la cat, on créé la liste avec un seuil à 0
            }
        }

        ObservableList<VBoxCell> observableList = FXCollections.observableList(list);
        listeBord.setItems(observableList);
          VBoxCell.buttonModSeuil1.setOnAction(e -> {
            gererBoutonModSeuil(e);
        });
        VBoxCell.buttonModSeuil2.setOnAction(e -> {
            gererBoutonModSeuil(e);
        });
        VBoxCell.buttonModSeuil3.setOnAction(e -> {
            gererBoutonModSeuil(e);
        });
        VBoxCell.buttonModSeuil4.setOnAction(e -> {
            gererBoutonModSeuil(e);
        });
        VBoxCell.buttonModSeuil5.setOnAction(e -> {
            gererBoutonModSeuil(e);
        });
        VBoxCell.buttonModSeuil6.setOnAction(e -> {
            gererBoutonModSeuil(e);
        });
        VBoxCell.buttonModSeuil7.setOnAction(e -> {
            gererBoutonModSeuil(e);
        });
        VBoxCell.buttonModSeuil8.setOnAction(e -> {
            gererBoutonModSeuil(e);
        });

        this.flecheRetour_pageModifSeuil.setOnMouseClicked(e -> {
            retourModifSeuil(e);
        });

        this.slider_pageModifSeuil.setOnMouseClicked(e ->  {
            gererSliderSeuil(e);
        });

        this.TextField_pageModifSeuil.setOnKeyPressed(e -> {
            gererTextFieldSeuil(e);
        });

        this.BTNValiderSeuil.setOnAction(e -> {
            InsererSeuil(e);
        });
        // remplissage de la liste des opérations
        remplirTabTransaction();

        tabPane.setOnMouseClicked(e -> {
            Tab t = tabPane.getSelectionModel().getSelectedItem();
            if(t == tabBord)
            {
                actualiserDiagBord();
                actualiserSolde();
            }
            else if(t == tabPeriode)
            {
                actualiserDiagPerio(2021, obtenirPeridicitePerioSelectionne());
            }
            else if(t == tabBudget)
            {
                actualiserDiagEvo(2021, obtenirPeridiciteEvoSelectionne());
            }
            else
            {
                actualiserSolde();
                //todo : mes transactions
            }
            });

            if(!FichierSeuilExist()){
                creerFichierSeuil();
            }
    }


    
    /** 
     * Méthode pour rafraîchir à une action 
     * @param e
     */
    public void refreshOnAction(ActionEvent e)
    {
        Tab t = tabPane.getSelectionModel().getSelectedItem();
        if(t == tabBord)
        {
            actualiserDiagBord();
            actualiserSolde();
        }
        else if(t == tabPeriode)
        {
            actualiserDiagPerio(2021, obtenirPeridicitePerioSelectionne());
        }
        else if(t == tabBudget)
        {
            actualiserDiagEvo(2021, obtenirPeridiciteEvoSelectionne());
        }
        else
        {
            actualiserSolde();
            remplirTabTransaction();
            //todo : mes transactions
        }
    }


    
    /** 
     * Méthode pour initialiser l'instance de l'utilisateur connecté
     * @param nom
     * @param mdp
     */
    public void initUtilisateur(String nom, String mdp)
    {
        // permet d'initialiser l'instance de l'utilisateur connecté
        Utilisateur.setInstance(nom, mdp);
        this.u = Utilisateur.getInstance();
    }
    
    public void actualiserSolde()
    {
        if(u.getCompte().aDecouvert())
        {
            montantSolde.setTextFill(Color.RED);
            montantSolde1.setTextFill(Color.RED);
        }
        else
        {
            montantSolde.setTextFill(Color.GREEN);
            montantSolde1.setTextFill(Color.GREEN);
        }
            montantSolde.setText(" " + u.getCompte().getSolde());
            montantSolde1.setText(" " + u.getCompte().getSolde());
    }

    
    /** 
     * Méthode pour gérer les boutons des seuils
     * @param e
     */
    public void gererBoutonModSeuil(ActionEvent e){
        int indice = 8;
        if(e.getTarget()==VBoxCell.buttonModSeuil1){
            indice = 1;
        }else if(e.getTarget()==VBoxCell.buttonModSeuil2){
            indice = 2;
        }else if(e.getTarget()==VBoxCell.buttonModSeuil3){
            indice = 3;
        }else if(e.getTarget()==VBoxCell.buttonModSeuil4){
            indice = 4;
        }else if(e.getTarget()==VBoxCell.buttonModSeuil5){
            indice = 5;
        } else if (e.getTarget() == VBoxCell.buttonModSeuil6) {
            indice = 6;
        } else if (e.getTarget() == VBoxCell.buttonModSeuil7) {
            indice = 7;
        }
        this.MessageErreurSeuil.setVisible(false);
        this.MessageErreurSeuil.setText(String.valueOf(indice)); //technique un peux moyenne et pas très propre

        this.pageModifSeuil.setVisible(true);
        this.pageModifSeuil.toFront();
        this.pageModifSeuil.setStyle("-fx-background-color: #FFFF;");
        
        // effet sombre
        Lighting effect = new Lighting();
        effect.setDiffuseConstant(0.51);
        effect.setSpecularConstant(0.0);
        this.listeBord.setEffect(effect);
        

        this.CategoriePageModif.setText(VBoxCell.getCatMod(indice).getNom());
        Color couleurCat = Color.web(VBoxCell.getCatMod(indice).getCouleur());
        this.CategoriePageModif.setTextFill(couleurCat);

        HashMap<CategoriePrincipale, Double> map = (HashMap<CategoriePrincipale, Double>) u.getCompte().getSoldeParCatPrincipale("dépense");
        double solde=map.get(VBoxCell.getCatMod(indice));

        Double seuil= montantSeuil(VBoxCell.getCatMod(indice));
        this.TextField_pageModifSeuil.setText(String.valueOf(seuil));
        this.slider_pageModifSeuil.setValue(seuil);
        
        
        if(solde>0){
            this.slider_pageModifSeuil.setMin(solde / 7);
            this.slider_pageModifSeuil.setMax(solde * 10);
        }else if(solde<=0 && seuil>0){
            this.slider_pageModifSeuil.setMin(solde);
            this.slider_pageModifSeuil.setMax(seuil*5);
        }else{
            this.slider_pageModifSeuil.setMin(solde);
            this.slider_pageModifSeuil.setMax(10000);
        }
    }

    
    /** 
     * Fonction pour avoir le montant d'un seuil selon sa catégorie
     * @param categorie
     * @return Double
     */
    public Double montantSeuil(Categorie categorie){
        Double seuil = null;

        File file = new File("Banking/src/main/resources/Data/"+ u.getNomUtilisateur()+"Seuil");
        
        try{
            if(file.exists()){
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) { //on parcour le fichier userSeuil
                    String elem[] = line.split("-");        //on séapre les lignes avec split à partir des -
                    if (elem[0].equals(categorie.getNom())){    //si la ligne commence par le nom de la catégorie principale
                        seuil=Double.valueOf(elem[1]);                                           //et si le seuil est plus grand que le solde de la catégorie alors c'est un seuil qui existe
                    }
                }
                fr.close();
            }
        }catch(Exception exception){
            System.out.println("Le fichier avec les seuils de "+u.getNom()+" n'est pas trouvé - "+exception);
        }
        return seuil;
    }


    /**
     * Méthode pour écrire dans le fichier des seuils
     */

    public void FichierSeuil() {
        String nomFic = "Banking/src/main/resources/Data/" + u.getNomUtilisateur() + "Seuil";
        Path file = Paths.get(nomFic);
        OutputStream output = null;
        
        try {
            output = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
           
            for(Map.Entry map : u.getCompte().getSoldeParCatPrincipale("dépense").entrySet()){
                CategoriePrincipale cat = (CategoriePrincipale) map.getKey(); //obligé de caster sinon sa ne marche pas
                Double soldeCat = (Double)map.getValue();

                byte[] data = (cat.getNom() + "-" + soldeCat + "\n").getBytes();
                /*if(cat.getSeuil().getMontantSeuil()>soldeCat){
                    data= null;
                    data = (cat.getNom() + "-" + cat.getSeuil().getMontantSeuil() + "\n").getBytes();
                }*/

                output.write(data);
            }

            output.flush();
            output.close();
            System.out.println("fichier avec les seuils de " + u.getNom() + " actualisé");

        } catch (Exception e) {
            System.out.println("Erreur de création & écriture du fichier seuil lors de l'insciption" + e);
        }
    }

    
    /** 
     * Fonction pour connaître si le seuil existe dans le fichier
     * @return boolean
     */
    public boolean FichierSeuilExist(){
        String nomFic = "Banking/src/main/resources/Data/" + u.getNomUtilisateur() + "Seuil";
        File file = new File(nomFic);
        return file.exists();
    }

    /**
     * Méthode pour créer le fichier des seuils
     */

    public void creerFichierSeuil() {
        String nomFic = "Banking/src/main/resources/Data/" + u.getNomUtilisateur() + "Seuil";
        Path file = Paths.get(nomFic);
        OutputStream output = null;

        try {
            output = new BufferedOutputStream(Files.newOutputStream(file, CREATE));

            for (Map.Entry map : u.getCompte().getSoldeParCatPrincipale("dépense").entrySet()) {
                CategoriePrincipale cat = (CategoriePrincipale) map.getKey(); // obligé de caster sinon sa ne marche pas
                Double soldeCat = (Double) map.getValue();
                byte[] data = (cat.getNom() + "-" + soldeCat + "\n").getBytes();
                output.write(data);
            }

            output.flush();
            output.close();
            System.out.println("fichier avec les seuils de " + u.getNom() + " créé");

        } catch (Exception e) {
            System.out.println("Erreur de création & écriture du fichier seuil lors de l'insciption" + e);
        }
    }


    
    /** 
     * Méthode pour gérer les sliders des seuils
     * @param e
     */
    public void gererSliderSeuil(MouseEvent e){
        this.TextField_pageModifSeuil.setText(String.valueOf(this.slider_pageModifSeuil.getValue()));
        HashMap<CategoriePrincipale, Double> map = (HashMap<CategoriePrincipale, Double>) u.getCompte().getSoldeParCatPrincipale("dépense");
        double solde = map.get(VBoxCell.getCatMod(Integer.parseInt(this.MessageErreurSeuil.getText())));
        
        if((this.slider_pageModifSeuil.getValue()<solde) || (Double.parseDouble(this.TextField_pageModifSeuil.getText()) < solde)){
            this.slider_pageModifSeuil.setStyle("-fx-control-inner-background: #ff0000;");
            this.TextField_pageModifSeuil.setStyle("-fx-color: #ff0000;");
            this.BTNValiderSeuil.setDisable(true);
        } else{
            this.slider_pageModifSeuil.setStyle("-fx-control-inner-background: #03B000;");
            this.TextField_pageModifSeuil.setStyle("-fx-color: #03B000;");
            this.BTNValiderSeuil.setDisable(false);
        }
    }

    
    /** 
     * Méthode pour gérer les TextFields des seuils
     * @param e
     */
    public void gererTextFieldSeuil(KeyEvent e){
        if( this.TextField_pageModifSeuil.getText().matches("[0-9]*")){
            this.slider_pageModifSeuil.setValue((Double.parseDouble(this.TextField_pageModifSeuil.getText())));
        }
    }

    
    /** 
     * Méthode pour inserer un Seuil
     * @param e
     */
    public void InsererSeuil(ActionEvent e) {
        Seuil s = new Seuil(u.getCompte().getCatPrincipaleParNom(this.CategoriePageModif.getText()), Double.parseDouble(this.TextField_pageModifSeuil.getText()));
        FichierSeuil();
        
        System.out.println("Seuil de "+this.CategoriePageModif.getText()+" = "+s.getMontantSeuil() + " - ajouté");

        this.pageModifSeuil.setVisible(false);
        this.pageModifSeuil.toBack();
        this.listeBord.setEffect(null);
    }

    
    /** 
     * Méthode pour cacher le widget du seuil
     * @param e
     */
    public void retourModifSeuil(MouseEvent e) {
        this.pageModifSeuil.setVisible(false);
        this.pageModifSeuil.toBack();
        this.listeBord.setEffect(null);
    }

    
    /** 
     * Méthode pour fermer le programme
     * @param event
     */
    public void bQuitterOnAction(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        // ferme le programme
    }
    
    /** 
     * Méthode pour rechercher un élément
     * @param obl
     * @param motRecherche
     * @return boolean
     */
    // todo : changer class on tab click
    
    public boolean rechercherElement(ObservableList<String> obl, String motRecherche)
    {
        boolean estDans = false;
        int i = 0;
        while(!(estDans) && i < obl.size())
        {
            if(obl.get(i).equals(motRecherche))
            {
                estDans = true;
            }
            else
            {
                i++;
            }
        }
        return estDans;
    }

    
    /** 
     * Méthode pour afficher le widget pour l'ajout d'une opération
     * @param event
     */
    public void bPlusOnAction(ActionEvent event)
    {
        panePlus.setVisible(true);
    }

    
    /** 
     * Méthode pour afficher le widget pour le profil
     * @param event
     */
    public void bProfilOnAction(ActionEvent event)
    {
        if(paneProfil.isVisible())
        {
            paneProfil.setVisible(false);
        }
        else
        {
            paneProfil.setVisible(true);
            bProfil.setViewOrder(-999);
        }
    }

    
    /** 
     * Méthode pour se déconnecter
     * @param event
     */
    public void bProfilDecoOnAction(ActionEvent event)
    {
        // déconnexion
        try {
            Stage s = new FenAcceuil(); // on repart à l'accueil
            s.setMinWidth(1280);
            s.setMinHeight(720);
            s.show();   
            ((Stage)(((Button)event.getSource()).getScene().getWindow())).close(); // on ferme le main

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
    }

    
    /** 
     * Méthode pour accéder au profil
     * @param event
     * @throws IOException
     */
    public void bProfilAccederOnAction(ActionEvent event) throws IOException
    {
        Stage fen = new FenModifCompte(u.getNomUtilisateur(), u.getDateCreation());// on affiche la page modif compte
        fen.show();

    }
    
    
    /** 
     * Méthode pour accéder aux préférences
     * @param event
     */
    public void bProfilPrefOnAction(ActionEvent event)
    {
        // direction gestion des préférences
        // à faire en dernier
        
    }

    
    /** 
     * Méthode pour cacher le widget d'ajout d'une opération
     * @param event
     */
    public void bPlusRetourOnAction(ActionEvent event)
    {
        panePlus.setVisible(false);
    }

    
    /** 
     * Méthode pour afficher la fenêtre d'ajout d'une opération
     * @param event
     */
    public void bPlusAjouterOpOnAction(ActionEvent event)
    {
        try {
            Stage s = new FenAjoutOp();
            s.setMinWidth(1280);
            s.setMinHeight(720);
            s.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /* ---------------------- TAB TABLEAU DE BORD ---------------------- */

    /**
     * Méthode pour actualiser le diagramme présent sur le tableau de bord
     */
    public void actualiserDiagBord()
    {
        // on vide le diagramme avant de le remplir
        diagTabBord.getData().removeAll(diagTabBord.getData());
        HashMap<?, Double> mapSoldeParCat;
        if(obtenirPeridiciteBordSelectionne().equals("semaine"))
        {
            // méthode semaine
            mapSoldeParCat = u.getCompte().getSoldeParCatSemaineActuelle(obtenirTypeOperationBord());
        }
        else if(obtenirPeridiciteBordSelectionne().equals("mois"))
        {
            // méthode mois
            if(obtenirTypeOperationBord().equals("Revenu"))
            {
                mapSoldeParCat = (HashMap<SousCategorie, Double>) u.getCompte().getSoldeParCatPrincipale(obtenirTypeOperationBord());
            }
            else
            {
                mapSoldeParCat = (HashMap<CategoriePrincipale, Double>) u.getCompte().getSoldeParCatPrincipale(obtenirTypeOperationBord());
            }
        }
        else {
            // méthode année
            if(obtenirTypeOperationBord().equals("Revenu"))
            {
                mapSoldeParCat = (HashMap<SousCategorie, Double>) u.getCompte().getSoldeParCatPrincipale(obtenirTypeOperationBord());
            }
            else
            {
                mapSoldeParCat = (HashMap<CategoriePrincipale, Double>) u.getCompte().getSoldeParCatPrincipale(obtenirTypeOperationBord());
            }
        }   
        System.out.println("---" + obtenirTypeOperationBord() + "---");
        for(Map.Entry<?, Double> m : mapSoldeParCat.entrySet())
        {
            System.out.println(((Categorie) m.getKey()).getNom() + " : " + m.getValue());
            Data d = new PieChart.Data(((Categorie) m.getKey()).getNom() + " : " + m.getValue() + "€", m.getValue());
            diagTabBord.getData().add(d);
            d.getNode().setStyle("-fx-pie-color:" + ((Categorie) m.getKey()).getCouleur());
        }
        diagTabBord.setLegendVisible(false);
    }

    
    /** 
     * Fonction pour connaître le type d'opération sélectionné
     * @return String
     */
    public String obtenirTypeOperationBord()
    {
        String bouton = "";
        if(rechercherElement(bRevenuBord.getStyleClass(), "bSelectionne"))
        {
            bouton = "revenu";
        }
        else if(rechercherElement(bDepenseBord.getStyleClass(), "bSelectionne"))
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
     * Fonction pour connaître la périodicité séléctionné
     * @return String
     */
    public String obtenirPeridiciteBordSelectionne()
    {
        String bouton;
        if(rechercherElement(bSemaineBord.getStyleClass(), "bSelectionne"))
        {
            // il s'agit du bouton sélectionné
            bouton = "semaine";
        }
        else if(rechercherElement(bMoisBord.getStyleClass(), "bSelectionne"))
        {
            bouton = "mois";
        }
        else 
        {
            bouton = "annee";
        }
        return bouton;
    }


    
    /** 
     * Méthode pour passer du mode revenu au mode dépense
     * @param event
     */
    public void bDepenseBordOnAction(ActionEvent event) {
        // bTypz
        bDepenseBord.getStyleClass().remove("bNonSelectionne");
        bDepenseBord.getStyleClass().add("bSelectionne");
        bRevenuBord.getStyleClass().remove("bSelectionne");
        bRevenuBord.getStyleClass().add("bNonSelectionne");
        lInfoTypeOperation.setText("Vos dépenses ce/cette " + obtenirPeridiciteBordSelectionne() + " :");
        actualiserDiagBord();

    }

    
    /** 
     * Méthode pour passer du mode dépense au mode revenu
     * @param event
     */
    public void bRevenuBordOnAction(ActionEvent event) {
        bRevenuBord.getStyleClass().remove("bNonSelectionne");
        bRevenuBord.getStyleClass().add("bSelectionne");
        bDepenseBord.getStyleClass().remove("bSelectionne");
        bDepenseBord.getStyleClass().add("bNonSelectionne");   
        lInfoTypeOperation.setText("Vos revenus ce/cette " + obtenirPeridiciteBordSelectionne() + " :");
        actualiserDiagBord();
    }

    
    /** 
     * Méthode pour passer dans le mode semaine
     * @param event
     */
    public void bSemaineBordOnAction(ActionEvent event) {
        if(rechercherElement(bMoisBord.getStyleClass(), "bSelectionne"))
        {
            bMoisBord.getStyleClass().remove("bSelectionne");
            bMoisBord.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bAnneeBord.getStyleClass(), "bSelectionne"))
        {
            bAnneeBord.getStyleClass().remove("bSelectionne");
            bAnneeBord.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bSemaineBord.getStyleClass(), "bNonSelectionne"))
        {
            // si non sélectionné :
            bSemaineBord.getStyleClass().remove("bNonSelectionne");
            bSemaineBord.getStyleClass().add("bSelectionne");
            lInfoTypeOperation.setText("Vos " + obtenirTypeOperationBord() + "s cette semaine : ");
        }
        actualiserDiagBord();
    }

    
    /** 
     * Méthode pour passer dans le mode mois
     * @param event
     */
    public void bMoisBordOnAction(ActionEvent event) {
        if(rechercherElement(bSemaineBord.getStyleClass(), "bSelectionne"))
        {
            bSemaineBord.getStyleClass().remove("bSelectionne");
            bSemaineBord.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bAnneeBord.getStyleClass(), "bSelectionne"))
        {
            bAnneeBord.getStyleClass().remove("bSelectionne");
            bAnneeBord.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bMoisBord.getStyleClass(), "bNonSelectionne"))
        {
            bMoisBord.getStyleClass().remove("bNonSelectionne");
            bMoisBord.getStyleClass().add("bSelectionne");    
            lInfoTypeOperation.setText("Vos " + obtenirTypeOperationBord() + "s ce mois-ci : ");
        }
        actualiserDiagBord();
    }

    
    /** 
     * Méthode pour passer dans le mode année
     * @param event
     */
    public void bAnneeBordOnAction(ActionEvent event) {
        if(rechercherElement(bSemaineBord.getStyleClass(), "bSelectionne"))
        {
            bSemaineBord.getStyleClass().remove("bSelectionne");
            bSemaineBord.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bMoisBord.getStyleClass(), "bSelectionne"))
        {
            bMoisBord.getStyleClass().remove("bSelectionne");
            bMoisBord.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bAnneeBord.getStyleClass(), "bNonSelectionne"))
        {
            bAnneeBord.getStyleClass().remove("bNonSelectionne");
            bAnneeBord.getStyleClass().add("bSelectionne");    
            lInfoTypeOperation.setText("Vos " + obtenirTypeOperationBord() + " cette année : ");
        }
        actualiserDiagBord();
    }


    
    /** 
     * Méthode pour actualiser le diagramme d'évolution du budget
     * @param annee
     * @param periodicite
     */
    /* ---------------------- TAB EVOLUTION DU BUDGET ---------------------- */
    public void actualiserDiagEvo(int annee, String periodicite) 
    {
        // réinitialisation des données
        diagEvo.setAnimated(false);
        diagEvo.getData().clear();
        diagEvo.layout();
        axeNombre.setLabel("Montant");
        diagEvo.setLegendVisible(true);
        diagEvo.setLegendSide(Side.LEFT);

        // ajout d'une ligne
        for(CategoriePrincipale c : u.getCompte().getListeCategoriePrincipale())
        {
            HashMap<String, Double> map = new HashMap<>();
            // pour chaque catégorie principale avec un type d'opération donné, on récupère le solde pour chaque mois
            if(periodicite.equals("mois"))
            {
                // l'axe des ordonnés = mois
                axeCategorie.setLabel("Mois");
                System.out.println("---- mode sélectionné : mois -----");
                map = u.getCompte().getSoldeCatParMois(2021, c, obtenirTypeOperationEvo());
                XYChart.Series<String, Double> tmp = new XYChart.Series<String, Double>();
                for(int i = 1; i <= 12; i++)
                {
                    String w = "";
                    switch(i)
                    {
                        case 1:
                            // janvier
                            w = "janvier";
                            break;
                        case 2:
                            // février
                            w = "février";
                            break;
                        case 3:
                            // mars
                            w = "mars";
                            break;
                        case 4:
                            // avril
                            w = "avril";
                            break;
                        case 5:
                            w = "mai";
                            // mai
                            break;
                        case 6:
                            w = "juin";
                            // juin
                            break;
                        case 7:
                            w = "juillet";
                            // juillet
                            break;
                        case 8:
                            w = "août";
                            // août
                            break;
                        case 9:
                            w = "septembre";
                            // septembre
                            break;
                        case 10:
                            w = "octobre";
                            // octobre
                            break;
                        case 11:
                            w = "novembre";
                            // novembre
                            break;
                        case 12:
                            w = "décembre";
                            // décembre
                            break;
                    }
                    System.out.println(w + " : " + map.get(String.valueOf(i)));
                    XYChart.Data<String, Double> data = new XYChart.Data<String, Double>(w, map.get(String.valueOf(i)));
                    tmp.getData().add(data);   
                }
                tmp.setName(c.getNom());
                diagEvo.getData().add(tmp);
                /*
                for(XYChart.Data<?,?> d : tmp.getData())
                {
                    d.getNode().setStyle("-fx-bar-fill:" + c.getCouleur());
                }*/
            }
            else if(periodicite.equals("annee"))
            {
                System.out.println("---- mode sélectionné : annee -----");
                // l'axe des ordonnés = mois
                axeCategorie.setLabel("Année");
                map = u.getCompte().getSoldeCatParAnnee(2021, c, obtenirTypeOperationEvo());
                XYChart.Series<String, Double> tmp = new XYChart.Series<String, Double>();
                for(long i = 2021-3; i <= 2021; i++)
                {
                    System.out.println(i + " " + map.get(String.valueOf(i)));
                    tmp.getData().add(new XYChart.Data<String, Double>(String.valueOf(i), map.get(String.valueOf(i))));    
                }
                tmp.setName(c.getNom());
                diagEvo.getData().add(tmp);
                /*for(XYChart.Data<?,?> d : tmp.getData())
                {
                    d.getNode().setStyle("-fx-bar-fill:" + c.getCouleur());
                }*/

            }
            else if(periodicite.equals("semaine"))
            {
                // TODO : methode getSoldeCatParSemaine
                System.out.println("---- mode sélectionné : semaine -----");
                HashMap<Integer, ArrayList<Object>> map2 = u.getCompte().getSoldeCatParSemaine(2021, c, obtenirTypeOperationEvo());
                axeCategorie.setLabel("Semaine");
                XYChart.Series<String, Double> tmp = new XYChart.Series<String, Double>();
                int max = 0;

                for(Map.Entry<Integer, ArrayList<Object>> m : map2.entrySet())
                {
                    if(m.getKey() > max)
                    {
                        max = m.getKey();
                    }
                }
                System.out.println(max);
                ArrayList<Object> t = new ArrayList<>();
                for(int i = 0; i <= max; i++)
                {
                    t = map2.get(i);
                    tmp.getData().add(new XYChart.Data<String, Double>(String.valueOf(t.get(0)), Double.valueOf(t.get(1).toString())));    
                }
                tmp.setName(c.getNom());
                diagEvo.getData().add(tmp);
                /*
                for(XYChart.Data<?,?> d : tmp.getData())
                {
                    d.getNode().setStyle("-fx-bar-fill:" + c.getCouleur());
                }*/

            }
            else
            {
                System.err.println("ici par là Erreur de périodicité dans la génération du diagramme d'évolution");
            }

        }
    }

    
    /** 
     * Fonction pour obtenir le type d'opération dans l'évolution du budget
     * @return String
     */
    public String obtenirTypeOperationEvo()
    {
        String bouton = "";
        if(rechercherElement(bRevenuEvo.getStyleClass(), "bSelectionne"))
        {
            bouton = "revenu";
        }
        else if(rechercherElement(bDepenseEvo.getStyleClass(), "bSelectionne"))
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
     * Fonction pour obtenir la périodicité dans l'évolution du budget
     * @return String
     */
    public String obtenirPeridiciteEvoSelectionne()
    {
        String bouton;
        if(rechercherElement(bSemaineEvo.getStyleClass(), "bSelectionne"))
        {
            // il s'agit du bouton sélectionné
            bouton = "semaine";
        }
        else if(rechercherElement(bMoisEvo.getStyleClass(), "bSelectionne"))
        {
            bouton = "mois";
        }
        else 
        {
            bouton = "annee";
        }
        return bouton;
    }


    
    /** 
     * Méthode pour passer dans le mode dépense dans l'évolution du budget
     * @param event
     */
    public void bDepenseEvoOnAction(ActionEvent event) {
        // bTypz
        bDepenseEvo.getStyleClass().remove("bNonSelectionne");
        bDepenseEvo.getStyleClass().add("bSelectionne");
        bRevenuEvo.getStyleClass().remove("bSelectionne");
        bRevenuEvo.getStyleClass().add("bNonSelectionne");
        actualiserDiagEvo(Calendar.getInstance().get(Calendar.YEAR), obtenirPeridiciteEvoSelectionne());
    }

    
    /** 
     * Méthode pour passer dans le mode revenu dans l'évolution du budget
     * @param event
     */
    public void bRevenuEvoOnAction(ActionEvent event) {
        bRevenuEvo.getStyleClass().remove("bNonSelectionne");
        bRevenuEvo.getStyleClass().add("bSelectionne");
        bDepenseEvo.getStyleClass().remove("bSelectionne");
        bDepenseEvo.getStyleClass().add("bNonSelectionne");   
        actualiserDiagEvo(Calendar.getInstance().get(Calendar.YEAR), obtenirPeridiciteEvoSelectionne());
    }

    
    /** 
     * Méthode pour passer dans le mode semaine dans l'évolution du budget
     * @param event
     */
    public void bSemaineEvoOnAction(ActionEvent event) {
        if(rechercherElement(bMoisEvo.getStyleClass(), "bSelectionne"))
        {
            bMoisEvo.getStyleClass().remove("bSelectionne");
            bMoisEvo.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bAnneeEvo.getStyleClass(), "bSelectionne"))
        {
            bAnneeEvo.getStyleClass().remove("bSelectionne");
            bAnneeEvo.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bSemaineEvo.getStyleClass(), "bNonSelectionne"))
        {
            bSemaineEvo.getStyleClass().remove("bNonSelectionne");
            bSemaineEvo.getStyleClass().add("bSelectionne");
            actualiserDiagEvo(Calendar.getInstance().get(Calendar.YEAR), "semaine");
        }

    }

    
    /** 
     * Méthode pour passer dans le mode mois dans l'évolution du budget
     * @param event
     */
    public void bMoisEvoOnAction(ActionEvent event) {
        if(rechercherElement(bSemaineEvo.getStyleClass(), "bSelectionne"))
        {
            bSemaineEvo.getStyleClass().remove("bSelectionne");
            bSemaineEvo.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bAnneeEvo.getStyleClass(), "bSelectionne"))
        {
            bAnneeEvo.getStyleClass().remove("bSelectionne");
            bAnneeEvo.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bMoisEvo.getStyleClass(), "bNonSelectionne"))
        {
            bMoisEvo.getStyleClass().remove("bNonSelectionne");
            bMoisEvo.getStyleClass().add("bSelectionne");  
            actualiserDiagEvo(Calendar.getInstance().get(Calendar.YEAR), "mois");
  
        }

    }

    
    /** 
     * Méthode pour passer dans le mode année dans l'évolution du budget
     * @param event
     */
    public void bAnneeEvoOnAction(ActionEvent event) {
        if(rechercherElement(bSemaineEvo.getStyleClass(), "bSelectionne"))
        {
            bSemaineEvo.getStyleClass().remove("bSelectionne");
            bSemaineEvo.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bMoisEvo.getStyleClass(), "bSelectionne"))
        {
            bMoisEvo.getStyleClass().remove("bSelectionne");
            bMoisEvo.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bAnneeEvo.getStyleClass(), "bNonSelectionne"))
        {
            bAnneeEvo.getStyleClass().remove("bNonSelectionne");
            bAnneeEvo.getStyleClass().add("bSelectionne");    
            actualiserDiagEvo(Calendar.getInstance().get(Calendar.YEAR), "annee");
        }
    }


    /* ---------------------- GESTION LISTVIEW ----------------------------*/

    /**
     * Cette classe permet de gérer la liste des seuils dans le tableau de bord
     */
    public static class VBoxCell extends VBox {
        // nom catégorie
        Label nomCat = new Label();
        // montanSolde
        Label mSolde = new Label();
        // gestion image V
        Class<?> classe = this.getClass();
        ImageView imgView = new ImageView();
        //gestion des boutons modifier
        static Button buttonModSeuil1 = new Button();
        static Button buttonModSeuil2 = new Button();
        static Button buttonModSeuil3 = new Button();
        static Button buttonModSeuil4 = new Button();
        static Button buttonModSeuil5 = new Button();
        static Button buttonModSeuil6 = new Button();
        static Button buttonModSeuil7 = new Button();
        static Button buttonModSeuil8 = new Button();
        static int i = 0;
        //categorie
        static ArrayList<CategoriePrincipale> categorie = new ArrayList<CategoriePrincipale>();
        // bar seuil
        ProgressBar pb = new ProgressBar(); // 60%

        /**
         * Constructeur d'une VBoxCell
         * @param soldeCat
         * @param c
         */
        VBoxCell(Double sCat, Double sSeuil, CategoriePrincipale c) {
            super();
            
            InputStream input = classe.getResourceAsStream("/images/categories/" + c.getNom().toLowerCase() + ".png");
            System.out.println(c.getNom() + ".png");
            Image img = new Image(input);    
            GridPane gp = new GridPane();
            nomCat.setText(c.getNom());
            mSolde.setText(sCat + "€");

            // taille image
            imgView.setImage(img);
            imgView.setFitHeight(50);
            imgView.setFitWidth(50);

            // gestion progressbar
            pb.setPrefHeight(35);
            pb.setPadding(new Insets(0,10,0,10));
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setStyle("-fx-accent: " + c.getCouleur()); // couleur de la barre
            System.out.println("---------------> " + sCat + " : " + sSeuil);
            pb.setProgress(sCat / sSeuil); // si ça dépasse le seuil on fait quoi ?
            GridPane.setHgrow(pb, Priority.ALWAYS);
            GridPane.setHalignment(pb, HPos.CENTER);

            // taille du solde
            mSolde.setPrefHeight(35);
            mSolde.setPadding(new Insets(0,10,0,0));

            gp.add(imgView, 0, 0);
            gp.add(pb, 1, 0);
            gp.add(mSolde, 2, 0);
            
            i++;
            switch(i){
                case 1 :
                    gp.add(buttonModSeuil1, 3, 0);
                    buttonModSeuil1.setText("Mod");
                    break;
                case 2:
                    gp.add(buttonModSeuil2, 3, 0);
                    buttonModSeuil2.setText("Mod");
                    break;
                case 3:
                    gp.add(buttonModSeuil3, 3, 0);
                    buttonModSeuil3.setText("Mod");
                    break;
                case 4:
                    gp.add(buttonModSeuil4, 3, 0);
                    buttonModSeuil4.setText("Mod");
                    break;
                case 5:
                    gp.add(buttonModSeuil5, 3, 0);
                    buttonModSeuil5.setText("Mod");
                    break;
                case 6:
                    gp.add(buttonModSeuil6, 3, 0);
                    buttonModSeuil6.setText("Mod");
                    break;
                case 7:
                    gp.add(buttonModSeuil7, 3, 0);
                    buttonModSeuil7.setText("Mod");
                    break;
                case 8:
                    gp.add(buttonModSeuil8, 3, 0);
                    buttonModSeuil8.setText("Mod");
                    break;
            }
            
            GridPane.setHalignment(mSolde, HPos.CENTER);
            this.getChildren().addAll(nomCat, gp);

            categorie.add(c);
            
        }

        public static CategoriePrincipale getCatMod(int indice){
            int tmp = indice - 1;
            return categorie.get(tmp);
        }


    }

    
    
    /** 
     * Méthode pour trier selon la semaine
     * @param event
     */
    public void bSemainePerioOnAction(ActionEvent event) {
        if(rechercherElement(bMoisPerio.getStyleClass(), "bSelectionne"))
        {
            bMoisPerio.getStyleClass().remove("bSelectionne");
            bMoisPerio.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bAnneePerio.getStyleClass(), "bSelectionne"))
        {
            bAnneePerio.getStyleClass().remove("bSelectionne");
            bAnneePerio.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bSemainePerio.getStyleClass(), "bNonSelectionne"))
        {
            // si non sélectionné :
            bSemainePerio.getStyleClass().remove("bNonSelectionne");
            bSemainePerio.getStyleClass().add("bSelectionne");
        }
        actualiserDiagPerio(Calendar.getInstance().get(Calendar.YEAR), "semaine");
    }

    
    /** 
     * Méthode pour trier selon le mois
     * @param event
     */
    public void bMoisPerioOnAction(ActionEvent event) {
        if(rechercherElement(bSemainePerio.getStyleClass(), "bSelectionne"))
        {
            bSemainePerio.getStyleClass().remove("bSelectionne");
            bSemainePerio.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bAnneePerio.getStyleClass(), "bSelectionne"))
        {
            bAnneePerio.getStyleClass().remove("bSelectionne");
            bAnneePerio.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bMoisPerio.getStyleClass(), "bNonSelectionne"))
        {
            bMoisPerio.getStyleClass().remove("bNonSelectionne");
            bMoisPerio.getStyleClass().add("bSelectionne");    
        }
        actualiserDiagPerio(Calendar.getInstance().get(Calendar.YEAR), "mois");
    }

    
    /** 
     * Méthode pour trier selon l'année
     * @param event
     */
    public void bAnneePerioOnAction(ActionEvent event) {
        if(rechercherElement(bSemainePerio.getStyleClass(), "bSelectionne"))
        {
            bSemainePerio.getStyleClass().remove("bSelectionne");
            bSemainePerio.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bMoisPerio.getStyleClass(), "bSelectionne"))
        {
            bMoisPerio.getStyleClass().remove("bSelectionne");
            bMoisPerio.getStyleClass().add("bNonSelectionne");    
        }
        if(rechercherElement(bAnneePerio.getStyleClass(), "bNonSelectionne"))
        {
            bAnneePerio.getStyleClass().remove("bNonSelectionne");
            bAnneePerio.getStyleClass().add("bSelectionne");    
        }
        actualiserDiagPerio(Calendar.getInstance().get(Calendar.YEAR), "annee");
    }

    
    /** 
     * 
     * @param annee
     * @param periodicite
     */
    public void actualiserDiagPerio(int annee, String periodicite) 
    {
        // réinitialisation des données
        diagPerio.setAnimated(false);
        diagPerio.getData().clear();
        diagPerio.layout();
        axeNumPerio.setLabel("Montant");
        diagPerio.setLegendVisible(false);

        // ajout d'une ligne
            HashMap<String, Number> mapDepense = new HashMap<>();
            HashMap<String, Number> mapRevenu = new HashMap<>();
            // pour chaque catégorie principale avec un type d'opération donné, on récupère le solde pour chaque mois
            if(periodicite.equals("mois"))
            {
                // l'axe des ordonnés = mois
                axeCatPerio.setLabel("Mois");
                System.out.println("---- mode sélectionné : mois -----");
                mapDepense = u.getCompte().getSoldeParMois(2021, "dépense");
                mapRevenu = u.getCompte().getSoldeParMois(2021, "revenu");
                XYChart.Series<String, Number> seriesRevenu = new XYChart.Series<String, Number>();
                XYChart.Series<String, Number> seriesDepense = new XYChart.Series<String, Number>();
                for(int i = 1; i <= 12; i++)
                {
                    String w = "";
                    switch(i)
                    {
                        case 1:
                            // janvier
                            w = "janvier";
                            break;
                        case 2:
                            // février
                            w = "février";
                            break;
                        case 3:
                            // mars
                            w = "mars";
                            break;
                        case 4:
                            // avril
                            w = "avril";
                            break;
                        case 5:
                            w = "mai";
                            // mai
                            break;
                        case 6:
                            w = "juin";
                            // juin
                            break;
                        case 7:
                            w = "juillet";
                            // juillet
                            break;
                        case 8:
                            w = "août";
                            // août
                            break;
                        case 9:
                            w = "septembre";
                            // septembre
                            break;
                        case 10:
                            w = "octobre";
                            // octobre
                            break;
                        case 11:
                            w = "novembre";
                            // novembre
                            break;
                        case 12:
                            w = "décembre";
                            // décembre
                            break;
                    }
                    System.out.println("dépense : " + w + " : " + - (mapDepense.get(String.valueOf(i)).doubleValue()));
                    System.out.println("revenu : " + w + " : " + mapRevenu.get(String.valueOf(i)));
                    seriesDepense.getData().add(new XYChart.Data<String, Number>(w, - (mapDepense.get(String.valueOf(i)).doubleValue())));    
                    seriesRevenu.getData().add(new XYChart.Data<String, Number>(w, mapRevenu.get(String.valueOf(i))));    
                }
                diagPerio.getData().add(seriesDepense);
                diagPerio.getData().add(seriesRevenu);
            }
            else if(periodicite.equals("annee"))
            {
                System.out.println("---- mode sélectionné : annee -----");
                // l'axe des ordonnés = mois
                axeCatPerio.setLabel("Année");
                mapDepense = u.getCompte().getSoldeParAnnee(2021, "dépense");
                mapRevenu = u.getCompte().getSoldeParAnnee(2021, "revenu");
                XYChart.Series<String, Number> tmp = new XYChart.Series<String, Number>();
                for(long i = 2021-3; i <= 2021; i++)
                {
                    System.out.println("dépense : " + i + " " + mapDepense.get(String.valueOf(i)));
                    System.out.println("revenu : " + i + " " + mapDepense.get(String.valueOf(i)));
                    tmp.getData().add(new XYChart.Data<String, Number>(String.valueOf(i), - (mapDepense.get(String.valueOf(i)).doubleValue())));    
                    tmp.getData().add(new XYChart.Data<String, Number>(String.valueOf(i), (mapRevenu.get(String.valueOf(i)).doubleValue())));    
                }
                diagPerio.getData().add(tmp);
            }
            else if(periodicite.equals("semaine"))
            {
                System.out.println("---- mode sélectionné : semaine -----");
                HashMap<Integer, ArrayList<Object>> mapDepense2 = u.getCompte().getSoldeParSemaine(2021, "dépense");
                HashMap<Integer, ArrayList<Object>> mapRevenu2 = u.getCompte().getSoldeParSemaine(2021, "revenu");
                axeCatPerio.setLabel("Semaine");
                XYChart.Series<String, Number> tmp = new XYChart.Series<String, Number>();
                int max = 0;

                for(Map.Entry<Integer, ArrayList<Object>> m : mapDepense2.entrySet())
                {
                    if(m.getKey() > max)
                    {
                        max = m.getKey();
                    }
                }
                System.out.println(max);
                ArrayList<Object> t = new ArrayList<>();
                ArrayList<Object> t2 = new ArrayList<>();
                for(int i = 0; i <= max; i++)
                {
                    t = mapDepense2.get(i);
                    t2 = mapRevenu2.get(i);
                    tmp.getData().add(new XYChart.Data<String, Number>(String.valueOf(t.get(0)), - Double.valueOf(t.get(1).toString())));    
                    tmp.getData().add(new XYChart.Data<String, Number>(String.valueOf(t2.get(0)), Double.valueOf(t2.get(1).toString())));    
                }

                diagPerio.getData().add(tmp);
            }
            else
            {
                System.err.println("ici Erreur de périodicité dans la génération du diagramme d'évolution");
            }

    }


    
    /** 
     * @return String
     */
    public String obtenirPeridicitePerioSelectionne()
    {
        String bouton;
        if(rechercherElement(bSemainePerio.getStyleClass(), "bSelectionne"))
        {
            // il s'agit du bouton sélectionné
            bouton = "semaine";
        }
        else if(rechercherElement(bMoisPerio.getStyleClass(), "bSelectionne"))
        {
            bouton = "mois";
        }
        else 
        {
            bouton = "annee";
        }
        return bouton;
    }

    /* ------------------------------------ TAB MES TRANSACTIONS  ----------------------------------*/ 

    public void remplirTabTransaction()
    {
        listViewTransactions.getItems().clear();
        List<VBox> listeT = new ArrayList<VBox>();
        int i;
        montantSolde1.setText(" " + Utilisateur.getInstance().getCompte().getSolde());
        ArrayList<OperationPonctuelle> listeOperations = u.getCompte().getListeOperation();
        ArrayList<Date> listeDates = u.getCompte().getDates();
        for (i=0; i<listeDates.size(); i++){
            VBox vbox = new VBox();

            String jour = new String();
            String [] mois = {"Janvier","Fevrier","Mars","Avril","Mai","Juin","Juiller","Aout","Septembre","Octobre","Novembre","Decembre"};
            if (listeDates.get(i).getDate()<10){
                jour = "0"+listeDates.get(i).getDate();
            }else{
                jour = ""+ listeDates.get(i).getDate();
            }
            Label date = new Label(jour+" "+mois[listeDates.get(i).getMonth()]+" "+(listeDates.get(i).getYear()+1900)/*listeDates.get(i).toString()*/);
            date.setFont(new Font("System",25));
            //Label date = new Label (listeDates.get(i).toString());
            vbox.getChildren().add(date);
            listeT.add(vbox);

            List<HBox> listOp = new ArrayList<HBox>();
            int j;
            for (j=0; j< listeOperations.size(); j++){
                if (listeOperations.get(j).getDateOp().equals(listeDates.get(i))){
                    /*TextField operation = new TextField(listeOperations.get(j).toString());
                    listOp.add(operation);*/

                    HBox op = new HBox();
                    Label libOp = new Label(listeOperations.get(j).getNom());
                    libOp.setFont(new Font("System",20));
                    libOp.setAlignment(Pos.CENTER_LEFT);
                    Label montantOp = new Label(listeOperations.get(j).getMontantOp().toString());
                    if (listeOperations.get(j).getTypeOperation().equals("revenu")){
                        montantOp = new Label("+"+montantOp.getText());
                        montantOp.setTextFill(Color.GREEN);
                    }else if (listeOperations.get(j).getTypeOperation().equals("dépense")){
                        montantOp = new Label("-"+montantOp.getText());
                        montantOp.setTextFill(Color.RED);
                    }
                    montantOp.setFont(new Font("System",20));
                    montantOp.setAlignment(Pos.CENTER_RIGHT);
                    Region reg = new Region();

                    Class clas = this.getClass();
                    InputStream inp = clas.getResourceAsStream("/images/categories/"+listeOperations.get(j).getCategorie().getNom()+".png");
                    
                    try {
                        Image img = new Image(inp);
                        
                        ImageView imgv = new ImageView(img);
                        imgv.setFitHeight(20);
                        imgv.setPreserveRatio(true);
                        op.getChildren().add(imgv);
                        
                    }catch (Exception e){
                        System.out.println("ca marche pas");
                    }

                    op.getChildren().add(libOp);
                    op.getChildren().add(reg);
                    op.getChildren().add(montantOp);
                    op.setHgrow(reg, Priority.ALWAYS);
                    
                    listOp.add(op);
                        
                    System.out.println("Ajout pour i="+i+" et j="+j);
                }
                
            }
            ListView<HBox> listOpV = new ListView<HBox>();
            ObservableList<HBox> listOb = FXCollections.observableList(listOp);
            listOpV.setItems(listOb);
            listOpV.setPrefHeight(listOb.size()*36.5 +2);
            
            
            vbox.getChildren().add(listOpV);
            //listOpV.setPrefHeight(listOp.size() * ROW_HEIGHT + 2);
            
        }
        this.listViewTransactions.setItems(FXCollections.observableList(listeT));

    }


}
