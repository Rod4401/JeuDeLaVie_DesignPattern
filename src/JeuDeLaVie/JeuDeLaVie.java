package JeuDeLaVie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 09:47:27 CET 2022
 *
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Cellule.Cellule;
import Cellule.CelluleEtatMort;
import Commande.Commande;
import Observateur.Observable;
import Observateur.Observateur;
import Visiteur.Visiteur;

/**
 * La classe JeuDeLaVie permet de representer le jeu de la vie
 */
public class JeuDeLaVie implements Observable {

	private List<GrilleSauvegarde> listePrecedentsEtats = new ArrayList<GrilleSauvegarde>();
	/**
	 * La liste des observateurs
	 */
	private List<Observateur> observateurs;

	/**
	 * La liste des observateurs
	 */
	private List<Commande> commandes;

	/**
	 * Le tableau qui contient les cellules L'utilisation d'un tableau favorise la
	 * relecture et l'utilisation
	 */
	private Cellule[][] grille;

	/**
	 * Variable qui contient le nombre total a un instant T de cellule vivante
	 */
	private int nbCellulesVivantes = 0;

	/**
	 * Le nombre de cellules en X maximal
	 */
	private int xMax;

	/**
	 * Le nombre de cellules en Y maximal
	 */
	private int yMax;

	/**
	 * Le visiteur de ce jeu de la vie
	 */
	private Visiteur visiteur;

	/**
	 * Le nombre de génération
	 */
	private int generations = 0;

	/**
	 * Boolean pour demander un reset
	 */
	private boolean reset = false;

	/**
	 * Le constructeur de la classe
	 * 
	 * @param xMax Le nombre de lignes maximal
	 * @param yMax Le nombre de colonnes maximal
	 */
	public JeuDeLaVie(int xMax, int yMax) {
		this.xMax = xMax;
		this.yMax = yMax;
		this.observateurs = new LinkedList<Observateur>();
		this.commandes = new LinkedList<Commande>();
		// On initialise le jeu avec que des cellules mortes
		this.initialiseGrille();
	}

	/*
	 * ====================================== = = = = = PARTIE - GRILLE = = = = = =
	 * = ======================================
	 */

	/**
	 * Méthode qui initialise le tableau des cellules
	 */
	public void initialiseGrille() {
		// Creation de la grille
		this.grille = new Cellule[this.xMax][this.yMax];
		for (int x = 0; x < this.xMax; x++) {
			for (int y = 0; y < this.yMax; y++) {
				// Création de la cellule x;y, à l'état mort
				this.grille[x][y] = new Cellule(x, y, CelluleEtatMort.getInstance());
			}
		}
	}

	/**
	 * Méthode qui permet de changer la taille du jeu
	 * 
	 * @param x La nouvelle coordonnee x
	 * @param y La nouvelle coordonnee y
	 */
	public void changerTaille(int x, int y) {
		// On sauvegarde avant de faire l'action
		this.saveEtatGrille();
		// Si il reste des commandes, on les vides
		this.executeCommandes();

		// Puis on change les tailles
		this.xMax = x;
		this.yMax = y;

		// On reinitialise la grille
		this.initialiseGrille();

		// On actualise les observateurs
		this.notifieObservateurs();
	}

	/**
	 * Méthode qui permet de generer (faire vivre) aleatoirement des cellules dans
	 * la grille
	 * 
	 * @param taux La densite de generation
	 */
	public void grilleRandomizer(double taux) {
		// On sauvegarde avant de faire l'action
		this.saveEtatGrille();
		// On remet à 0 la grille
		this.resetGrille();
		for (int x = 0; x < this.xMax; x++) {
			for (int y = 0; y < this.yMax; y++) {
				// Si on génère un nombre inférieur au
				// taux alors on fait vivre la cellue
				if (Math.random() < taux) {
					this.grille[x][y].vit();
				}
			}
		}
		// On termine par notifier tous les observateurs
		this.notifieObservateurs();
	}

	/**
	 * Méthode qui met à l'état "morte" toutes les cellules
	 */
	public void resetGrille() {
		// On sauvegarde avant de faire l'action
		this.saveEtatGrille();

		for (int x = 0; x < this.xMax; x++) {
			for (int y = 0; y < this.yMax; y++) {
				if (this.grille[x][y].estVivante()) {
					this.grille[x][y].meurt();
				}
			}
		}
		// On remet a 0 les compteurs
		this.generations = 0;
		this.resetCompteurCellulesVivantes();

		// On termine par notifier tous les observateurs
		this.notifieObservateurs();
	}

	/**
	 * Méthode qui renvoie une cellule à la coordonné x,y
	 * 
	 * @param x La coordonnée X
	 * @param y La coordonnée Y
	 * @return La cellule corespondante
	 */
	public Cellule getGrilleXY(int x, int y) {
		return this.grille[x][y];
	}

