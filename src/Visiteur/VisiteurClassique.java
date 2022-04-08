package Visiteur;

import Cellule.Cellule;
import Commande.CommandeMeurt;
import Commande.CommandeVit;
import JeuDeLaVie.JeuDeLaVie;
import Main.Fenetre;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 11:25:37 CET 2022
 *
 */
/**
 * Le visiteurClassique permet de realiser la strategie classique du jeu de la
 * vie
 */
public class VisiteurClassique extends Visiteur {
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
					+ Fenetre.getProperties().getProperty("ActionStratClassique.windowname"));
	}

	/**
	 * Comme on souhaite pouvoir changer de visiteur Il semble evident de faire le
	 * pattern Singleton pour eviter de creer des instances a outrance
	 */
	private static VisiteurClassique instance;

	/**
	 * Methode qui permet d'obtenir l'instance de ce visiteur
	 * 
	 * @param jeu Le jeu
	 * @return L'instance unique
	 */
	public static VisiteurClassique getInstance(JeuDeLaVie jeu) {
		if (instance == null) {
			instance = new VisiteurClassique(jeu);
		}
		return instance;
	}

	/**
	 * Constructeur de la classe est privé
	 * 
	 * @param jeu Le jeu
	 */
	private VisiteurClassique(JeuDeLaVie jeu) {
		super(jeu);
	}

	/**
	 * Méthode qui permet au visiteur de visiter une cellule vivante
	 * 
	 * @param c La cellule qu'il visite
	 */
	public void visiteCelluleVivante(Cellule c) {
		int nbVoisines = c.nombreVoisinesVivantes(this.jeu);
		// Si la cellule a trop peu de voisines alors elle meurt de solitude
		if (nbVoisines < 2) {
			jeu.ajouteCommande(new CommandeMeurt(c));
			return;
		}
		// Si la cellule a trop de voisines alors elle meurt d'etouffement
		if (nbVoisines > 3) {
			jeu.ajouteCommande(new CommandeMeurt(c));
		}
	}

	/**
	 * Méthode qui permet au visiteur de visiter une cellule morte
	 * 
	 * @param c La cellule qu'il visite
	 */
	public void visiteCelluleMorte(Cellule c) {
		int nbVoisines = c.nombreVoisinesVivantes(this.jeu);
		// Si la cellule a 3 voisines et qu'elle est "morte" alors elle renaît
		if (nbVoisines == 3) {
			jeu.ajouteCommande(new CommandeVit(c));
		}
	}
}