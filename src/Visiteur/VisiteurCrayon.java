package Visiteur;

import Cellule.Cellule;
import JeuDeLaVie.JeuDeLaVie;
import Main.Fenetre;

/**
 * @author Meunier
 * @version 0.1 : Date : Wed Mar 30 09:01:34 CEST 2022
 *
 */
/**
 * La classe VisiteurCrayon permet de representer un crayon Lorsque
 * l'utilisateur dessine sur la grille, il peut selectionner le crayon pour
 * faire naitre des cellules.
 */
public class VisiteurCrayon extends VisiteurOutil {
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
	private static VisiteurCrayon instance;

	/**
	 * La methode getInstance permet d'obtenir l'instance unique de ce visiteur
	 * 
	 * @param jeu Le jeu de la vie associe a ce visiteur
	 * @return L'instance unique
	 */
	public static VisiteurCrayon getInstance(JeuDeLaVie jeu) {
		if (instance == null) {
			instance = new VisiteurCrayon(jeu);
		}
		return instance;
	}

	/**
	 * Constructeur de la classe est privé
	 * 
	 * @param jeu Le jeu associe a ce visiteur
	 */
	private VisiteurCrayon(JeuDeLaVie jeu) {
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
					jeu.setCellule(x, y);
				}
			}
		}
	}
}