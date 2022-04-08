package Visiteur;

import Cellule.Cellule;
import Commande.CommandeMeurt;
import Commande.CommandeVit;
import JeuDeLaVie.JeuDeLaVie;
import Main.Fenetre;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 13:05:38 CET 2022
 *
 */
/**
 * Le VisiteurDayNight permet de realiser la strategie DayNight du jeu de la vie
 */
public class VisiteurDayNight extends Visiteur {
	/**
	 * La methode qui masque les parametres de ce visiteur En l'occurence, elle ne
	 * fait rien
	 */
	@Override
	public void masquerParametres() {
	}

	/**
	 * Methode qui affiche les parametres de ce visiteur En l'occurence, elle
	 * affiche le nom du visiteur
	 */
	@Override
	public void afficherParametres() {
		// Si on a choisi l'observateur fenetre
		if (Fenetre.getInstance() != null)
			Fenetre.getInstance().setTitle(Fenetre.getProperties().getProperty("windowBaseName")
					+ Fenetre.getProperties().getProperty("ActionStratDayNight.windowname"));
	}

	/**
	 * Comme on souhaite pouvoir changer de visiteur Il semble evident de faire le
	 * pattern Singleton pour eviter de creer des instances a outrance
	 */
	private static VisiteurDayNight instance;

	/**
	 * Methode qui permet d'obtenir l'instance de ce visiteur
	 */
	public static VisiteurDayNight getInstance(JeuDeLaVie jeu) {
		if (instance == null) {
			instance = new VisiteurDayNight(jeu);
		}
		return instance;
	}

	/**
	 * Constructeur de la classe est privé
	 */
	public VisiteurDayNight(JeuDeLaVie jeu) {
		super(jeu);
	}

	/**
	 * Méthode qui permet au visiteur de visiter une cellule vivante
	 * 
	 * @param c La cellule vivante a visiter
	 * @return L'instance unique
	 */
	public void visiteCelluleVivante(Cellule c) {
		int nbVoisines = c.nombreVoisinesVivantes(this.jeu);
		// Si la cellule a trop peu de voisines alors elle meurt de solitude
		if (nbVoisines == 0 || nbVoisines == 1 || nbVoisines == 2 || nbVoisines == 5) {
			jeu.ajouteCommande(new CommandeMeurt(c));
		}
	}

	/**
	 * Méthode qui permet au visiteur de visiter une cellule morte
	 * 
	 * @param c La cellule morte a visiter
	 */
	public void visiteCelluleMorte(Cellule c) {
		int nbVoisines = c.nombreVoisinesVivantes(this.jeu);
		// Si la cellule a 3 voisines et qu'elle est "morte" alors elle renaît
		if (nbVoisines == 3 || nbVoisines == 6 || nbVoisines == 7 || nbVoisines == 8) {
			jeu.ajouteCommande(new CommandeVit(c));
		}
	}
}