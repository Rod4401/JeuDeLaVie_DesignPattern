package JeuDeLaVie;

import java.io.Serializable;
/**
 * @author Meunier
 * @version 0.1 : Date : Fri Apr 01 21:23:09 CEST 2022
 *
 */
import java.util.ArrayList;
import java.util.List;

import Cellule.Cellule;
import Cellule.CelluleEtatMort;

/**
 * La classe GrilleSauvegarde permet de reduire la grille a sauvegarder en une
 * liste de cellule vivante L'objectif est de ne pas sauvegarder de cellule
 * morte car elles ne servent a rien (on peut facilement les refaires)
 */
@SuppressWarnings("serial")
public class GrilleSauvegarde implements Serializable {
	/**
	 * La liste des cellules vivantes
	 */
	private final List<Cellule> listeDesCellulesVivantes;

	/**
	 * Le nombre de lignes de la grille a sauvegarder
	 */
	private final int xMax;

	/**
	 * Le nombre de colonnes de la grille a sauvegarder
	 */
	private final int yMax;

	/**
	 * Le constructeur de la classe
	 * 
	 * @param grille La grille a sauvegarder
	 */
	public GrilleSauvegarde(Cellule[][] grille) {
		this.listeDesCellulesVivantes = new ArrayList<Cellule>();
		// On sauvegarde la taille
		this.xMax = grille.length;
		this.yMax = grille[0].length;
		// On parcours toute la grille a la recherche des cellules vivantes
		for (Cellule[] ligne : grille) {
			for (Cellule cel : ligne) {
				if (cel.estVivante()) {
					this.listeDesCellulesVivantes.add(cel);
				}
			}
		}
	}

	/**
	 * La methode permet de retourner la grille a son etat d'origine avec les la
	 * liste des cellules vivantes sauvegardee
	 * 
	 * @return La grille sous forme de tableau prete a l'emploi
	 */
	public Cellule[][] rechargerGrille() {
		// Creation de la nouvelle grille
		Cellule[][] nouvelleGrille = new Cellule[this.xMax][this.yMax];
		// On ajoute les cellules vivantes dans la grille a leur emplacement d'origine
		for (Cellule c : this.listeDesCellulesVivantes) {
			nouvelleGrille[c.getX()][c.getY()] = c;
		}
		// Pour les autres cases "les restantes" on creer des cellules mortes
		for (int x = 0; x < this.xMax; x++) {
			for (int y = 0; y < this.yMax; y++) {
				if (nouvelleGrille[x][y] == null) {
					nouvelleGrille[x][y] = new Cellule(x, y, CelluleEtatMort.getInstance());
				}
			}
		}
		return nouvelleGrille;
	}

	/**
	 * Getter du nombre de lignes de la grille
	 * 
	 * @return Le nombre de lignes
	 */
	public int getXMax() {
		return this.xMax;
	}

	/**
	 * Getter du nombre de colonnes
	 * 
	 * @return Le nombre de colonnes
	 */
	public int getYMax() {
		return this.yMax;
	}
}