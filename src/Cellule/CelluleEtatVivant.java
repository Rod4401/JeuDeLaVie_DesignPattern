package Cellule;

import Visiteur.Visiteur;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 09:38:30 CET 2022
 *
 */

/**
 * La classe CelluleEtatVivant permet de representer le concepte d'etat vivant
 */
@SuppressWarnings("serial")
public class CelluleEtatVivant implements CelluleEtat {
	/**
	 * L'instance unique de la classe
	 */
	private static CelluleEtatVivant instance;

	/**
	 * M�thode qui cr�e une seule instance de la classe et qui la renvoie
	 * 
	 * @return L'instance unique
	 */
	public static CelluleEtatVivant getInstance() {
		if (instance == null) {
			instance = new CelluleEtatVivant();
		}
		return instance;
	}

	/**
	 * On rend le constructeur priv�
	 */
	private CelluleEtatVivant() {
	}

	/**
	 * La m�thode permet de renvoyer une CelluleEtat vivante Elle change l'�tat de
	 * la cellule
	 * 
	 * @return La CelluleEtat correpondante
	 */
	@Override
	public CelluleEtat vit() {
		return this;
	}

	/**
	 * La m�thode permet de renvoyer une CelluleEtat morte Elle change l'�tat de la
	 * cellule
	 * 
	 * @return La CelluleEtat correpondante
	 */
	@Override
	public CelluleEtat meurt() {
		return CelluleEtatMort.getInstance();
	}

	/**
	 * La m�thode permet d'indiquer si la cellule est vivante
	 * 
	 * @return true
	 */
	@Override
	public boolean estVivante() {
		return true;
	}

	/**
	 * M�thode qui fait visiter un visiteur
	 * 
	 * @param v Le visiteur
	 * @param c La cellule
	 */
	@Override
	public void accepte(Visiteur v, Cellule c) {
		v.visiteCelluleVivante(c);
	}
}