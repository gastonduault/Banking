package fr.fleyg.banking;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.nio.file.*;

/**
 * La classe Compte permet de gérer les comptes
 */
public class Compte {
    private Double solde;
    private ArrayList<CategoriePrincipale> listeCategoriePrincipale;
    private ArrayList<OperationPonctuelle> listeOperationPonc;
    private ArrayList<Seuil> listeSeuil;
    // les catégories principales contiennent des sous catégories

    /**
     * Constructeur de Comtpe
     * @param solde
     */
    public Compte(Double solde) {
        this.solde = solde;
        this.listeCategoriePrincipale = new ArrayList<CategoriePrincipale>();
        this.listeSeuil = new ArrayList<Seuil>();
        this.listeCategoriePrincipale = genererCatPrincipale(); // on initialise les catégories principales
        this.listeOperationPonc = new ArrayList<>();

        //il faut qu'à la création du compte, l'utilisateur est déjà toutes les catégories principales de prêtes
    }

    public Seuil getSeuilParCat(CategoriePrincipale cp)
    {
        boolean trouve = false;
        int i = 0;
        Seuil s = null; 
        while(!trouve && i < listeSeuil.size())
        {
            if(listeSeuil.get(i).getCategorie().getNom().equals(cp.getNom()))
            {
                s = listeSeuil.get(i);
                trouve = true;
            }
            else
            {
                i++;
            }
        }
        return s; 
    }

    public void ajouterSeuil(Seuil s)
    {
        this.listeSeuil.add(s);
        System.out.println("seuil " + s.getMontantSeuil() + " pour la catégorie " + s.getCategorie().getNom());
    }