	/**
	 * GETTER des xMax
	 * 
	 * @return Le nombre maximale de lignes
	 */
	public int getXMax() {
		return this.xMax;
	}

	/**
	 * GETTER des yMax
	 * 
	 * @return Le nombre maximale de colonnes
	 */
	public int getYMax() {
		return this.yMax;
	}

	/**
	 * Méthode qui test si les coordonnées passés sont correctes
	 * 
	 * @param x La coordonnée X a tester
	 * @param y La coordonnée Y a tester
	 * @return true si les coordonnees sont correctes, false sinon
	 */
	public boolean testXY(int x, int y) {
		return (x >= 0 && y >= 0 && x < this.xMax && y < this.yMax);
	}

	/**
	 * Methode qui permet de placer un tableau de cellules (preceptes)
	 * 
	 * @param x      La coordonnee x initiale
	 * @param y      La coordonnee y initiale
	 * @param coords Les coordonnees des cases du precepte
	 */
	public void placerPreceptes(int x, int y, int[][] coords) {
		// On sauvegarde avant de faire l'action
		this.saveEtatGrille();
		for (int[] cellule : coords) {
			this.setCellule(x + cellule[0], y + cellule[1]);
		}
	}

	/**
	 * Methode qui permet de sauvegarder le grille dans un fichier
	 * 
	 * @param f L'emplacement du fichier de destination
	 */
	public void sauvegarderGrille(File f) {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(new GrilleSauvegarde(this.grille));
			oos.flush();
			oos.close();
		} catch (IOException erreur) {
			// Normalement on ne devrait pas avoir de souci mais
			// si c'est le cas on ne fait rien
		}
	}

	/**
	 * Methode qui permet de charger une autre confirguration sauvergadee dans un
	 * fichier
	 * 
	 * @param f L'emplacement du fichier source
	 */
	public void chargerGrille(File f) {
		Cellule[][] buffer = this.grille;
		try {
			// Ouverture du stream
			FileInputStream fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			// On recupere l'objet du fichier
			GrilleSauvegarde save = ((GrilleSauvegarde) ois.readObject());
			this.grille = save.rechargerGrille();
			// fermeture du stream
			ois.close();
			// On recupere les lignes et les colonnes
			this.xMax = save.getXMax();
			this.yMax = save.getYMax();
			// On remet a 0 le compteur des cellules vivantes
			this.resetCompteurCellulesVivantes();
			// On repart de 0 avec les generations
			this.generations = 0;
		} catch (IOException erreur) {
			erreur.printStackTrace();
			// Normalement on ne devrait pas avoir de souci mais
			// si c'est le cas on ne fait que remmettre la grille a son etat d'origine
			// La grille reste intacte
			this.grille = buffer;
		} catch (ClassNotFoundException erreur) {
			erreur.printStackTrace();
			// Normalement on ne devrait pas avoir de souci mais
			// si c'est le cas on ne fait que remmettre la grille a son etat d'origine
			// La grille reste intacte
			this.grille = buffer;
		}

		// On notifie tous les observateurs
		this.notifieObservateurs();
	}

	/**
	 * Methode qui permet d'enregistrer l'etat de la grille pour pouvoir revenir en
	 * arriere Action ctrl+s
	 */
	public void saveEtatGrille() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(new GrilleSauvegarde(this.grille));
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();

			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);

			this.listePrecedentsEtats.add(((GrilleSauvegarde) new ObjectInputStream(bais).readObject()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Methode qui permet de charger l'etat precedent de la grille Action ctrl+z
	 */
	public void reloadEtatGrille() {
		// Si il y reste des etats dans la liste buffer
		if (!this.listePrecedentsEtats.isEmpty()) {
			GrilleSauvegarde save = this.listePrecedentsEtats.get(this.listePrecedentsEtats.size() - 1);
			this.grille = save.rechargerGrille();
			this.xMax = save.getXMax();
			this.yMax = save.getYMax();
			this.listePrecedentsEtats.remove(this.listePrecedentsEtats.size() - 1);
		}
		this.notifieObservateurs();
	}

	/*
	 * ====================================== = = = = = PARTIE - CELLULES = = = = =
	 * = = ======================================
	 */

	/**
	 * Méthode qui fait naitre une cellule à la coordonnée {x; y}
	 * 
	 * @param X la coordonnée X
	 * @param Y la coordonnée Y
	 */
	public void setCellule(int x, int y) {
		// Si la cellule n'est pas valide on annule la requete
		if (!testXY(x, y))
			return;
		// On fait naitre la celulle
		this.grille[x][y].vit();
		// On notifie les observateurs
		this.notifieObservateurs();
	}

	/**
	 * Méthode qui fait mourir une cellule à la coordonnée {x; y}
	 * 
	 * @param X la coordonnée X
	 * @param Y la coordonnée Y
	 */
	public void cutCellule(int x, int y) {
		// Si la cellule n'est pas valide on annule la requete
		if (!testXY(x, y))
			return;
		// On fait naitre la celulle
		this.grille[x][y].meurt();
		// On notifie les observateurs
		this.notifieObservateurs();
	}

	/**
	 * Méthode qui change l'etat d'une cellule à la coordonnée {x; y}
	 * 
	 * @param X la coordonnée X
	 * @param Y la coordonnée Y
	 */
	public void changerEtat(int x, int y) {
		if (this.grille[x][y].estVivante()) {
			// Si la cellule est vivante, elle meurt
			this.grille[x][y].meurt();
		} else {
			// Sinon elle vit
			this.grille[x][y].vit();
		}
		// On notifie les observateurs pour faire la mise a jour
		this.notifieObservateurs();
	}

	/**
	 * Methode qui permet de demander un reset Comme cette version du jeu de la vie
	 * utilise des "commandes" si on demande le reset a un instant T et qu'il reste
	 * des commandes alors il y aura des cellules qui vont revivre malgre le reset
	 * La variable reset est mise a true et quand il n'y aura plus de commandes Un
	 * reset sera execute
	 */
	public void demanderReset() {
		this.reset = true;
	}

	/**
	 * Méthode qui permet d'incrémenter le nombre de cellules totale en vie
	 */
	public void incremente() {
		this.nbCellulesVivantes++;
	}

	/**
	 * Methode qui permet de remettre a 0 le compteur des cellules vivantes
	 */
	public void resetCompteurCellulesVivantes() {
		this.nbCellulesVivantes = 0;
	}

	/**
	 * GETTER du nombres de cellules vivante
	 * 
	 * @return Le nombre de cellules vivantes
	 */
	public int getNbCellulesVivantes() {
		return this.nbCellulesVivantes;
	}

	/**
	 * GETTER du nombres de générations
	 * 
	 * @return Le nombre de generations
	 */
	public int getNbGenerations() {
		return this.generations;
	}

	/**
	 * Méthode qui calcule la génération suivante
	 */
	public void calculerGenerationSuivante() {
		// On reset le compteur de cellules vivantes
		this.resetCompteurCellulesVivantes();
		// On incremente le nombre de generations
		this.generations++;
		// Distribution des visiteurs
		this.distribueVisiteur();
		// Execution de toutes les commandes
		this.executeCommandes();
		// Si un reset a ete demande il sera execute ici
		if (this.reset) {
			this.reset = false;
			this.resetGrille();
		}
		// On notifie tous les observateurs
		this.notifieObservateurs();
	}

	/*
	 * ====================================== = = = = = PARTIE - OBSERVATEURS = = =
	 * = = = = ======================================
	 */

	/**
	 * Méthode qui ajoute un observateur
	 * 
	 * @param o L'observateur a ajouter
	 */
	public void attacheObservateur(Observateur o) {
		this.observateurs.add(o);
		// On l'actualise une premiere fois -> initialisation
		o.actualise();
	}

	/**
	 * Méthode qui retire un observateur
	 * 
	 * @param o L'observateur a retirer
	 */
	public void detacheObservateur(Observateur o) {
		// On verifie si l'observateur est présent dans la liste
		if (this.observateurs.contains(o)) {
			this.observateurs.remove(o);
		}
	}

	/**
	 * Méthode qui notifie tous les observateurs
	 */
	public void notifieObservateurs() {
		// On parcours tous les observateurs pour les notifier
		for (Observateur o : this.observateurs) {
			o.actualise();
		}
	}

	/*
	 * ====================================== = = = = = PARTIE - COMMANDES = = = = =
	 * = = ======================================
	 */

	/**
	 * Méthode qui ajoute une commande à la liste
	 * 
	 * @param c La commande a ajouter
	 */
	public void ajouteCommande(Commande c) {
		this.commandes.add(c);
	}

	/**
	 * Méthode qui exectute les commandes
	 */
	public void executeCommandes() {
		// On parcours toutes les commandes
		for (Commande c : this.commandes) {
			c.executer();
		}
		// On supprime toutes les commandes
		this.commandes.clear();
	}

	/*
	 * ====================================== = = = = = PARTIE - VISITEURS = = = = =
	 * = = ======================================
	 */

	/**
	 * La méthode permet de changer le visiteur du jeu de la vie
	 * 
	 * @param v Le nouveau visiteur
	 */
	public void setVisiteur(Visiteur v) {
		if (this.visiteur != null)
			this.visiteur.masquerParametres();
		this.visiteur = v;
		this.visiteur.afficherParametres();
	}

	/**
	 * La méthode permet de faire visiter le visiteur sur toutes les cellules
	 */
	public void distribueVisiteur() {
		for (int x = 0; x < this.xMax; x++) {
			for (int y = 0; y < this.yMax; y++) {
				this.grille[x][y].accepte(this.visiteur);
			}
		}
	}
}