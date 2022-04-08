package Commande;

import Cellule.Cellule;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 11:10:31 CET 2022
 *
 */
/**
 * La commande Meurt est une commande qui permet de faire mourir la cellule qui
 * lui est reliee
 */
public class CommandeMeurt extends Commande {

	/**
	 * Le constructeur de la classe
	 * 
	 * @param c La cellule associee a cette commande
	 */
	public CommandeMeurt(Cellule c) {
		super(c);
	}

	/**
	 * Méthode qui fait executer la commande En l'occurence, elle fait mourir la
	 * cellule
	 */
	@Override
	public void executer() {
		this.cellule.meurt();
	}
}