    public ArrayList<Seuil> getListeSeuil()
    {
        return this.listeSeuil;
    }
    
    
    /** 
     * Méthode pour connaître la catégorie Principale
     * @param nom
     * @return CategoriePrincipale
     */
    public CategoriePrincipale getCatPrincipaleParNom(String nom)
    {
        CategoriePrincipale cp = null;
        for(CategoriePrincipale c : listeCategoriePrincipale)
        {
            if(c.getNom().equals(nom))
            {
                cp = c;
            }
        }
        return cp;
    }

    
    /** 
     * Fonction pour connaître le solde de la semaine
     * @param annee
     * @param typeOp
     * @return HashMap<Integer, ArrayList<Object>>
     */
    public HashMap<Integer, ArrayList<Object>> getSoldeParSemaine(int annee, String typeOp)
    {
        HashMap<Integer, ArrayList<Object>> mapSoldeParSemaine = new HashMap<>();

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date d = sdf.parse("01-01-"+annee);

            Double solde = 0.0;
            int i = 0;

            Calendar calDebut = Calendar.getInstance();
            Calendar calFinSem = Calendar.getInstance();
            Calendar calTmp = Calendar.getInstance();

            calDebut.setTime(d);

            calFinSem.setTime(d);
            
            // dimanche -> 1, lundi -> 2, ..., samedi -> 7
            while(calFinSem.get(Calendar.DAY_OF_WEEK) != 1)
            {
                // dimanche = fin de semaine
                calFinSem.add(Calendar.DATE, 1);
            }
            
            // on récupère le solde de chaque opération étant entre le début et la fin de la semaine recherchée
            for(OperationPonctuelle op : listeOperationPonc)
            {
                calTmp.setTime(op.getDateOp()); // on met la date de l'opération dans calTmp
                if((calTmp.before(calFinSem) || calTmp.equals(calFinSem)) && (calTmp.after(calDebut) || calTmp.equals(calDebut)) && op.getTypeOperation().equals(typeOp))
                {
                    solde += op.getMontantOp();
                }
            }
            ArrayList<Object> tmp = new ArrayList<>();
            tmp.add(calDebut.get(Calendar.DAY_OF_MONTH) + "/" + (calDebut.get(Calendar.MONTH)+1) + " au " + calFinSem.get(Calendar.DAY_OF_MONTH) + "/" + (calFinSem.get(Calendar.MONTH)+1));
            tmp.add(solde);
            mapSoldeParSemaine.put(i, tmp);
            i++;

            while(calDebut.get(Calendar.DAY_OF_WEEK) != 2) // de lundi
            {
                // on met le jour de début au lundi d'après
                calDebut.add(Calendar.DATE, 1);
            }
            calFinSem.add(Calendar.DATE, 7); // du dimanche au dimanche suivant

            // initialisation condition de fin
            int jourDeFin;
            if(annee == Calendar.getInstance().get(Calendar.YEAR))
            {
                // si année courante, on parcourt jusqu'au jour courrant
                jourDeFin = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            }
            else
            {
                // sinon on parcours toute l'année
                if(annee % 400 == 0)
                {
                    // année bissextile
                    jourDeFin = 366;
                }
                else
                {
                    jourDeFin = 365;
                }
            }

            // on rentre dans la boucle
            while(calFinSem.get(Calendar.DAY_OF_YEAR) <= jourDeFin)
            {
                // tant qu'on n'a pas parcouru toute l'année
                solde = 0.0;
                for(OperationPonctuelle op : listeOperationPonc)
                {
                    calTmp.setTime(op.getDateOp());
                    if((calTmp.before(calFinSem) || calTmp.equals(calFinSem)) && (calTmp.after(calDebut) || calTmp.equals(calDebut)) && op.getTypeOperation().equals(typeOp))
                    {
                        solde += op.getMontantOp();
                    }
                }
                // System.out.println("Solde pour la semaine du " + calDebut.get(Calendar.DAY_OF_MONTH) + "/" + (calDebut.get(Calendar.MONTH)+1) + "/" + calDebut.get(Calendar.YEAR) + " au " + calFinSem.get(Calendar.DAY_OF_MONTH) + "/" + (calFinSem.get(Calendar.MONTH)+1) + "/" + calFinSem.get(Calendar.YEAR) + " : " + solde);
                ArrayList<Object> tmp2 = new ArrayList<>();
                tmp2.add(calDebut.get(Calendar.DAY_OF_MONTH) + "/" + calDebut.get(Calendar.MONTH) + " au " + calFinSem.get(Calendar.DAY_OF_MONTH) + "/" + calFinSem.get(Calendar.MONTH));
                tmp2.add(solde);    
                mapSoldeParSemaine.put(i, tmp2);
                i++;
                    
                if(calFinSem.get(Calendar.DAY_OF_YEAR) + 7 < jourDeFin)
                {
                    // on avance d'une semaine
                    calDebut.add(Calendar.DATE, 7);
                    calFinSem.add(Calendar.DATE, 7);     
                }
                else if(calFinSem.get(Calendar.DAY_OF_YEAR) != jourDeFin)
                {
                    // on fixe la date de fin au jour actuel
                    calDebut.add(Calendar.DATE, 7);
                    calFinSem.setTime(Calendar.getInstance().getTime());
                }
                else
                {
                    // on fait dépasser volontairement la date de fin pour sortir de la boucle
                    calFinSem.add(Calendar.DATE, 1);     
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return mapSoldeParSemaine;

    }

    
    /** 
     * Fonction pour connaître le solde par année
     * @param anneeMax
     * @param typeOp
     * @return HashMap<String, Number>
     */
    public HashMap<String, Number> getSoldeParAnnee(int anneeMax, String typeOp)
    {
        HashMap<String, Number> mapSoldeParAnnee = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        Number solde = 0.0;
        long i;

        for(i = anneeMax-3; i <= anneeMax; i++) // les 3 dernières années précédents anneeMax
        {
            solde = 0.0;
            for(OperationPonctuelle op : listeOperationPonc)
            {
                cal.setTime(op.getDateOp());
                if(cal.get(Calendar.YEAR) == i && typeOp.equals(op.getTypeOperation())) // annee, catégorie et type correspondant
                {
                    solde = solde.doubleValue() + op.getMontantOp();
                }
            }
            mapSoldeParAnnee.put(String.valueOf(i), solde);

        }
        return mapSoldeParAnnee;

        // TODO
    }

    
    /** 
     * Fonction pour connaître le solde par mois
     * @param annee
     * @param typeOp
     * @return HashMap<String, Number>
     */
    public HashMap<String, Number> getSoldeParMois(int annee, String typeOp)
    {
        HashMap<String, Number> mapSoldeParMois = new HashMap<>(); // clé -> mois, double -> solde pour un mois
        Calendar cal = Calendar.getInstance();
        Number solde = 0.0;
        for(int i = 1; i <= 12; i++)
        {
            solde = 0.0;
            for(OperationPonctuelle op : listeOperationPonc)
            {
                cal.setTime(op.getDateOp());
                if(cal.get(Calendar.MONTH)+1 == i && cal.get(Calendar.YEAR) == annee && typeOp.equals(op.getTypeOperation())) // annee, mois, catégorie et type correspondant
                {

                    solde = solde.doubleValue() + op.getMontantOp();
                }
            }
            mapSoldeParMois.put(String.valueOf(i), solde); // mois en int & solde

        }
        return mapSoldeParMois;

    }
    
    /** 
     * Fonction pour connaître le solde par semaine selon une catégorie
     * @param annee
     * @param cat
     * @param typeOp
     * @return HashMap<Integer, ArrayList<Object>>
     */
    public HashMap<Integer, ArrayList<Object>> getSoldeCatParSemaine(int annee, CategoriePrincipale cat, String typeOp) 
    {
        HashMap<Integer, ArrayList<Object>> mapSoldeParSemaine = new HashMap<>();
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date d = sdf.parse("01-01-"+annee);

            Double solde = 0.0;
            int i = 0;

            Calendar calDebut = Calendar.getInstance();
            Calendar calFinSem = Calendar.getInstance();
            Calendar calTmp = Calendar.getInstance();

            calDebut.setTime(d);

            calFinSem.setTime(d);
            
            // dimanche -> 1, lundi -> 2, ..., samedi -> 7
            while(calFinSem.get(Calendar.DAY_OF_WEEK) != 1)
            {
                // dimanche = fin de semaine
                calFinSem.add(Calendar.DATE, 1);
            }
            
            for(OperationPonctuelle op : listeOperationPonc)
            {
                calTmp.setTime(op.getDateOp());
                if((calTmp.before(calFinSem) || calTmp.equals(calFinSem)) && (calTmp.after(calDebut) || calTmp.equals(calDebut)) && op.getTypeOperation().equals(typeOp) && cat.estDans(op.getCategorie()))
                {
                    solde += op.getMontantOp();
                }
            }
            ArrayList<Object> tmp = new ArrayList<>();
            tmp.add(calDebut.get(Calendar.DAY_OF_MONTH) + "/" + (calDebut.get(Calendar.MONTH)+1) + " au " + calFinSem.get(Calendar.DAY_OF_MONTH) + "/" + (calFinSem.get(Calendar.MONTH)+1));
            tmp.add(solde);
            mapSoldeParSemaine.put(i, tmp);
            i++;

            while(calDebut.get(Calendar.DAY_OF_WEEK) != 2) // de lundi
            {
                // on met le jour de début au lundi d'après
                calDebut.add(Calendar.DATE, 1);
            }
            calFinSem.add(Calendar.DATE, 7); // du dimanche au dimanche suivant

            // initialisation condition de fin
            int jourDeFin;
            if(annee == Calendar.getInstance().get(Calendar.YEAR))
            {
                // si année courante, on parcourt jusqu'au jour courrant
                jourDeFin = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            }
            else
            {
                // sinon on parcours toute l'année
                if(annee % 400 == 0)
                {
                    // année bissextile
                    jourDeFin = 366;
                }
                else
                {
                    jourDeFin = 365;
                }
            }

            // on rentre dans la boucle
            while(calFinSem.get(Calendar.DAY_OF_YEAR) <= jourDeFin)
            {
                // tant qu'on n'a pas parcouru toute l'année
                solde = 0.0;
                for(OperationPonctuelle op : listeOperationPonc)
                {
                    calTmp.setTime(op.getDateOp());
                    if((calTmp.before(calFinSem) || calTmp.equals(calFinSem)) && (calTmp.after(calDebut) || calTmp.equals(calDebut)) && op.getTypeOperation().equals(typeOp) && cat.estDans(op.getCategorie()))
                    {
                        solde += op.getMontantOp();
                    }
                }
                // System.out.println("Solde pour la semaine du " + calDebut.get(Calendar.DAY_OF_MONTH) + "/" + (calDebut.get(Calendar.MONTH)+1) + "/" + calDebut.get(Calendar.YEAR) + " au " + calFinSem.get(Calendar.DAY_OF_MONTH) + "/" + (calFinSem.get(Calendar.MONTH)+1) + "/" + calFinSem.get(Calendar.YEAR) + " : " + solde);
                ArrayList<Object> tmp2 = new ArrayList<>();
                tmp2.add(calDebut.get(Calendar.DAY_OF_MONTH) + "/" + calDebut.get(Calendar.MONTH) + " au " + calFinSem.get(Calendar.DAY_OF_MONTH) + "/" + calFinSem.get(Calendar.MONTH));
                tmp2.add(solde);    
                mapSoldeParSemaine.put(i, tmp2);
                i++;
                    
                if(calFinSem.get(Calendar.DAY_OF_YEAR) + 7 < jourDeFin)
                {
                    // on avance d'une semaine
                    calDebut.add(Calendar.DATE, 7);
                    calFinSem.add(Calendar.DATE, 7);     
                }
                else if(calFinSem.get(Calendar.DAY_OF_YEAR) != jourDeFin)
                {
                    // on fixe la date de fin au jour actuel
                    calDebut.add(Calendar.DATE, 7);
                    calFinSem.setTime(Calendar.getInstance().getTime());
                }
                else
                {
                    // on fait dépasser volontairement la date de fin pour sortir de la boucle
                    calFinSem.add(Calendar.DATE, 1);     
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return mapSoldeParSemaine;
    }

    
    /** 
     * Fonction pour connaître le solde par année selon une catégorie
     * @param anneeMax
     * @param cat
     * @param typeOp
     * @return HashMap<String, Double>
     */
    public HashMap<String, Double> getSoldeCatParAnnee(int anneeMax, CategoriePrincipale cat, String typeOp)
    {
        HashMap<String, Double> mapSoldeParAnnee = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        Double solde = 0.0;
        long i;

        for(i = anneeMax-3; i <= anneeMax; i++) // les 3 dernières années précédents anneeMax
        {
            solde = 0.0;
            for(OperationPonctuelle op : listeOperationPonc)
            {
                cal.setTime(op.getDateOp());
                if(cal.get(Calendar.YEAR) == i && cat.estDans(op.getCategorie()) && typeOp.equals(op.getTypeOperation())) // annee, catégorie et type correspondant
                {
                    solde += op.getMontantOp();
                }
            }
            mapSoldeParAnnee.put(String.valueOf(i), solde);

        }
        return mapSoldeParAnnee;

        // TODO
    }


    
    /** 
     * Fonction pour connaître le solde par mois selon une catégorie
     * @param annee
     * @param cat
     * @param typeOp
     * @return HashMap<String, Double>
     */
    public HashMap<String, Double> getSoldeCatParMois(int annee, CategoriePrincipale cat, String typeOp)
    {
        HashMap<String, Double> mapSoldeParMois = new HashMap<>(); // clé -> mois, double -> solde pour un mois
        Calendar cal = Calendar.getInstance();
        Double solde = 0.0;
        for(int i = 1; i <= 12; i++)
        {
            solde = 0.0;
            for(OperationPonctuelle op : listeOperationPonc)
            {
                cal.setTime(op.getDateOp());
                if(cal.get(Calendar.MONTH)+1 == i && cal.get(Calendar.YEAR) == annee && cat.estDans(op.getCategorie()) && typeOp.equals(op.getTypeOperation())) // annee, mois, catégorie et type correspondant
                {

                    solde += op.getMontantOp();
                }
            }
            mapSoldeParMois.put(String.valueOf(i), solde); // mois en int & solde

        }
        return mapSoldeParMois;

    }

    
    /** 
     * Fonction pour connaître le solde par semaine actuelle selon une catégorie
     * @param typeOperation
     * @return HashMap<CategoriePrincipale, Double>
     */
    // prend la semaine actuelle comme référence
    public HashMap<CategoriePrincipale, Double> getSoldeParCatSemaineActuelle(String typeOperation)
    {
        HashMap<CategoriePrincipale, Double> mapSoldeParCatParSemaine = new HashMap<>();
        Double soldeCat = 0.0;
        Calendar cal = Calendar.getInstance();
        Calendar calDebut = Calendar.getInstance();
        Calendar calTmp = Calendar.getInstance();
        cal.getTime(); 
        calDebut.getTime();
        while(calDebut.get(Calendar.DAY_OF_WEEK) != 2) // tant qu'on n'est pas à lundi
        {
            calDebut.add(Calendar.DATE, -1); // on recule d'un jour jusqu'à être à lundi
        }

        for(CategoriePrincipale c : listeCategoriePrincipale)
        {
            soldeCat = 0.0;
            for(OperationPonctuelle o : listeOperationPonc)
            {
                calTmp.setTime(o.getDateOp());
                if((calTmp.before(cal) || calTmp.equals(cal)) && (calTmp.after(calDebut) || calTmp.equals(calDebut)) && o.getTypeOperation().equals(typeOperation) && c.estDans(o.getCategorie()))
                {
                    // si l'opération est compris dans l'intervalle, qui correspond au type d'opération recherché et que la catégorie est bonne
                    soldeCat += o.getMontantOp();
                }
            }
            mapSoldeParCatParSemaine.put(c, soldeCat);
        }
        return mapSoldeParCatParSemaine;
    }

    public Double getSoldePourUneCatPrincipale(CategoriePrincipale cp)
    {
        Double montant = 0.0;
        for(Operation o : listeOperationPonc)
        {
            if(cp.estDans(o.getCategorie()))
            {
                montant += o.getMontantOp();
            }
        }
        return montant;
    }

    
    /** 
     * Fonction de récupération de solde pour chaque catégorie
     * @param typeOperation
     * @return HashMap<CategoriePrincipale, Double>
     */
    // algo de récupération de solde pour chaque catégorie
    public HashMap<?, Double> getSoldeParCatPrincipale(String typeOperation)
    {
        HashMap<?, Double> f;
        Double soldeCat = 0.0;
        if(typeOperation.equals("revenu"))
        {
            HashMap<SousCategorie, Double> mapSoldeParCat = new HashMap<>();
            for(SousCategorie sc : this.getCatPrincipaleParNom("Revenu").getlSousCat())
            {
                soldeCat = 0.0;
                for(Operation o : listeOperationPonc)
                {

                    if(sc.getNom().equals(o.getCategorie().getNom()) && typeOperation.equals(o.getTypeOperation()))
                    {
                        soldeCat += o.getMontantOp();
                    }
                }
                mapSoldeParCat.put(sc, soldeCat);
            }
            f = mapSoldeParCat;
        }
        else
        {
            HashMap<CategoriePrincipale, Double> mapSoldeParCat = new HashMap<>();
            for(CategoriePrincipale c : listeCategoriePrincipale)
            {
                soldeCat = 0.0;
                for(Operation o : listeOperationPonc)
                {
                    if(c.estDans(o.getCategorie()) & typeOperation.equals(o.getTypeOperation()))
                    {
                        // si la sous catégorie de l'opération est dans la catégorie et que 
                        // le type d'opération de l'opération correspond à celui recherché
                        soldeCat += o.getMontantOp();
                    }
                }
                mapSoldeParCat.put(c, soldeCat);
            }    
            f = mapSoldeParCat;
        }
        return f;
    }


    
    /** 
     * Fonction pour avoir une sous catégorie
     * @param nom
     * @return SousCategorie
     */
    public SousCategorie getSousCatParNom(String nom)
    {
        SousCategorie sc = null;
        int i = 0;
        int j = 0;
        while(sc == null && i < listeCategoriePrincipale.size())
        {
            // on parcourt la liste des catégories principales
            j = 0;
            while(j < listeCategoriePrincipale.get(i).getlSousCat().size() && sc == null)
            {
                // on parcourt la liste des sous catégories de chaque catégorie principale
                if(listeCategoriePrincipale.get(i).getlSousCat().get(j).getNom().equals(nom))
                {
                    // si le nom de la sous cat = nom recherché
                    sc = listeCategoriePrincipale.get(i).getlSousCat().get(j);
                }
                else
                {
                    j++;
                }
            }
            i++;
        }
        if(sc == null)
        {
            System.out.println("sous catégorie n'existe pas.");
        }
        return sc; // == null si n'existe pas
    }

    
    /** 
     * Ajout d'une opération sans vérification
     * @param op
     */
    // ajout sans vérification
    public void ajouterOperation(OperationPonctuelle op )
    {
        this.listeOperationPonc.add(op);    
        if(op.getTypeOperation().equals("dépense"))
        {
            // on enlève le montant de l'opération au solde
            this.solde -= op.getMontantOp();
        }
        else
        {
            // inverse
            this.solde += op.getMontantOp();
        }
    }

    
    /** 
     * Méthode pour ajouter une nouvelle opération avec une écriture dans un fichier
     * @param op
     */
    // ajout + écriture dans le fichier
    public void ajouterNouvelleOperation(OperationPonctuelle op)
    {
        this.listeOperationPonc.add(op);
        if(op.getTypeOperation().equals("dépense"))
        {
            // on enlève le montant de l'opération au solde
            this.solde -= op.getMontantOp();
        }
        else
        {
            // inverse
            this.solde += op.getMontantOp();
        }

        // récupération Utilisateur connecté
        Utilisateur u = Utilisateur.getInstance();
        
        // écriture dans le fichier de l'opération ponctuelle
        // TODO : CAS OPERATION RECURRENTE


        // ATTENTION CARACTERE DE SEPARTION : |
        String chemin = "Banking/src/main/resources/Data/" + u.getNomUtilisateur();
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // on n'enregistre pas l'identifiant, il lui sera redonné à la réouverture du compte
        byte[] opB = ( "\n" + op.getTypeOperation() + "|" + op.getNom() + "|" + Double.toString(op.getMontantOp()) + "|" + dateFormat.format(op.getDateOp()) + "|" + op.getCategorie().toString()).getBytes();

        try {
            Files.write(Paths.get(chemin), opB, StandardOpenOption.APPEND);
            // à partir de la ligne 5 du fic se sont les opérations        
        }
        catch(IOException e)
        {
            System.out.println("Problème d'écriture de l'opération dans le fichier");
            e.printStackTrace();
        }

    }

    
    /** 
     * Méthode pour supprimer une opération
     * @param op
     */
    public void supprimerOperation(OperationPonctuelle op)
    {
        this.listeOperationPonc.remove(op);
    }

    
    /** 
     * Méthode pour générer les catégories principales
     * @return ArrayList<CategoriePrincipale>
     */
    public ArrayList<CategoriePrincipale> genererCatPrincipale()
    {
        // TODO : Générer catégorie principale, les sous catégories et un seuil négatif au début
        ArrayList<CategoriePrincipale> liste = new ArrayList<>();
        
        CategoriePrincipale cHabitation = new CategoriePrincipale("Habitation", "#572502");
        cHabitation.ajouterSousCat(new SousCategorie("h0","Impôt taxe foncière", "#E36205"));
        cHabitation.ajouterSousCat(new SousCategorie("h1","Ameublement et Equipement", "#D15A04"));
        cHabitation.ajouterSousCat(new SousCategorie("h2","Assurance Logement", "#C75504"));
        cHabitation.ajouterSousCat(new SousCategorie("h3","Ménage", "#B54E04"));
        cHabitation.ajouterSousCat(new SousCategorie("h4","Jardin", "#AB4903"));
        cHabitation.ajouterSousCat(new SousCategorie("h5","Habitation Secondaire", "#994203"));
        cHabitation.ajouterSousCat(new SousCategorie("h6","Loyer", "#8F3D03"));
        cHabitation.ajouterSousCat(new SousCategorie("h7","Electricité, Eau, Gaz", "#7D3602"));
        cHabitation.ajouterSousCat(new SousCategorie("h8","Travaux", "#733102"));
        cHabitation.ajouterSousCat(new SousCategorie("h9","Impot taxe d'habitation", "#612A02"));

        CategoriePrincipale cFinance = new CategoriePrincipale("Finance", "#CC1A3A"); 
        cFinance.ajouterSousCat(new SousCategorie("f0","Impôt", "#CC1A3A"));
        cFinance.ajouterSousCat(new SousCategorie("f1","Prêt", "#BA1836"));
        cFinance.ajouterSousCat(new SousCategorie("f2","Epargne", "#B01733"));
        cFinance.ajouterSousCat(new SousCategorie("f3","Agio", "#A1152F"));
        cFinance.ajouterSousCat(new SousCategorie("f4","Frais bancaires", "#94132B"));
        cFinance.ajouterSousCat(new SousCategorie("f5","Assurance vie", "#821126"));
        cFinance.ajouterSousCat(new SousCategorie("f6","Bourse", "#781023"));
        cFinance.ajouterSousCat(new SousCategorie("f7","Retrait d'argent", "#690E1E"));

        CategoriePrincipale cAlimentation = new CategoriePrincipale("Alimentation", "#F5D467");
        cAlimentation.ajouterSousCat(new SousCategorie("a0","Cantine", "#E3C45F"));
        cAlimentation.ajouterSousCat(new SousCategorie("a1","Restaurant", "#F5D467"));
        cAlimentation.ajouterSousCat(new SousCategorie("a2","Course", "#CCB056"));
        
        CategoriePrincipale cHabillement = new CategoriePrincipale("Habillement", "#FA4A99");
        cHabillement.ajouterSousCat(new SousCategorie("h10","Accessoire", "#E8468F"));
        cHabillement.ajouterSousCat(new SousCategorie("h11","Chaussure", "#FA4A99"));
        cHabillement.ajouterSousCat(new SousCategorie("h12","Vêtement", "#DE4388"));

        CategoriePrincipale cLoisir = new CategoriePrincipale("Loisir", "#B66FDA"); 
        cLoisir.ajouterSousCat(new SousCategorie("l0","Multimédia", "#B66FDA"));
        cLoisir.ajouterSousCat(new SousCategorie("l1","Cinéma", "#B66FDA"));
        cLoisir.ajouterSousCat(new SousCategorie("l2","Hôtel", "#9E60BD"));
        cLoisir.ajouterSousCat(new SousCategorie("l3","Sport", "#9158AD"));
        cLoisir.ajouterSousCat(new SousCategorie("l4","Vacances", "#A665C7"));
        cLoisir.ajouterSousCat(new SousCategorie("l5","Jeux", "#76498F"));
        cLoisir.ajouterSousCat(new SousCategorie("l6","Sortie", "#6E4485"));
        cLoisir.ajouterSousCat(new SousCategorie("l7","Animaux", "#8652A1"));
        cLoisir.ajouterSousCat(new SousCategorie("l8","Evènement", "#5B386E"));
        cLoisir.ajouterSousCat(new SousCategorie("l9","Livres", "#4C2F5C"));
        cLoisir.ajouterSousCat(new SousCategorie("l10","Dépenses cadeaux", "#68407D"));


        CategoriePrincipale cSoin = new CategoriePrincipale("Soin", "#90C8E0"); 
        cSoin.ajouterSousCat(new SousCategorie("s0","Santé", "#90C8E0"));
        cSoin.ajouterSousCat(new SousCategorie("s1","Mutuelle", "#84B8CF"));
        cSoin.ajouterSousCat(new SousCategorie("s2","Pharmacie", "#7EAFC4"));
        cSoin.ajouterSousCat(new SousCategorie("s3","Soin du corps", "#74A1B5"));
        cSoin.ajouterSousCat(new SousCategorie("s4","Assurance santé", "#6C96A8"));
        cSoin.ajouterSousCat(new SousCategorie("s5","Aide à domicile", "#608696"));
        cSoin.ajouterSousCat(new SousCategorie("s6","Service à la personne", "#5A7D8C"));
        cSoin.ajouterSousCat(new SousCategorie("s7","Coiffeur", "#506F7D"));

        CategoriePrincipale cTransport = new CategoriePrincipale("Transport", "#572502"); 
        cTransport.ajouterSousCat(new SousCategorie("t0","Véhicule", "#B87548"));
        cTransport.ajouterSousCat(new SousCategorie("t1","Moto", "#506F7D"));
        cTransport.ajouterSousCat(new SousCategorie("t2","Location véhicule", "#9C633D"));
        cTransport.ajouterSousCat(new SousCategorie("t3","Essence", "#8C5937"));
        cTransport.ajouterSousCat(new SousCategorie("t4","Péage", "#805132"));
        cTransport.ajouterSousCat(new SousCategorie("t5","Assurance moto", "#6E462B"));
        cTransport.ajouterSousCat(new SousCategorie("t6","Transport en commun", "#633F27"));
        cTransport.ajouterSousCat(new SousCategorie("t7","Assurance voiture", "#543521"));
        cTransport.ajouterSousCat(new SousCategorie("t8","Parking", "#472D1C"));

        CategoriePrincipale cCommunication = new CategoriePrincipale("Communication", "#572502"); 
        cCommunication.ajouterSousCat(new SousCategorie("c0","Communication", "#ABABB0"));
        cCommunication.ajouterSousCat(new SousCategorie("c1","Internet et TV", "#8F8F94"));
        cCommunication.ajouterSousCat(new SousCategorie("c2","Téléphone", "#99999E"));


        CategoriePrincipale cAutre = new CategoriePrincipale("Autre", "#B87548"); 
        cAutre.ajouterSousCat(new SousCategorie("a10","Amende", "#DFEFCA"));
        cAutre.ajouterSousCat(new SousCategorie("a11","Frais scolaire", "#CFDEBD"));
        cAutre.ajouterSousCat(new SousCategorie("a12","Autres dépenses", "#919C84"));
        cAutre.ajouterSousCat(new SousCategorie("a13","Frais de justice", "#C6D4B4"));
        cAutre.ajouterSousCat(new SousCategorie("a14","Dons", "#B5C2A5"));
        cAutre.ajouterSousCat(new SousCategorie("a15","Impots autres", "#ACB89C"));
        cAutre.ajouterSousCat(new SousCategorie("a16","Assurances autres", "#9BA68D"));

        CategoriePrincipale cRevenu = new CategoriePrincipale("Revenu", "#B87548"); 
        cRevenu.ajouterSousCat(new SousCategorie("r0","Salaire", "#3D91E0"));
        cRevenu.ajouterSousCat(new SousCategorie("r1","Remboursement social", "#3886CF"));
        cRevenu.ajouterSousCat(new SousCategorie("r2","Sécurité sociale", "#357FC4"));
        cRevenu.ajouterSousCat(new SousCategorie("r3","Revenu foncier", "#3074B3"));
        cRevenu.ajouterSousCat(new SousCategorie("r4","CAF", "#2D6DA8"));
        cRevenu.ajouterSousCat(new SousCategorie("r5","Autres revenus", "#296196"));
        cRevenu.ajouterSousCat(new SousCategorie("r6","Rente pension", "#3074B3"));
        cRevenu.ajouterSousCat(new SousCategorie("r7","Remboursement des frais professionnels", "#214F7A"));
        cRevenu.ajouterSousCat(new SousCategorie("r8","Cadeau", "#1E4970"));
        cRevenu.ajouterSousCat(new SousCategorie("r9","Dividendes et plus values", "#193D5E"));


        liste.add(cHabitation);
        liste.add(cFinance);
        liste.add(cAlimentation);
        liste.add(cHabillement);
        liste.add(cLoisir);
        liste.add(cSoin);
        liste.add(cTransport);
        liste.add(cAutre);
        liste.add(cRevenu);
        return liste;
    }

    
    /** 
     * Fonction pour savoir si le compte est a découvert
     * @return boolean
     */
    public boolean aDecouvert()
    {
        return this.solde < 0; // return true si solde < 0
    }

    
    /** 
     * Fonction pour avoir le solde du compte
     * @return Double
     */
    public Double getSolde() {
        return solde;
    }

    
    /** 
     * Fonction pour fixer un solde
     * @param solde
     */
    public void setSolde(Double solde) {
        this.solde = solde;
    }

    
    /** 
     * @return ArrayList<CategoriePrincipale>
     */
    public ArrayList<CategoriePrincipale> getListeCategoriePrincipale() {
        return listeCategoriePrincipale;
    }

    
    /** 
     * @param listeCategoriePrincipale
     */
    public void setListeCategoriePrincipale(ArrayList<CategoriePrincipale> listeCategoriePrincipale) {
        this.listeCategoriePrincipale = listeCategoriePrincipale;
    }

    
    /** 
     * @return ArrayList<OperationPonctuelle>
     */
    public ArrayList<OperationPonctuelle> getListeOperation() {
        return listeOperationPonc;
    }

    
    /** 
     * @param listeOperationPonc
     */
    public void setListeOperation(ArrayList<OperationPonctuelle> listeOperationPonc) {
        this.listeOperationPonc = listeOperationPonc;
    }

    
    /** 
     * Fonction pour avoir des dates
     * @return ArrayList<Date>
     */
    public ArrayList<Date> getDates(){
        ArrayList<Date> res = new ArrayList<Date>();
        int i;
        for (i=0; i< this.getListeOperation().size(); i++){
            if (!res.contains(this.getListeOperation().get(i).getDateOp())){
                res.add(this.getListeOperation().get(i).getDateOp());
            }
        }
        return res;
    }

}
