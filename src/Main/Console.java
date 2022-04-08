package Main;

import JeuDeLaVie.JeuDeLaVie;
import Observateur.Observateur;
import Observateur.ObservateurConsole;
import Visiteur.Visiteur;
import Visiteur.VisiteurClassique;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 13:15:35 CET 2022
 *
 */
/**
 * La classe console permet de lancer le jeu de la vie avec un observateur
 * console C'est-a-dire, pas d'interface graphique
 */
public class Console {
	/**
	 * La méthode main
	 */
	public static void main(String args[]) {
		// On defini un nombre maximal de generations
		int nombreDeGenerationsMax = 100;
		// Creation du jeu
		JeuDeLaVie jeu = new JeuDeLaVie(30, 30);
		// On remplit aleatoirement
		jeu.grilleRandomizer(0.5);

		// Creation du visiteur classique (au choix)
		Visiteur visiteur = VisiteurClassique.getInstance(jeu);
		jeu.setVisiteur(visiteur);

		// Creation de l'observateur (console)
		Observateur observateur = new ObservateurConsole(jeu);
		jeu.attacheObservateur(observateur);

		// On boucle jusqu'au nombre maximal de generations
		for (int i = 0; i < nombreDeGenerationsMax; i++) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// On ne fait rien
			}
			jeu.calculerGenerationSuivante();
		}
	}
}