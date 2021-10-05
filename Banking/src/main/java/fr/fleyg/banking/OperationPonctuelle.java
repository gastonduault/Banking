package fr.fleyg.banking;
import java.util.Calendar;
import java.util.Date;

public class OperationPonctuelle extends Operation{
    private Date dateOp;
/**
 * Le constructeur d'Operation Ponctuelle
 * @param typeOperation
 * @param nom
 * @param montantOp le montant de l'operation
 * @param dateOp la date de l'operation
 * @param sc la categorie de l'operation
 * @param isNew pour ajouter l'operation à l'utilisateur
 */
    public OperationPonctuelle(String typeOperation, String nom, Double montantOp, Date dateOp, SousCategorie sc, boolean isNew) {
        super(typeOperation, nom, montantOp, sc);
        this.dateOp = dateOp;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateOp);
        System.out.println("Création d'une " + typeOperation + " de montant " + montantOp + " et de sous catégorie " + sc.getNom() + ", date : " + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR));
        if(isNew)
        {
            // nouvelle op
            Utilisateur.getInstance().getCompte().ajouterNouvelleOperation(this); // ajout de l'opération dans la liste
        }
    }
/**
 * permet d'obtenir la date de l'operation
 * @return la date de l'operation
 */
    public Date getDateOp() {
        return dateOp;
    }
/**
 * permet de definir la date de l'operation
 * @param dateOp
 */
    public void setDateOp(Date dateOp) {
        this.dateOp = dateOp;
    }

    @Override
    public String toString() {
        return "OperationPonctuelle [dateOp=" + dateOp + "]";
    }
    
    
}
