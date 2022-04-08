package Cellule;

/**
 * @author Meunier
 * @version 0.1 : Date : Fri Apr 01 14:33:42 CEST 2022
 *
 */
import java.awt.Color;

/**
 * L'enumeration Etat permet de definir des etats personalises pour les cellules
 * Un etat se caracterise par une couleur
 */
public enum Etat {
	// L'etat par default
	DEFAULT(Color.BLACK),

	// Les etats "customs" pour la strategie Visiteur
	ARBRE_VIVANT(Color.GREEN), ARBRE_EN_FEU(Color.RED), ARBRE_EN_CENDRES(Color.GRAY), ARBRE_MORT(Color.BLACK);

	/**
	 * La couleur de l'etat
	 */
	public final Color color;

	/**
	 * Le constructeur de l'etat
	 * 
	 * @param c La couleur associe a l'etat
	 */
	Etat(Color c) {
		this.color = c;
	}
}