package fr.fleyg.banking;

/**
 * Cette classe permet de définir une sous catégorie
 */
public class SousCategorie extends Categorie{

    private String id;
    /**
     * Constructeur de la sous catégorie
     * @param id
     * @param nom
     * @param couleur
     */
    public SousCategorie(String id, String nom, String couleur) {
        super(nom, couleur);
        this.id = id;
    }

    
    /** 
     * Getter pour l'id de la sous catégorie
     * @return String
     */
    public String getId()
    {
        return this.id;
    }

    
    /** 
     * Fonction de toString
     * @return String
     */
    public String toString()
    {
        return super.getNom();
    }
    
}
