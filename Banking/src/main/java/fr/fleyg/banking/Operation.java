package fr.fleyg.banking;
public abstract class Operation implements GestionMontant{
    private int identifiant;
    private String typeOperation;
    private String nom;
    private Double montantOp;
    private SousCategorie categorie;

    public static int nbOperation = 0;

/**
 * Le constructeur d'operation
 * @param typeOperation 
 *      le type de l'operation
 * @param nom
 *      le libelle de l'operation
 * @param montantOp
 *      le montant de l'operation
 * @param categorie
 *      la categorie de l'operation
 */
    public Operation(String typeOperation, String nom, Double montantOp, SousCategorie categorie) {
        this.identifiant = nbOperation;
        nbOperation++;
        // TODO : identifiant unique -> en cas de suppression, problème
        this.typeOperation = typeOperation;
        this.montantOp = montantOp;
        this.nom = nom;
        this.categorie = categorie;
    }

    // TODO : remplir la liste à l'aide d'un fichier au démarrage du programme

    /**
     * Retourne la categorie de l'operation
     * @return categorie de l'operation
     */  
    public SousCategorie getCategorie() {
        return categorie;
    }
/**
 * permet d'obtenir l'identifiant de l'operation
 * @return identifiant de l'operation
 */
    public int getIdentifiant() {
        return identifiant;
    }
    /**
     * permet d'obtenir le type de l'operation
     * @return type de l'operation
     */
    public String getTypeOperation() {
        return typeOperation;
    }
    /**
     * permet d'obtenir le nom de l'operation
     * @return nom de l'operation
     */
    public String getNom() {
        return nom;
    }
    /**
     * permet de definir le nom de l'operation
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * permet d'obtenir le montant de l'operation
     * @return montant de l'operation
     */
    public Double getMontantOp() {
        return montantOp;
    }

    @Override
    public String toString() {
        return "Operation [MontantOp=" + montantOp + ", identifiant=" + identifiant + ", nom=" + nom
                + ", typeOperation=" + typeOperation + "]";
    }
    @Override
    public void modifierMontant(Double nouveauMontant) {
        this.montantOp = nouveauMontant;
    }

    @Override
    public boolean verifierMontant(Double montant) {
        boolean res = false;
        if (this.typeOperation.equals("depense")){
            if (montant<0){
                res = true;
            }
            else {
                System.out.println("Erreur : montant depense incorrect.");
            }
        }
        else if (this.typeOperation.equals("revenu")){
            if (montant>0){
                res = true;
            }
            else {
                System.out.println("Erreur : montant revenu incorrect.");
            }
        }
        else {
            System.out.println("Erreur: type incorrect, 'depense' ou 'revenu");
        }
        return res;
    }
}
