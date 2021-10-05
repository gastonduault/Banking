package fr.fleyg.banking;

import java.util.ArrayList;

public class CategoriePrincipale extends Categorie{
/**
 * La classe CategoriePrincipale permet de gérer les catégories principales
 */
    public ArrayList<SousCategorie> lSousCat;
/**
 * Le constructeur de categorie principale
 * @param nom
 * @param couleur
 */
    public CategoriePrincipale(String nom, String couleur) {
        super(nom, couleur);
        lSousCat = new ArrayList<>();
        // 
    }
/**
 * Permet d'obtenir la liste des sous categories
 * @return la liste de sous categories
 */
    public ArrayList<SousCategorie> getlSousCat() {
        return lSousCat;
    }
/**
 * Permet d'ajouter une sous categore à la liste
 * @param sc ajouter une sous categorie
 */
    public void ajouterSousCat(SousCategorie sc)
    {
        if(!(estDans(sc)))
        {
            lSousCat.add(sc);
        }
        else
        {
            // message de gestion des problèmes dans la console
            System.out.println("Impossible d'ajouter la sous catégorie");
        }
    }

    // pas besoin de méthode pour supprimer une sous catégorie
/**
 * Une fonction qui verifie la présence d'une sous categorie dans la liste
 * @param sc
 * @return un booleen qui verifie la présence d'une sous categorie dan la liste
 */
    public boolean estDans(SousCategorie sc)
    {
        boolean trouve = false;
        int i = 0;
        while(!(trouve) && i < lSousCat.size())
        {
            if(lSousCat.get(i).getNom().equals(sc.getNom()))
            {
                trouve = true;
            }
            else
            {
                i++;
            }
        }
        return trouve;
    }
    
}
