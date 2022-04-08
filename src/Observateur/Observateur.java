package Observateur;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 10:24:29 CET 2022
 *
 */

/**
 * L'interface Observateur permet de definir une classe comme un observateur Il
 * poura etre attache a une classe dite observable pour pouvoir etre notifie
 * lors d'un changement des donnees qu'il observe
 */
public interface Observateur {
	/**
	 * Le prototype de la methode qui réalise l'acualisation d'un observateur
	 */
	public void actualise();
}