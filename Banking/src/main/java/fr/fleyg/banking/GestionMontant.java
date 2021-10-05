package fr.fleyg.banking;
/**
 * Cette interface permet de gérer les montants
 */
public interface GestionMontant {
    public void modifierMontant(Double nouveauMontant);
    public boolean verifierMontant(Double montant);
}
