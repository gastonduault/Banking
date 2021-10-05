package fr.fleyg.banking;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cette classe définie un utilisateur
 */
public class Utilisateur {

    // variable de classe
    private static Utilisateur instance;

    // variable d'instance
    private String nomUtilisateur;
    private String nom;
    private String prenom;
    private String mdp;
    private Date dateCreation;
    private String periodiciteParDefaut;
    private boolean modeSombre;
    private Compte compte;

    /**
     * Constructeur d'un utilisateur
     * @param nomUtilisateur
     * @param nom
     * @param prenom
     * @param mdp
     */
    public Utilisateur(String nomUtilisateur, String nom, String prenom, String mdp) {
        this.nomUtilisateur = nomUtilisateur;
        this.prenom = prenom;
        this.nom = nom;
        this.mdp = mdp;
        this.compte = new Compte(0.0); // le solde de l'utilisateur est initialisé à 0.
        this.dateCreation = new Date(System.currentTimeMillis()); // temps en milliseconde, sera convertie avec un SimpleDateFormat
        this.periodiciteParDefaut = "année";
        this.modeSombre = false; // mode sombre désactivé de base
    }

    

    
    /** 
     * Getter d'un prénom
     * @return String
     */
    public String getPrenom()
    {
        return this.prenom;
    }
    
    /**
     * Méthode pour enlever une instance
     */
    public static void removeInstance()
    {
        instance = null;
    }

    
    /** 
     * Getter d'une instance
     * @return Utilisateur
     */
    public static Utilisateur getInstance()
    {
        return instance;
    }

    
    /** 
     * Setter d'une instance
     * @param nomUtilisateur
     * @param mdp
     */
    public static void setInstance(String nomUtilisateur, String mdp)
    {
        System.out.println();
        File test = new File("Banking/src/main/resources/Data/" + nomUtilisateur);
        System.out.println(test.getAbsolutePath());
        if(test.isFile()) // si le fichier existe...
        {

            // on créer l'instance d'utilisateur
            try {
                BufferedReader br = new BufferedReader(new FileReader(test));
                // StringBuffer sb  = new StringBuffer();
                String nom = br.readLine(); // chaque ligne correspond à une info
                String prenom = br.readLine();
                String nomUtil = br.readLine();
                String pass = br.readLine();
                if(pass.equals(mdp))
                {
                    // si le mot de passe correspond, on fait une instance
                    instance = new Utilisateur(nomUtil, nom, prenom, pass);

                    // MAINTENANT ON INSERE TOUTES LES OPERATIONS
                    String ligne = br.readLine();
                    while(ligne != null)
                    {
                        String elem[] = ligne.split("\\|");
                        // TODO : CAS OPERATION RECURRENTE
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Utilisateur.getInstance().getCompte().ajouterOperation(new OperationPonctuelle(elem[0], elem[1], Double.valueOf(elem[2]), sdf.parse(elem[3]), Utilisateur.getInstance().getCompte().getSousCatParNom(elem[4]), false));
                        ligne = br.readLine();
                    }
                    System.out.println("Utilisateur instancié");
                    for(OperationPonctuelle op : Utilisateur.getInstance().getCompte().getListeOperation())
                    {
                        System.out.println("- " + op.getNom());
                    }

                    // MAINTENANT TOUS LES SEUILS
                    File fileSeuil = new File("Banking/src/main/resources/Data/" + nomUtilisateur +"Seuil");
                    if(fileSeuil.exists())
                    {
                        BufferedReader br2 = new BufferedReader(new FileReader(fileSeuil));
                        ligne = br2.readLine();
                        Seuil s;
                        String elem[] = ligne.split("\\-");
                        while (ligne != null)
                        {
                            s = new Seuil(Utilisateur.getInstance().getCompte().getCatPrincipaleParNom(elem[0]), Double.parseDouble(elem[1]));
                            Utilisateur.getInstance().getCompte().ajouterSeuil(s); // ajout du seuil    
                            ligne = br2.readLine();
                            if(ligne != null)
                            {
                                elem = ligne.split("\\-");
                            }
                        }
                    }
                    else
                    {
                    }
                }
                else
                {
                    System.out.println("Utilisateur correcte mais mdp non reconnu");
                }    
                br.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
 
        }
        else
        {
            System.out.println("Utilisateur non reconnu");
        }

    }

    
    /** 
     * Méthode pour déterminer la force d'un mot de passe
     * @param mdp
     * @return int
     */
    // méthode statique
    public static int forceMdp(String mdp)
    {
        // TODO : déterminer force des mdp
        return 0;
    }

    
    /** 
     * Méthode pour changer de mot de passe
     * @param nouveauMdp
     */
    // TODO : verifierNomUtilisateur() & rechercheUtilisateur()

    // méthode d'instance
    public void changerMdp(String nouveauMdp)
    {
        this.mdp = nouveauMdp;
    }

    
    /** 
     * Getter d'un nom d'utilisateur
     * @return String
     */
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    
    /** 
     * Getter d'un nom
     * @return String
     */
    // pas de setter pour le nom d'utilisateur, pas modifiable

    public String getNom() {
        return nom;
    }

    
    /** 
     * Setter d'un nom
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    
    /** 
     * Getter de la date de création
     * @return Date
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    
    /** 
     * Setter de la date de création
     * @param dateCreation
     */
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    
    /** 
     * Getter de la périodicité par défaut
     * @return String
     */
    public String getPeriodiciteParDefaut() {
        return periodiciteParDefaut;
    }

    
    /** 
     * Setter de la périodicité par défaut
     * @param periodiciteParDefaut
     */
    public void setPeriodiciteParDefaut(String periodiciteParDefaut) {
        this.periodiciteParDefaut = periodiciteParDefaut;
    }

    
    /** 
     * Fonction pour savoir le mode sombre
     * @return boolean
     */
    public boolean isModeSombre() {
        return modeSombre;
    }

    
    /** 
     * Setter pour le mode sombre
     * @param modeSombre
     */
    public void setModeSombre(boolean modeSombre) {
        this.modeSombre = modeSombre;
    }

    
    /** 
     * Getter du compte
     * @return Compte
     */
    public Compte getCompte() {
        return compte;
    }

    
    /** 
     * Setter du compte
     * @param compte
     */
    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    

    
}
