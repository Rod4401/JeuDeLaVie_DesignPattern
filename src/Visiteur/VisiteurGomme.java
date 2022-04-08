package Visiteur;

import Cellule.Cellule;
import JeuDeLaVie.JeuDeLaVie;
import Main.Fenetre;

/**
 * @author Meunier
 * @version 0.1 : Date : Wed Mar 30 13:21:15 CEST 2022
 *
 */
/**
 * La classe VisiteurGomme permet de representer une gomme Lorsque l'utilisateur
 * dessine sur la grille, il peut selectionner la gomme pour tuer des cellules
 * vivantes.
 */
public class VisiteurGomme extends VisiteurOutil {
	/**
	 * La methode qui masque les parametres de ce visiteur En l'occurence, elle ne
	 * fait rien
	 */
	@Override
	public void masquerParametres() {
	}

	/**
	 * Methode qui affiche les parametres de ce visiteur En l'occurence, elle ne
	 * fait rien
	 */
	@Override
	public void afficherParametres() {
	}

	/**
	 * Comme on souhaite pouvoir changer de visiteur Il semble evident de faire le
	 * pattern Singleton pour eviter de creer des instances a outrance
	 */
	private static VisiteurGomme instance;

	/**
	 * La methode getInstance permet d'obtenir l'instance unique de ce visiteur
	 * 
	 * @param jeu Le jeu de la vie associe a ce visiteur
	 */
	public static VisiteurGomme getInstance(JeuDeLaVie jeu) {
		if (instance == null) {
			instance = new VisiteurGomme(jeu);
		}
		return instance;
	}

	/**
	 * Constructeur de la classe est privé
	 * 
	 * @param jeu Le jeu relie a ce visiteur
	 */
	private VisiteurGomme(JeuDeLaVie jeu) {
		super(jeu);
	}

	/**
	 * Methode qui permet d'appliquer l'outil du visiteur Comme l'etat de la cellule
	 * importe peu, cette methode est appelee par les 2 methodes de visite
	 * 
	 * @param c La cellule a visiter
	 */
	@Override
	protected void appliquerOutil(Cellule c) {
		int taille = Fenetre.getInstance().getTailleDessin();
		for (int x = c.getX() - taille / 2; x < c.getX() + (taille + 1) / 2; x++) {
			for (int y = c.getY() - taille / 2; y < c.getY() + (taille + 1) / 2; y++) {
				if (this.jeu.testXY(x, y)) {
					jeu.cutCellule(x, y);
				}
			}
		}
	}
}