package Observateur;

import JeuDeLaVie.JeuDeLaVie;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 11:44:56 CET 2022
 *
 */
/**
 * L'observateur console est un observateur qui permet d'afficher l'evolution du
 * jeu dans une console
 */
public class ObservateurConsole implements Observateur {
	/**
	 * Le jeu de la vie qu'observe cet observateur
	 */
	private JeuDeLaVie jeu;

	/**
	 * Le constructeur de la classe
	 * 
	 * @param jeu Le jeu relié à cet observateur
	 */
	public ObservateurConsole(JeuDeLaVie jeu) {
		this.jeu = jeu;
	}

	/**
	 * Méthode qui permet d'actualiser cet observateur elle réalise l'affichage du
	 * jeu de la vie dans le terminal
	 */
	@Override
	public void actualise() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
		System.out.println("Un @ correpond à une cellule vivante");
		int nbVivantes = 0;
		System.out.print("+");
		for (int y = 0; y < jeu.getYMax(); y++)
			System.out.print("-");
		System.out.println("+");
		for (int x = 0; x < jeu.getXMax(); x++) {
			System.out.print("|");
			for (int y = 0; y < jeu.getYMax(); y++) {
				if (jeu.getGrilleXY(x, y).estVivante()) {
					System.out.print("@");
					nbVivantes++;
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("|");
		}
		System.out.print("+");
		for (int y = 0; y < jeu.getYMax(); y++)
			System.out.print("-");
		System.out.println("+");
		System.out.println("----------");
		System.out.println("Generations n° : " + this.jeu.getNbGenerations());
		System.out.println("Il y a " + nbVivantes + " cellules vivantes");
		System.out.println("----------");
	}
}