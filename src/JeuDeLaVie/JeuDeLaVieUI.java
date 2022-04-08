package JeuDeLaVie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 10:41:00 CET 2022
 *
 */
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import Action.ActionsMenu;
import Cellule.Etat;
import Main.Fenetre;
import Observateur.Observable;
import Observateur.Observateur;

/**
 * La classe JeuDeLaVieUI est un observateur du jeu de la vie, cela lui permet
 * d'etre mis a jour des qu'il y a un changement sur la grille. C'est egalement
 * un JPanel car il agit comme un tableau/panneau pour l'interface graphique
 * C'est lui qui permet a l'utilisateur de visualiser la grille
 */
@SuppressWarnings("serial")
public class JeuDeLaVieUI extends JPanel implements Observateur, Observable {

	private java.util.List<Observateur> observateurs;

	/**
	 * La couleur par default du system
	 */
	private static Color COLOR_DEFAULT;
	/**
	 * Min et max du zoom
	 */
	// le zoom minimum est 3 car en 1 les cellules ne sont pas visibles et en 2
	// elles sont pas rondes
	private static int MinZoom = 3;
	private static int MaxZoom = 50;

	/**
	 * Le jeu de la vie qu'observe cet observateur
	 */
	private JeuDeLaVie jeu;

	/**
	 * Variable lie au zoom
	 */
	private int zoom = 4;

	/**
	 * Les decalages X et Y lorsque la taille de la fenetre depasse celle de la
	 * grille
	 */
	private int decalageX, decalageY;

	/**
	 * La popup Menu pour le clique Droit avec les variables de localisation du
	 * click
	 */
	private JPopupMenu popupMenu;
	private int xClick;
	private int yClick;

	/**
	 * Boolean pour afficher ou non le quadrillage
	 */
	private boolean quadriller = false;

