package Cellule;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 09:29:07 CET 2022
 *
 */
import java.awt.Color;
import java.io.Serializable;

import JeuDeLaVie.JeuDeLaVie;
import Visiteur.Visiteur;

/**
 * La classe cellule permet de representer une cellule du jeu de la vie
 */
@SuppressWarnings("serial")
public class Cellule implements Serializable {
	/**
	 * L'état de la cellule
	 */
	private CelluleEtat etat;

	/**
	 * L'etat de la cellule Ici on parle pas d'etat vivant ou mort On parle
	 * d'etatVivant c'est-à-dire de l'etat que represente la cellule ex : un arbre
	 * est soit en feu soit vivant soit en cendre donc on a comme etat : -vivant -en
	 * feu -en cendres -mort
	 */
	private Etat etatVivant = Etat.DEFAULT;

	/**
	 * L'age de la cellule
	 */
	private int age = 0;

	/**
	 * Les coordonnées x et y de la cellule
	 */
	private int x, y;

	/**
	 * Constructeur de la classe La cellule est initialisé à mort
	 * 
	 * @param x    La coordonnée X de la cellule
	 * @param y    La coordonnée Y de la cellule
	 * @param etat L'état de la cellule
	 */
	public Cellule(int x, int y, CelluleEtat etat) {
		this.etat = etat;
		this.x = x;
		this.y = y;
	}

	/**
	 * Methode qui permet d'obtenir la coordonnee x de la cellule
	 * 
	 * @return x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Methode qui permet d'obtenir la coordonnee y de la cellule
	 * 
	 * @return y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * La méthode vit permet de changer l'état de la cellule sur VIVANTE Elle fait
	 * vivre la cellule
	 */
	public void vit() {
		this.etat = this.etat.vit();
	}

	/**
	 * La méthode meurt permet de changer l'état de la cellule sur MORTE Elle fait
	 * mourir la cellule
	 */
	public void meurt() {
		this.etat = this.etat.meurt();
		this.resetAge();
		this.changerEtat(Etat.DEFAULT);
	}

	/**
	 * Méthode qui indique si la cellule est vivante
	 * 
	 * @return true si oui, false sinon
	 */
	public boolean estVivante() {
		return this.etat.estVivante();
	}

	/**
	 * Méthode qui renvoie le nombre de cellules voisines vivante
	 * 
	 * @param jeu Le jeu de la vie associé à la cellule
	 * @return Le nombre de voisins
	 */
	public int nombreVoisinesVivantes(JeuDeLaVie jeu) {
		// Initialisation du compteur de cellules vivantes
		int compteurVivantes = 0;
		for (int x = this.x - 1; x <= this.x + 1; x++) {
			for (int y = this.y - 1; y <= this.y + 1; y++) {
				// On regarde si les coordonnés sont valides
				if (jeu.testXY(x, y) && !(x == this.x && y == this.y)) {
					if (jeu.getGrilleXY(x, y).estVivante()) {
						compteurVivantes++;
					}
				}
			}
		}
		return compteurVivantes;
	}

	/**
	 * Méthode qui fait visiter un visiteur
	 * 
	 * @param v Le visiteur
	 */
	public void accepte(Visiteur v) {
		this.etat.accepte(v, this);
	}

	/**
	 * Methode qui permet d'obtenir la couleur en fonction de l'etat de la cellule
	 * 
	 * @return Color La couleur qui est associe a l'etat de la cellule
	 */
	public Color getColor() {
		return this.etatVivant.color;
	}

	/**
	 * Cette methode permet d'obtenir l'etat de la cellule
	 * 
	 * @return Etat L'etat de la cellule
	 */
	public Etat getEtat() {
		return this.etatVivant;
	}

	/**
	 * Cette methode permet de changer l'etat de la cellule
	 * 
	 * @param e Le nouvel etat (le nouveau etatVivant)
	 */
	public void changerEtat(Etat e) {
		this.etatVivant = e;
	}

	/**
	 * Methode qui permet d'obtenir l'age de la cellule
	 * 
	 * @return L'age de la cellule
	 */
	public int getAge() {
		return this.age;
	}

	/**
	 * Methode qui permet de remettre a 0 l'age de la cellule
	 */
	public void resetAge() {
		this.age = 0;
	}

	/**
	 * Methode qui permet de faire vieillir la cellule
	 */
	public void vieillir() {
		this.age++;
	}
}