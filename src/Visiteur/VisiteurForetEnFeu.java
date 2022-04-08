package Visiteur;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Cellule.Cellule;
import Cellule.Etat;
import Commande.CommandeMeurt;
import JeuDeLaVie.JeuDeLaVie;
import Main.Fenetre;

/**
 * Le VisiteurForet permet de realiser la strategie foret en feu Dans cette
 * version, les cellules meme vivante ont plusieurs "etat" Ils sont : Arbre
 * vivant, Arbre en feu et arbre en cendre Apres un certain temps, un arbre en
 * cendre s'envole avec le vent et disparait
 */
public class VisiteurForetEnFeu extends Visiteur {
	/**
	 * La methode qui permet de masquer les parametres de ce visiteur
	 */
	@Override
	public void masquerParametres() {
		popup.setVisible(false);
	}

	/**
	 * La methode qui permet d'afficher les parametres de ce visiteur
	 */
	@Override
	public void afficherParametres() {
		popup.setVisible(true);
		// Si on a choisi l'observateur fenetre
		if (Fenetre.getInstance() != null)
			Fenetre.getInstance().setTitle(Fenetre.getProperties().getProperty("windowBaseName")
					+ Fenetre.getProperties().getProperty("ActionStratForetEnFeu.windowname"));
	}

	/**
	 * La popup d'option de ce visiteur
	 */
	private static JFrame popup;

	// Les variables statiques en rapport avec ce visiteur
	private static double PROPAGATION_FEU = 0.1;
	private static int DUREE_EPUISEMENT_FEU = 15;
	private static int DUREE_MAXIMAL_CENDRE = 40;
	private static double PROBABILITE_FEU = 1;

	// Les spinner de la popup des parametres
	private static JSpinner spinner_PROPAGATION_FEU;
	private static JSpinner spinner_DUREE_EPUISEMENT_FEU;
	private static JSpinner spinner_DUREE_MAXIMAL_CENDRE;
	private static JSpinner spinner_PROBABILITE_FEU;