	/**
	 * Le constructeur de la classe
	 * 
	 * @param jeu Le jeu relié à cet observateur
	 */
	public JeuDeLaVieUI(JeuDeLaVie jeu) {
		super();
		this.jeu = jeu;

		// Creation de la liste des observateurs
		this.observateurs = new java.util.LinkedList<Observateur>();
		/*
		 * Evenements
		 */

		/**
		 * Evenement deplacement souris Cette ecouteur sert principalement pour savoir
		 * si l'utilisateur fait un "drag" avec la souris pour appliquer l'option de
		 * dessin (gomme ou crayon)
		 */
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				// Si on drag avec le bouton gauche
				if (!SwingUtilities.isRightMouseButton(e)) {
					appliquerDessinCellule(e.getX() - decalageX, e.getY() - decalageY);
				}
			}
		});

		/**
		 * Evenement click souris Cette ecouteur sert pour dessiner une cellule du click
		 * gauche ou placer un precepte du clique droit
		 */
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// Les coordonnees de la cellule visee par le click
				xClick = (e.getX() - decalageX) / zoom;
				yClick = (e.getY() - decalageY) / zoom;
				if (SwingUtilities.isRightMouseButton(e)) {
					/*
					 * Clique droit
					 */
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				} else {
					/*
					 * Clique gauche On fait changer d'etat la cellule visee
					 */
					// On sauvegarde avant de faire l'action
					jeu.saveEtatGrille();
					appliquerDessinCellule(e.getX() - decalageX, e.getY() - decalageY);
				}
			}
		});

		/**
		 * Evenement de la molette souris -> pour le zoom
		 */
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// On regarde si la touche controle est enclanchee
				if (e.isControlDown()) {
					zoomer(e.getWheelRotation(), e.getPoint());
				}
			}
		});
	}

	/**
	 * La methode initComponents permet d'initialiser les composants de ce composant
	 * graphique Avec l'utilisation d'actions, il faut que les composants
	 * "principaux" soient crees avant d'autre. Cette methode permet d'initialiser
	 * les composants "secondaires"
	 */
	public void initComponents() {
		// Creation de la popupMenu pour le click droit
		this.popupMenu = createPopupMenu();
	}

	/**
	 * La methode appliquerDessinCellule permet d'appliquer l'option de dessin sur
	 * la cellule dont les coordonnees sont passees en parametres
	 * 
	 * @param x La coordonnee x de la cellule
	 * @param y La coordonnee y de la cellule
	 */
	private void appliquerDessinCellule(int x, int y) {
		if (jeu.testXY(x / zoom, y / zoom)) {
			jeu.getGrilleXY(x / zoom, y / zoom).accepte(Fenetre.getInstance().getOptionDessin());
		}
	}

	/**
	 * Methode qui permet de creer une popupMenu pour le click droit
	 * 
	 * @return une JPopupMenu
	 */
	private JPopupMenu createPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();

		popupMenu.add(ActionsMenu.MNU_PRECEPTES);
		return popupMenu;
	}

	/**
	 * Méthode qui permet d'actualiser cet observateur
	 */
	@Override
	public void actualise() {
		repaint();
	}

	/**
	 * Méthode pour dessiner la grille
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (COLOR_DEFAULT == null) {
			COLOR_DEFAULT = g.getColor();
		}
		this.jeu.resetCompteurCellulesVivantes();
		// On regarde si la taille de la grille est trop petite pour couvir l'ecran
		// Si c'est le cas on decale pour center
		// Calcul du decalage en largeur
		this.decalageX = 0;
		if (this.jeu.getXMax() * this.zoom < this.getWidth()) {
			this.decalageX = (this.getWidth() - this.jeu.getXMax() * this.zoom) / 2;
		}
		// Calcul du decalage en hauteur
		this.decalageY = 0;
		if (this.jeu.getYMax() * this.zoom < this.getHeight()) {
			this.decalageY = (this.getHeight() - this.jeu.getYMax() * this.zoom) / 2;
		}

		// Le contour
		g.setColor(Color.BLACK);
		g.drawRect(this.decalageX, this.decalageY, this.jeu.getXMax() * this.zoom, this.jeu.getYMax() * this.zoom);
		// La couleur de la cellule
		Color c;
		for (int x = 0; x < this.jeu.getXMax(); x++) {
			for (int y = 0; y < this.jeu.getYMax(); y++) {
				if (this.jeu.getGrilleXY(x, y).estVivante()) {
					// Si la couleur de la cellule n'est pas la couleur par default
					if (this.jeu.getGrilleXY(x, y).getColor() != Etat.DEFAULT.color) {
						// alors on la dessine de sa couleur
						c = this.jeu.getGrilleXY(x, y).getColor();
					} else {
						// Sinon on prend la couleur par default
						c = COLOR_DEFAULT;
					}
					g.setColor(c);

					g.fillOval(this.decalageX + x * this.zoom, this.decalageY + y * this.zoom, this.zoom, this.zoom);
					this.jeu.incremente();
				}
				if (this.quadriller) {
					g.setColor(Color.BLACK);
					g.drawLine(this.decalageX + x * this.zoom + this.zoom, this.decalageY + y * this.zoom,
							this.decalageX + x * this.zoom + this.zoom, this.decalageY + y * this.zoom + this.zoom);
					g.drawLine(this.decalageX + x * this.zoom, this.decalageY + y * this.zoom + this.zoom,
							this.decalageX + x * this.zoom + this.zoom, this.decalageY + y * this.zoom + this.zoom);
				}
			}
		}
		this.notifieObservateurs();
	}

	/**
	 * La methode zoomer permet de zoomer sur la grille
	 * 
	 * @param orientation (-1 ou 1) represente l'orientation de la molette
	 * @param p           Le point de zoom
	 */
	private void zoomer(int orientation, Point p) {
		// On recupere les dimension du JViewport (du scrollPane -> le parent) pour les
		// calculs
		Rectangle rectPrecedent = ((JViewport) getParent()).getViewRect();
		// On bufferise l'ancien zoom
		int ancienZoom = this.zoom;
		// On applique l'orientation
		this.zoom -= orientation;
		// On empeche la variable zoom de depacer les limites
		if (this.zoom < MinZoom) {
			this.zoom = MinZoom;
		}
		if (this.zoom > MaxZoom) {
			this.zoom = MaxZoom;
		}
		// On actualise l'affichage -> On redessine
		actualise();
		// Changement de la size preferee pour zoomer ou dezoomer
		// Cette methode fait changer la taille du jpanel en fonction du zoom
		// Mais ne change pas la position de la vue
		setPreferredSize(new Dimension(this.jeu.getXMax() * this.zoom, this.jeu.getYMax() * this.zoom));

		// Pour eviter les mouvements et plus particulierement pour ne rien faire quand
		// le zoom ne change pas
		// Si le zoom est different du precedent donc si un nouveau zoom est demande
		// alors on le recalcul
		if (this.zoom != ancienZoom) {
			// distanceX represente la distance en x entre le point precedent de zoom et le
			// nouveau
			// ex : Si on zoom avec la souris en {500;500} sur une grille de 100 cellules
			// avec un zoom de 10 donc 1000px;1000px
			// La grille va donc faire par la suite 10+1 = 11 pour le zoom donc 100*11 =
			// 1100px pour la coordonnee x
			// Donc le point 500;500 ne sera plus le centre, il faudra donc deplacer la vue
			// de 500 - (500/10) * 11 = 50 pour la coordonnee X
			int distanceX = p.x - (p.x / ancienZoom) * this.zoom;
			// On fait de meme avec la coordonnee y
			int distanceY = p.y - (p.y / ancienZoom) * this.zoom;
			// Le point k sera donc notre nouveau point et on l'applique a notre JViewport
			Point k = new Point(rectPrecedent.x - distanceX, rectPrecedent.y - distanceY);
			((JViewport) getParent()).setViewPosition(k);
		}
		// On fait reagir le layout de la scrollBar qui est le parent de ce panel
		// affichage de la grille --> panelGrille(scrollPane(ce JPanel))
		getParent().doLayout();
	}

	/**
	 * Methode qui permet d'obtenir la coordonnee x de depart de la popup menu
	 * 
	 * @return La coordonnee x du click
	 */
	public int getXClick() {
		return this.xClick;
	}

	/**
	 * Methode qui permet d'obtenir la coordonnee y de depart de la popup menu
	 * 
	 * @return La coordonnee y du click
	 */
	public int getYClick() {
		return this.yClick;
	}

	/**
	 * Methode qui permet de sauvegarder la grille dans une image Elle permet de
	 * faire une capture de la grille sous forme png
	 * 
	 * @param f L'emplacement du fichier de destination
	 */
	public void saveImage(File f) {
		// Creation du image bufferisee
		BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		// On creer un composant graphique a partir de l'image
		Graphics2D g2 = image.createGraphics();
		// On applique la methode paint de ce composant pour le dessiner dans l'image
		paint(g2);

		try {
			// On tente d'ecrire l'image dans le fichier
			ImageIO.write(image, "png", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode qui permet de changer l'etat du boolean qui permet d'afficher ou non
	 * le quadrillage On fait une porte NO
	 */
	public void changeQuadrillage() {
		this.quadriller = !this.quadriller;
	}

	/**
	 * La methode permet d'ajouter un observateur
	 * 
	 * @param o L'observateur a attacher
	 */
	public void attacheObservateur(Observateur o) {
		this.observateurs.add(o);
		// On l'actualise une premiere fois -> initialisation
		o.actualise();
	}

	/**
	 * La methode permet de retirer un observateur
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
	 * La methode permet d'indiquer a tous les observateurs un changement
	 */
	public void notifieObservateurs() {
		// On parcours tous les observateurs pour les notifier
		for (Observateur o : this.observateurs) {
			o.actualise();
		}
	}
}