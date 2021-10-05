package fr.fleyg.banking;
/**
 * La classe Catégorie permet de gérer les différentes catégories 
 */
abstract public class Categorie {
    private String nom;
    private String couleur;
/**
 * Le constructeur de la classe
 * @param nom
 * @param couleur
 */
    public Categorie(String nom, String couleur) {
        this.nom = nom;
        this.couleur = couleur;
    }

/**
 * Permet d'obtenir le nom de la categorie
 * @return le nom de la categorie
 */
    public String getNom() {
        return nom;
    }
/**
 * Permet de definir le nom de la categorie
 * @param nom
 */
    public void setNom(String nom) {
        this.nom = nom;
    }
/**
 * Permet d'obtenir la couleur de la categorie
 * @return la couleur de la catégorie
 */
    public String getCouleur() {
        return couleur;
    }
/**
 * Permet de definir la couleur de la categorie
 * @param couleur
 */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }


}