	/**
	 * La methode permet de creer la popup des parametres de ce visiteur
	 */
	private static void creerPopup() {
		JPanel buffer;
		// Fenetre.getProperties().getProperty("popupParametrageStategieForetEnFeu.title")
		popup = new JFrame(Fenetre.getProperties().getProperty("popupParametrageStategieForetEnFeu.windowName"));
		popup.setMinimumSize(new Dimension(250, 300));
		popup.setResizable(false);
		popup.setAlwaysOnTop(true);
		JPanel panelGlobal = new JPanel();
		panelGlobal.setLayout(new BoxLayout(panelGlobal, BoxLayout.PAGE_AXIS));
		// On commence par le panel qui contient un label "titre"
		buffer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buffer.add(new JLabel(Fenetre.getProperties().getProperty("popupParametrageStategieForetEnFeu.title")));
		panelGlobal.add(buffer);

		// Le panel du taux de propagation du feu
		buffer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buffer.add(
				new JLabel(Fenetre.getProperties().getProperty("popupParametrageStategieForetEnFeu.PROPAGATION_FEU")));
		spinner_PROPAGATION_FEU = new JSpinner(new SpinnerNumberModel(0.1, // valeur initiale
				0, // valeur minimum
				1, // valeur maximum
				0.01 // pas
		));
		spinner_PROPAGATION_FEU.setPreferredSize(new Dimension(80, 20));
		spinner_PROPAGATION_FEU.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				PROPAGATION_FEU = (double) spinner_PROPAGATION_FEU.getValue();
			}
		});
		buffer.add(spinner_PROPAGATION_FEU);
		panelGlobal.add(buffer);

		// Le panel duree epuisement du feu
		buffer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buffer.add(new JLabel(
				Fenetre.getProperties().getProperty("popupParametrageStategieForetEnFeu.DUREE_EPUISEMENT_FEU")));
		spinner_DUREE_EPUISEMENT_FEU = new JSpinner(new SpinnerNumberModel(15, // valeur initiale
				0, // valeur minimum
				1000, // valeur maximum
				5 // pas
		));
		spinner_DUREE_EPUISEMENT_FEU.setPreferredSize(new Dimension(80, 20));
		spinner_DUREE_EPUISEMENT_FEU.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				DUREE_EPUISEMENT_FEU = (int) spinner_DUREE_EPUISEMENT_FEU.getValue();
			}
		});
		buffer.add(spinner_DUREE_EPUISEMENT_FEU);
		panelGlobal.add(buffer);

		// Le panel duree maximal cendres
		buffer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buffer.add(new JLabel(
				Fenetre.getProperties().getProperty("popupParametrageStategieForetEnFeu.DUREE_MAXIMAL_CENDRE")));
		spinner_DUREE_MAXIMAL_CENDRE = new JSpinner(new SpinnerNumberModel(40, // valeur initiale
				0, // valeur minimum
				1000, // valeur maximum
				5 // pas
		));
		spinner_DUREE_MAXIMAL_CENDRE.setPreferredSize(new Dimension(80, 20));
		spinner_DUREE_MAXIMAL_CENDRE.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				DUREE_MAXIMAL_CENDRE = (int) spinner_DUREE_MAXIMAL_CENDRE.getValue();
			}
		});
		buffer.add(spinner_DUREE_MAXIMAL_CENDRE);
		panelGlobal.add(buffer);

		// Le panel probabilite apparition du feu
		buffer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buffer.add(
				new JLabel(Fenetre.getProperties().getProperty("popupParametrageStategieForetEnFeu.PROBABILITE_FEU")));
		spinner_PROBABILITE_FEU = new JSpinner(new SpinnerNumberModel(10, // valeur initiale
				0, // valeur minimum
				1000000, // valeur maximum
				1 // pas
		));
		spinner_PROBABILITE_FEU.setPreferredSize(new Dimension(80, 20));
		spinner_PROBABILITE_FEU.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				PROBABILITE_FEU = (double) spinner_PROBABILITE_FEU.getValue();
			}
		});
		buffer.add(spinner_PROBABILITE_FEU);

		panelGlobal.add(buffer);

		popup.add(panelGlobal);
	}

	/**
	 * Comme on souhaite pouvoir changer de visiteur Il semble evident de faire le
	 * pattern Singleton pour eviter de creer des instances a outrance
	 */
	private static VisiteurForetEnFeu instance;

	/**
	 * Methode qui permet d'obtenir l'instance de ce visiteur
	 * 
	 * @param jeu Le jeu
	 */
	public static VisiteurForetEnFeu getInstance(JeuDeLaVie jeu) {
		if (popup == null) {
			creerPopup();
		}
		if (instance == null) {
			instance = new VisiteurForetEnFeu(jeu);
		}
		popup.setVisible(true);
		return instance;
	}

	/**
	 * Constructeur de la classe est privé
	 * 
	 * @param jeu Le jeu
	 */
	private VisiteurForetEnFeu(JeuDeLaVie jeu) {
		super(jeu);
	}

	/**
	 * Méthode qui permet au visiteur de visiter une cellule vivante
	 * 
	 * @param c La cellule qu'il visite
	 */
	public void visiteCelluleVivante(Cellule c) {
		// On regarde quel est l'etat de la cellule
		Etat etat = c.getEtat();
		c.vieillir();
		int age = c.getAge();
		// Le cas ou la cellule represente un arbre en vie
		if (etat == Etat.ARBRE_VIVANT) {
			// On recherche si une cellule dans le voisinage est en feu
			for (int x = c.getX() - 1; x <= c.getX() + 1; x++) {
				for (int y = c.getY() - 1; y <= c.getY() + 1; y++) {
					// On teste si la cellule est un arbre en feu
					if (this.jeu.testXY(x, y) && this.jeu.getGrilleXY(x, y).getEtat() == Etat.ARBRE_EN_FEU) {
						// L'arbre vivant a 1 chance sur TAUX_CHANGEMENT/1 de devenir en feu
						if (Math.random() < PROPAGATION_FEU) {
							// On reset son age pour compter
							c.resetAge();
							c.changerEtat(Etat.ARBRE_EN_FEU);
							return;
						}
					} else {
						// L'homme est parfois vilain et jette n'importe quoi dans la foret
						// Cela provoque parfois un depart de feu
						if (Math.random() < PROBABILITE_FEU / 1000000) {
							c.changerEtat(Etat.ARBRE_EN_FEU);
						}
					}
				}
			}
			return;
		}
		// Le cas ou la cellule represente un arbre en feu
		if (etat == Etat.ARBRE_EN_FEU) {
			if (age > DUREE_EPUISEMENT_FEU) {
				// On reset son age pour compter
				c.resetAge();
				c.changerEtat(Etat.ARBRE_EN_CENDRES);
			}
			return;
		}
		// Le cas ou la cellule represente un arbre en cendres
		if (etat == Etat.ARBRE_EN_CENDRES) {
			// On pourrait mettre un egal plutot qu'un superieur ou egal
			// mais si jamais on depasse, les cendres ne disparaitrons jamais
			if (age >= DUREE_MAXIMAL_CENDRE) {
				jeu.ajouteCommande(new CommandeMeurt(c));
			}
			return;
		}

		// Si on est arrive ici alors la cellule est dans un etat inconnu pour cette
		// version
		// C'est surtout quand on place des cellules, elles sont par default inconnu
		// On change donc la cellule dans un etat d'arbre vivant
		c.changerEtat(Etat.ARBRE_VIVANT);
	}

	/**
	 * Méthode qui permet au visiteur de visiter une cellule morte
	 * 
	 * @param c La cellule qu'il visite
	 */
	public void visiteCelluleMorte(Cellule c) {
		// On ne fait rien
	}
}