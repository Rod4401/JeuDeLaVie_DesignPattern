package Commande;

import Cellule.Cellule;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 11:00:16 CET 2022
 *
 */
/**
 * Notre jeu de la vie ne repose pas sur un second tableau qui stoque le nombre
 * de voisins mais sur une liste de commandes executee apres le parcours de la
 * grille Cette classe abstraite permet de representer une commande
 */
public abstract class Commande {
	/**
	 * La cellule reliée à cette commande
	 */
	protected Cellule cellule;

	/**
	 * Le constructeur de la classe
	 * 
	 * @param c La cellule associee a cette commande
	 */
	protected Commande(Cellule c) {
		this.cellule = c;
	}

	/**
	 * Le prototype de la methode qui fait executer une commande
	 */
	abstract public void executer();
}