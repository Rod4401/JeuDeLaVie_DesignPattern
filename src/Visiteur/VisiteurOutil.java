package Visiteur;

import Cellule.Cellule;
import JeuDeLaVie.JeuDeLaVie;

/**
 * @author Meunier
 * @version 0.1 : Date : Wed Mar 30 15:25:38 CEST 2022
 *
 */
/**
 * La classe VisiteurOutil permet de definir des visiteur de type dessin et pas
 * Strategie Ces visiteurs permettent de faire du dessin, on note le crayon ou
 * la gomme. On pourrait imaginer d'autre outils comme une zone de selection, un
 * sceau de peinture (recouvrir)
 */
abstract public class VisiteurOutil extends Visiteur {
	/**
	 * Le constructeur de la classe
	 * 
	 * @param jeu Le jeu associe a ce visiteur
	 */
	public VisiteurOutil(JeuDeLaVie jeu) {
		super(jeu);
	}

	/**
	 * Méthode qui permet au visiteur de visiter une cellule vivante
	 * 
	 * @param c La cellule vivante qu'il visite
	 */
	@Override
	public void visiteCelluleVivante(Cellule c) {
		appliquerOutil(c);
	}

	/**
	 * Méthode qui permet au visiteur de visiter une cellule morte
	 * 
	 * @param c La cellule morte qu'il visite
	 */
	@Override
	public void visiteCelluleMorte(Cellule c) {
		appliquerOutil(c);
	}

	/**
	 * Prototype de la methode qui permet d'appliquer l'outil du visiteur Comme
	 * l'etat de la cellule importe peu, cette methode est appelee par les 2
	 * methodes de visite
	 * 
	 * @param c La cellule a visiter
	 */
	abstract protected void appliquerOutil(Cellule c);
}