package Commande;

import Cellule.Cellule;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 11:07:56 CET 2022
 *
 */
/**
 * La commande Vit est une commande qui permet de faire vivre la cellule qui lui
 * est reliee
 */
public class CommandeVit extends Commande {

	/**
	 * Le constructeur de la classe
	 * 
	 * @param c La cellule associee a cette commande
	 */
	public CommandeVit(Cellule c) {
		super(c);
	}

	/**
	 * Méthode qui fait executer la commande En l'occurence, elle fait vivre la
	 * cellule
	 */
	@Override
	public void executer() {
		this.cellule.vit();
	}
}