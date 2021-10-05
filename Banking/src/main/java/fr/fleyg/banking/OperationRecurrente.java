package fr.fleyg.banking;

public class OperationRecurrente extends Operation{
    private String jourDeRecu;
    private String peridodicite;
    /**
     * Le constructeur d'Operation recurrente
     * @param typeOperation
     * @param nom
     * @param montantOp
     * @param jourDeRecu
     * @param peridodicite
     * @param sCategorie categorie de l'operation
     */
    public OperationRecurrente(String typeOperation, String nom, Double montantOp, String jourDeRecu,
            String peridodicite, SousCategorie sCategorie) {
        super(typeOperation, nom, montantOp, sCategorie);
        this.jourDeRecu = jourDeRecu;
        this.peridodicite = peridodicite;
    }
/**
 * permet d'obtenir le jour de recu
 * @return le jour de prélèvement/depot
 */
    public String getJourDeRecu() {
        return jourDeRecu;
    }
/**
 * permet de definir le jour de recu
 * @param jourDeRecu
 */
    public void setJourDeRecu(String jourDeRecu) {
        this.jourDeRecu = jourDeRecu;
    }
/**
 * permet d'obtenir la periodicite
 * @return la periodicité
 */
    public String getPeridodicite() {
        return peridodicite;
    }
/**
 * permet de definir la periodicite
 * @param peridodicite
 */
    public void setPeridodicite(String peridodicite) {
        this.peridodicite = peridodicite;
    }

    @Override
    public String toString() {
        return "OperationRecurrente [jourDeRecu=" + jourDeRecu + ", peridodicite=" + peridodicite + "]";
    }

    
}
