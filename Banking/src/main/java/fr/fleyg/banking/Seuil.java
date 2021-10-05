package fr.fleyg.banking;

public class Seuil implements GestionMontant{
    private int identifiant;
    private Double montantSeuil;
    private CategoriePrincipale cp;

    private static int nbSeuil = 0;
    
    public Seuil(CategoriePrincipale cp, Double montantSeuil) {
        this.identifiant = nbSeuil;
        nbSeuil++;
        // permet d'identifier le seuil
        this.montantSeuil = montantSeuil;
        this.cp = cp;
    }

    public CategoriePrincipale getCategorie()
    {
        return this.cp;
    }

    
    /** 
     * Getter de l'identifiant du seuil
     * @return int
     */
    public int getIdentifiant() {
        return identifiant;
    }

    
    /** 
     * Getter du montant du seuil
     * @return Double
     */
    public Double getMontantSeuil() {
        return montantSeuil;
    }

    
    /** 
     * Méthode pour modifier le montant du seuil
     * @param nouveauMontant
     */
    @Override
    public void modifierMontant(Double nouveauMontant) {
        this.montantSeuil = nouveauMontant;
    }

    
    /** 
     * Fonction pour vérifier le montant d'un seuil
     * @param montant
     * @return boolean
     */
    @Override
    public boolean verifierMontant(Double montant) {
        boolean res = false;
        if (montant<= this.montantSeuil){
            res = true;
        }
        return res;
    }

    
}
