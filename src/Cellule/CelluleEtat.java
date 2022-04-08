package Cellule;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 09:30:51 CET 2022
 *
 */
import java.io.Serializable;

import Visiteur.Visiteur;

/**
 * L'interface CelluleEtat permet de definir des comportements pour un etat
 */
public interface CelluleEtat extends Serializable {
	/**
	 * Le prototype de la methode permet de renvoyer une celluleEtat vivante
	 */
	public CelluleEtat vit();

	/**
	 * Le prototype de la methode permet de renvoyer une celluleEtat morte
	 */
	public CelluleEtat meurt();

	/**
	 * Le prototype de la methode permet de savoir si la cellule est vivante ou non
	 */
	public boolean estVivante();

	/**
	 * Le prototype de la methode qui fait visiter un visiteur
	 * 
	 * @param v Le visiteur
	 * @param c La cellule
	 */
	public void accepte(Visiteur v, Cellule c);
}