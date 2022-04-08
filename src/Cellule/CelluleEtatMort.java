package Cellule;

import Visiteur.Visiteur;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 09:38:30 CET 2022
 *
 */
/**
 * La classe CelluleEtatMort permet de representer le concepte d'etat morte
 */
@SuppressWarnings("serial")
public class CelluleEtatMort implements CelluleEtat {
	/**
	 * L'instance unique de la classe
	 */
	private static CelluleEtatMort instance;

	/**
	 * Méthode qui crée une seule instance de la classe et qui la renvoie
	 * 
	 * @return L'instance unique
	 */
	public static CelluleEtatMort getInstance() {
		if (instance == null) {
			instance = new CelluleEtatMort();
		}
		return instance;
	}

	/**
	 * On rend le constructeur privé
	 */
	private CelluleEtatMort() {
	}

	/**
	 * La méthode permet de renvoyer une CelluleEtat vivante Elle change l'état de
	 * la cellule
	 * 
	 * @return La CelluleEtat correpondante
	 */
	@Override
	public CelluleEtat vit() {
		return CelluleEtatVivant.getInstance();
	}

	/**
	 * La méthode permet de renvoyer une CelluleEtat morte Elle change l'état de la
	 * cellule
	 * 
	 * @return La CelluleEtat correpondante
	 */
	@Override
	public CelluleEtat meurt() {
		return this;
	}

	/**
	 * La méthode permet d'indiquer si la cellule est morte
	 * 
	 * @return false
	 */
	@Override
	public boolean estVivante() {
		return false;
	}

	/**
	 * Méthode qui fait visiter un visiteur
	 * 
	 * @param v Le visiteur
	 * @param c La cellule
	 */
	@Override
	public void accepte(Visiteur v, Cellule c) {
		v.visiteCelluleMorte(c);
	}
}