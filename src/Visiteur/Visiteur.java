package Visiteur;

import Cellule.Cellule;
import JeuDeLaVie.JeuDeLaVie;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 11:15:07 CET 2022
 *
 */
/**
 * Pour pouvoir changer facilement de strategie de jeu, nous implementons le
 * pattern Visiteur Un visiteur est une entite qui execute un comportement
 * different selon le type de cellule qu'il visite
 */
public abstract class Visiteur {
	/**
	 * Le jeu de la vie que ce visiteur peut visiter
	 */
	protected JeuDeLaVie jeu;

	/**
	 * Le constructeur de la classe
	 * 
	 * @param jeu Le jeu
	 */
	public Visiteur(JeuDeLaVie jeu) {
		this.jeu = jeu;
	}

	/**
	 * Méthode qui permet au visiteur de visiter une cellule vivante
	 * 
	 * @param c La cellule qu'il visite
	 */
	abstract public void visiteCelluleVivante(Cellule c);

	/**
	 * Méthode qui permet au visiteur de visiter une cellule morte
	 * 
	 * @param c La cellule qu'il visite
	 */
	abstract public void visiteCelluleMorte(Cellule c);

	/**
	 * Certains visiteurs (un seul en verite) possedent une fenetre de parametre
	 * Cette methode permet de masquer la popup
	 */
	abstract public void masquerParametres();

	/**
	 * Certains visiteurs (un seul en verite) possedent une fenetre de parametre
	 * Cette methode permet d'afficher la popup
	 */
	abstract public void afficherParametres();
}