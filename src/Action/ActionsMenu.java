package Action;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Meunier
 * @version 0.1 : Date : Sun Mar 27 09:00:34 CEST 2022
 *
 */
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import Cellule.Cellule;
import Commande.CommandeMeurt;
import Commande.CommandeVit;
import Main.Fenetre;
import Visiteur.Visiteur;
import Visiteur.VisiteurClassique;
import Visiteur.VisiteurCrayon;
import Visiteur.VisiteurDayNight;
import Visiteur.VisiteurForetEnFeu;
import Visiteur.VisiteurGomme;
import Visiteur.VisiteurHighLife;

/**
 * La classe ActionsMenu ne possede pas d'instance et est abstraite Cette classe
 * permet de creer les actions pour l'ensemble des composants de notre jeu de la
 * vie
 */
public abstract class ActionsMenu {

	/**
	 * La constante pour le mnemonic vide Permet de ne pas utiliser null
	 */
	private static final int MNEMONIC_VIDE = -1;

	/**
	 * Les instances uniques des differents menus
	 */
	public static JMenu MNU_PRECEPTES;

	/**
	 * Les instances uniques (Singletons) pour eviter de creer trop d'instance
	 * inutiles Elles sont public car ce n'est pas reserve a la classe surtout que
	 * celle-ci ne s'en sert pas
	 */
	public static AbstractAction ACT_NEXTSTEP;
	public static AbstractAction ACT_PLAYPAUSE;
	public static AbstractAction ACT_RAND;
	public static AbstractAction ACT_RESET;
	public static AbstractAction ACT_CHARGER;
	public static AbstractAction ACT_SAUVEGARDER;
	public static AbstractAction ACT_TOIMAGE;
	public static AbstractAction ACT_TOOLBAR_AFFICHER;
	public static AbstractAction ACT_TOOLBAR_MASQUER;
	public static AbstractAction ACT_CRAYON;
	public static AbstractAction ACT_GOMME;
	public static AbstractAction ACT_QUADRILLAGE;
	public static AbstractAction ACT_CREATION_STRATEGIE;
	public static AbstractAction ACT_SRATEGIE_DEFAULT_CLASSIQUE;
	public static AbstractAction ACT_SRATEGIE_DEFAULT_HIGHLIFE;
	public static AbstractAction ACT_SRATEGIE_DEFAULT_DAYNIGHT;
	public static AbstractAction ACT_SRATEGIE_DEFAULT_FORETENFEU;

	/**
	 * Liste des strings pour les preceptes Ce sont des "modeles"
	 */
	private static Map<String, int[][]> listePreceptes = new Hashtable<String, int[][]>();

	/**
	 * Methode pour facilite la relecture
	 */
	private static String getProperty(String s) {
		return Fenetre.getProperties().getProperty(s);
	}

	/**
	 * Insertion des valeurs dans la table de hachage des preceptes
	 */
	static {
		// Ouverture du fichier
		String[] nomsPrecepts = getProperty("listeNomsPrecepts").split(";");
		int[][] tabCoords;
		String[] coords;
		for (String precept : nomsPrecepts) {
			// On recupere la chaine de caracteres qui contient les coordonnes
			coords = getProperty(precept).split(";");
			tabCoords = new int[coords.length][2];
			for (int i = 0; i < coords.length; i++) {
				tabCoords[i] = convertStringToCoords(coords[i]);
			}
			listePreceptes.put(precept, tabCoords);
		}
	}

	/**
	 * Methode qui transforme une chaine de caratere du type : "x,y" en un tableau
	 * de int
	 * 
	 * @param s La chaine a convertir
	 */
	private static int[] convertStringToCoords(String s) {
		// La chaine initiale ressemble a cela : "{0,0}"
		// On retire les accolades
		s = s.replace("{", "");
		s = s.replace("}", "");
		// On split les 2 entiers
		String[] coordFinal = s.split(",");
		// On recupere le premier entier | La coordonnee x
		int x = Integer.parseInt(coordFinal[0]);
		// On recupere le premier entier | La coordonnee x
		int y = Integer.parseInt(coordFinal[1]);
		return new int[] { x, y };
	}

	/**
	 * Methode qui permet de creer une action
	 * 
	 * @param name             Le nom de l'action
	 * @param shortDescription La description de l'action
	 * @param bloc             Les instructions a executer lorsque l'action est
	 *                         activee
	 * @return La nouvelle action creee
	 */
	@SuppressWarnings("serial")
	private static AbstractAction creerAction(String name, int mnemonic, String shortDescription, ActionExecute bloc) {
		return new AbstractAction() {
			{
				// On defini le nom de l'action
				putValue(Action.NAME, name);
				// Si il y a un mnemonic, on le defini
				if (mnemonic != MNEMONIC_VIDE) {
					putValue(Action.MNEMONIC_KEY, mnemonic);
				}
				// On defini la courte description
				// Elle est visible quand on laisse la souris sur un element
				putValue(Action.SHORT_DESCRIPTION, shortDescription);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				// On execute le bloc d'instructions
				bloc.executer();
				// Apres avoir executer le bloc, on rend le focus a la fenetre
				Fenetre.getInstance().requestFocus();
			}
		};
	}

	/**
	 * Methode qui permet de charger les actions
	 */
	public static void chargerActions() {
		/*
		 * Chargement de l'action sauvegarder
		 */
		if (ACT_SAUVEGARDER == null) {
			ACT_SAUVEGARDER = creerAction(getProperty("ActionEnregistrer.name"), KeyEvent.VK_E,
					getProperty("ActionEnregistrer.shortDescription"), () -> {
						// On creer un fileChooser pour demander a l'utilisateur de choisir
						// l'emplacement de sauvegarde
						JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
						jfc.setDialogTitle(getProperty("ActionEnregister.titleWindow"));
						jfc.setDragEnabled(false);
						jfc.setMultiSelectionEnabled(false);
						jfc.setSelectedFile(new File(getProperty("ActionEnregistrer.nameFileDefault")));
						int returnValue = jfc.showOpenDialog(null);
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							File selectedFile = jfc.getSelectedFile();
							Fenetre.getInstance().getJeu().sauvegarderGrille(selectedFile);

						}
					});
		}

		/*
		 * Chargement de l'action charger
		 */
		if (ACT_CHARGER == null) {
			ACT_CHARGER = creerAction(getProperty("ActionCharger.name"), KeyEvent.VK_C,
					getProperty("ActionCharger.shortDescription"), () -> {
						// On creer un fileChooser pour demander a l'utilisateur de choisir le fichier a
						// load
						JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
						jfc.setDialogTitle(getProperty("ActionCharger.titleWindow"));
						jfc.setDragEnabled(false);
						jfc.setMultiSelectionEnabled(false);
						int returnValue = jfc.showOpenDialog(null);
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							File selectedFile = jfc.getSelectedFile();
							Fenetre.getInstance().getJeu().chargerGrille(selectedFile);

						}
					});
		}

		/*
		 * Chargement de l'action capture to image
		 */
		if (ACT_TOIMAGE == null) {
			ACT_TOIMAGE = creerAction(getProperty("ActionToImage.name"), KeyEvent.VK_A,
					getProperty("ActionToImage.shortDescription"), () -> {
						// On creer un fileChooser pour demander a l'utilisateur de choisir
						// l'emplacement de sauvegarde
						JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
						jfc.setDialogTitle(getProperty("ActionToImage.titleWindow"));
						jfc.setDragEnabled(false);
						jfc.setMultiSelectionEnabled(false);
						jfc.setSelectedFile(new File(getProperty("ActionToImage.nameFileDefault")));
						int returnValue = jfc.showOpenDialog(null);
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							File selectedFile = jfc.getSelectedFile();
							Fenetre.getInstance().getJeuUI().saveImage(selectedFile);

						}
					});
		}

		/*
		 * Chargement de l'action afficher la toolbar
		 */
		if (ACT_TOOLBAR_AFFICHER == null)
			ACT_TOOLBAR_AFFICHER = creerAction(getProperty("ActionAfficher.name"), KeyEvent.VK_A,
					getProperty("ActionAfficher.shortDescription"), () -> {
						Fenetre.getInstance().afficherCommandes();
					});

		/*
		 * Chargement de l'action masquer la toolbar
		 */
		if (ACT_TOOLBAR_MASQUER == null)
			ACT_TOOLBAR_MASQUER = creerAction(getProperty("ActionMasquer.name"), KeyEvent.VK_M,
					getProperty("ActionMasquer.shortDescription"), () -> {
						Fenetre.getInstance().masquerCommandes();
					});

		/*
		 * Chargement de l'action reset la grille
		 */
		if (ACT_RESET == null) {
			ACT_RESET = creerAction(getProperty("ActionReset.name"), KeyEvent.VK_R,
					getProperty("ActionReset.shortDescription"), () -> {
						// Si le jeu n'est pas en pause on fait une demande de reset
						// En effet, si le jeu est en cours d'execution (avec un autre thread)
						// il va executer toutes ses commandes
						// Si on fait un reset, il reste des commandes en cours d'execution et
						// par consequent il restera des dechets (cellules en vie) apres le reset
						if (!Fenetre.getInstance().isPause())
							Fenetre.getInstance().getJeu().demanderReset();
						else
							Fenetre.getInstance().getJeu().resetGrille();
						Fenetre.getInstance().mettrePause();
					});
		}

		/*
		 * Chargement de l'action generer la grille aleatoire
		 */
		if (ACT_RAND == null) {
			ACT_RAND = creerAction(getProperty("ActionGenerer.name"), KeyEvent.VK_G,
					getProperty("ActionGenerer.shortDescription"), () -> {
						Fenetre.getInstance().mettrePause();
						Fenetre.getInstance().getJeu().saveEtatGrille();
						Fenetre.getInstance().getJeu().grilleRandomizer(Fenetre.getInstance().getTaux());
					});
		}

		/*
		 * Chargement de l'action play/pause
		 */
		if (ACT_PLAYPAUSE == null) {
			ACT_PLAYPAUSE = creerAction(getProperty("ActionPlay.name"), KeyEvent.VK_P,
					getProperty("ActionPlay.shortDescription"), () -> {
						Fenetre.getInstance().getJeu().saveEtatGrille();
						if (Fenetre.getInstance().isPause()) {
							Fenetre.getInstance().mettrePlay();
						} else {
							Fenetre.getInstance().mettrePause();
						}
					});
		}

		/*
		 * Chargement de l'action etape suivante
		 */
		if (ACT_NEXTSTEP == null) {
			ACT_NEXTSTEP = creerAction(getProperty("ActionNext.name"), KeyEvent.VK_N,
					getProperty("ActionNext.shortDescription"), () -> {
						Fenetre.getInstance().mettrePause();
						Fenetre.getInstance().getJeu().saveEtatGrille();
						Fenetre.getInstance().getJeu().calculerGenerationSuivante();
					});
		}

		/*
		 * Chargement de l'action selectionner le crayon
		 */
		if (ACT_CRAYON == null) {
			ACT_CRAYON = creerAction(getProperty("ActionCrayon.name"), KeyEvent.VK_C,
					getProperty("ActionCrayon.shortDescription"), () -> {
						Fenetre.getInstance()
								.setOptionDessin(VisiteurCrayon.getInstance(Fenetre.getInstance().getJeu()));
					});
		}

		/*
		 * Chargement de l'action selectionner la gomme
		 */
		if (ACT_GOMME == null) {
			ACT_GOMME = creerAction(getProperty("ActionGomme.name"), KeyEvent.VK_O,
					getProperty("ActionGomme.shortDescription"), () -> {
						Fenetre.getInstance()
								.setOptionDessin(VisiteurGomme.getInstance(Fenetre.getInstance().getJeu()));
					});
		}

		/*
		 * Chargement de l'action afficher le quadrillage
		 */
		if (ACT_QUADRILLAGE == null) {
			ACT_QUADRILLAGE = creerAction(getProperty("ActionQuadrillage.name"), KeyEvent.VK_O,
					getProperty("ActionQuadrillage.shortDescription"), () -> {
						Fenetre.getInstance().getJeuUI().changeQuadrillage();
						Fenetre.getInstance().getJeuUI().actualise();
					});
		}

		/*
		 * Chargement de l'action selectionner la strategie classique
		 */
		if (ACT_SRATEGIE_DEFAULT_CLASSIQUE == null) {
			ACT_SRATEGIE_DEFAULT_CLASSIQUE = creerAction(getProperty("ActionStratClassique.name"), MNEMONIC_VIDE,
					getProperty("ActionStratClassique.shortDescription"), () -> {
						Fenetre.getInstance().getJeu().saveEtatGrille();
						Fenetre.getInstance().getJeu()
								.setVisiteur(VisiteurClassique.getInstance(Fenetre.getInstance().getJeu()));
					});
		}

		/*
		 * Chargement de l'action selectionner la strategie highlife
		 */
		if (ACT_SRATEGIE_DEFAULT_HIGHLIFE == null) {
			ACT_SRATEGIE_DEFAULT_HIGHLIFE = creerAction(getProperty("ActionStratHighLife.name"), MNEMONIC_VIDE,
					getProperty("ActionStratHighLife.shortDescription"), () -> {
						Fenetre.getInstance().getJeu().saveEtatGrille();
						Fenetre.getInstance().getJeu()
								.setVisiteur(VisiteurHighLife.getInstance(Fenetre.getInstance().getJeu()));
					});
		}

		/*
		 * Chargement de l'action selectionner la strategie daynight
		 */
		if (ACT_SRATEGIE_DEFAULT_DAYNIGHT == null) {
			ACT_SRATEGIE_DEFAULT_DAYNIGHT = creerAction(getProperty("ActionStratDayNight.name"), MNEMONIC_VIDE,
					getProperty("ActionStratDayNight.shortDescription"), () -> {
						Fenetre.getInstance().getJeu().saveEtatGrille();
						Fenetre.getInstance().getJeu()
								.setVisiteur(VisiteurDayNight.getInstance(Fenetre.getInstance().getJeu()));
					});
		}

		/*
		 * Chargement de l'action selectionner la strategie foret en feu
		 */
		if (ACT_SRATEGIE_DEFAULT_FORETENFEU == null) {
			ACT_SRATEGIE_DEFAULT_FORETENFEU = creerAction(getProperty("ActionStratForetEnFeu.name"), MNEMONIC_VIDE,
					getProperty("ActionStratForetEnFeu.shortDescription"), () -> {
						Fenetre.getInstance().getJeu().saveEtatGrille();
						Fenetre.getInstance().getJeu()
								.setVisiteur(VisiteurForetEnFeu.getInstance(Fenetre.getInstance().getJeu()));
					});
		}

		/*
		 * Chargement de l'action creer une strategie
		 */
		if (ACT_CREATION_STRATEGIE == null) {
			ACT_CREATION_STRATEGIE = creerAction(getProperty("ActionCreationStrategie.name"), KeyEvent.VK_O,
					getProperty("ActionCreationStrategie.shortDescription"), () -> {
						// Creation de la popup
						JDialog popup = new JDialog(Fenetre.getInstance(), getProperty("popupNewStrategie.nom"));
						// On rend visible la popup
						popup.setVisible(true);
						// On centre la popup
						popup.setLocation((int) (Fenetre.getInstance().getWidth() * 0.4),
								(int) (Fenetre.getInstance().getHeight() * 0.4));
						// On defini sa taille
						popup.setMinimumSize(new Dimension(260, 300));
						// On rend la popup non redimentionnable
						popup.setResizable(false);
						// Creation du JPanel global
						JPanel panneauGlobal = new JPanel();
						panneauGlobal.setLayout(new BoxLayout(panneauGlobal, BoxLayout.PAGE_AXIS));
						// On ajoute le panneau global a la popup
						popup.setContentPane(panneauGlobal);

						JLabel label;

						// -----------------------------------
						// Creation du JPanel pour le nom
						// -----------------------------------
						JPanel panneauGetX = new JPanel();
						panneauGetX.setLayout(new FlowLayout(FlowLayout.LEFT));
						label = new JLabel(getProperty("popupNewStrategie.lblName"));
						label.setPreferredSize(new Dimension(80, 30));
						panneauGetX.add(label);

						// Creation du textField
						JTextField tfName = new JTextField();
						tfName.setPreferredSize(new Dimension(150, 20));
						panneauGetX.add(tfName);

						// On termine par ajouter le panneau au panneau globale
						panneauGlobal.add(panneauGetX);

						// -----------------------------------
						// Creation du JPanel pour le nombre de voisins necessaires pour la naissance
						// -----------------------------------
						JPanel panneauGetY = new JPanel();
						panneauGetY.setLayout(new FlowLayout(FlowLayout.LEFT));
						label = new JLabel(getProperty("popupNewStrategie.lblNaissance"));
						label.setPreferredSize(new Dimension(80, 30));
						panneauGetY.add(label);
						// Creation du textField
						JTextField tfNaissance = new JTextField();
						tfNaissance.setPreferredSize(new Dimension(150, 20));
						panneauGetY.add(tfNaissance);

						// On termine par ajouter le panneau au panneau globale
						panneauGlobal.add(panneauGetY);

						// -----------------------------------
						// Creation du JPanel pour le nombre de voisins necessaires pour la survie
						// -----------------------------------
						JPanel panneauGetZ = new JPanel();
						panneauGetZ.setLayout(new FlowLayout(FlowLayout.LEFT));
						label = new JLabel(getProperty("popupNewStrategie.lblSurvie"));
						label.setPreferredSize(new Dimension(80, 30));
						panneauGetZ.add(label);
						// Creation du textField
						JTextField tfSurvie = new JTextField();
						tfSurvie.setPreferredSize(new Dimension(150, 20));
						panneauGetZ.add(tfSurvie);

						// On termine par ajouter le panneau au panneau globale
						panneauGlobal.add(panneauGetZ);

						// -----------------------------------
						// Creation du JPanel pour l'exemple
						// -----------------------------------
						JPanel panneauEx = new JPanel();
						panneauEx.setLayout(new FlowLayout(FlowLayout.CENTER));
						label = new JLabel(getProperty("popupNewStrategie.exemple"));
						label.setPreferredSize(new Dimension(250, 50));
						panneauEx.add(label);

						// On termine par ajouter le panneau au panneau globale
						panneauGlobal.add(panneauEx);

						// -----------------------------------
						// Creation du JPanel pour Valider
						// -----------------------------------
						JPanel panneauValider = new JPanel();
						panneauValider.setLayout(new FlowLayout(FlowLayout.CENTER));
						JButton valider = new JButton(getProperty("popupNewStrategie.validate"));
						panneauValider.add(valider);

						// On termine par ajouter le panneau au panneau globale
						panneauGlobal.add(panneauValider);

						/*
						 * Evenement du bouton valider
						 */
						valider.addActionListener(ev -> {
							// Lorsque l'utilisateur clique sur le bouton valider
							// On doit verifier si ce qu'il a rentre est correct

							if (tfName.getText().isEmpty() || tfNaissance.getText().isEmpty()
									|| tfSurvie.getText().isEmpty())
								return;
							for (String s : tfNaissance.getText().split(",")) {
								if (!s.matches("[+-]?\\d*(\\.\\d+)?"))
									return;
							}

							for (String s : tfSurvie.getText().split(",")) {
								if (!s.matches("[+-]?\\d*(\\.\\d+)?"))
									return;
							}

							// Recuperation du nom de la nouvelle strategie (nouveau visiteur)
							String name = tfName.getText();
							// Recuperation du string contenant les differents parametres de naissance
							String naissance = tfNaissance.getText();
							// Recuperation du string contenant les differents parametres de survie
							String survie = tfSurvie.getText();
							// On ferme la popup
							popup.dispose();

							int[] valeursNaissance = new int[9];
							int[] valeursSurvie = new int[9];

							// On recupere les entiers qui correspondent au nombre de cellule requise pour
							// qu'une cellule naisse
							for (String s : naissance.split(",")) {
								if (Integer.parseInt(s) >= 0 && Integer.parseInt(s) < 9) {
									valeursNaissance[Integer.parseInt(s)] = 1;
								}
							}

							// On recupere les entiers qui correspondent au nombre de cellule requise pour
							// qu'une cellule survive
							for (String s : survie.split(",")) {
								if (Integer.parseInt(s) >= 0 && Integer.parseInt(s) < 9) {
									valeursSurvie[Integer.parseInt(s)] = 1;
								}
							}

							// On va d'abord creer notre visiteur
							Visiteur nouveauVisiteur = new Visiteur(Fenetre.getInstance().getJeu()) {
								/**
								 * La methode qui masque les parametres de ce visiteur En l'occurence, elle ne
								 * fait rien
								 */
								@Override
								public void masquerParametres() {
								}

								/**
								 * Methode qui affiche les parametres de ce visiteur En l'occurence, elle
								 * affiche le nom de la strategie au niveau de la fenetre
								 */
								@Override
								public void afficherParametres() {
									if (Fenetre.getInstance() != null)
										Fenetre.getInstance()
												.setTitle(Fenetre.getProperties().getProperty("windowBaseName") + name);
								}

								/**
								 * Méthode qui permet au visiteur de visiter une cellule vivante
								 */
								@Override
								public void visiteCelluleVivante(Cellule c) {
									int nbVoisines = c.nombreVoisinesVivantes(this.jeu);
									if (valeursSurvie[nbVoisines] == 0) {
										jeu.ajouteCommande(new CommandeMeurt(c));
										return;
									}
								}

								/**
								 * Méthode qui permet au visiteur de visiter une cellule morte
								 */
								@Override
								public void visiteCelluleMorte(Cellule c) {
									int nbVoisines = c.nombreVoisinesVivantes(this.jeu);
									if (valeursNaissance[nbVoisines] == 1) {
										jeu.ajouteCommande(new CommandeVit(c));
										return;
									}
								}

							};
							// Pour creer une strategie il faut d'abord creer une action qui lui sera
							// attache
							AbstractAction action = creerAction(name, MNEMONIC_VIDE, name, () -> {
								Fenetre.getInstance().getJeu().setVisiteur(nouveauVisiteur);
								Fenetre.getInstance().setTitle(
										Fenetre.getProperties().getProperty("windowBaseName") + "(" + name + ")");
							});

							// Maintenant il faut selectionner cette strategie et l'ajouter dans la liste
							// des "selectionnables"
							Fenetre.getInstance().addStrategie(action);
							Fenetre.getInstance().getJeu().setVisiteur(nouveauVisiteur);

						});
					});
		}

		/*
		 * Chargement du menu des preceptes
		 */
		if (MNU_PRECEPTES == null) {
			MNU_PRECEPTES = new JMenu(getProperty("menuPreceptes"));
			// On parcours tous les preceptes et pour chacun d'entre eux, on creer une
			// action que l'on met dans le JMenu
			listePreceptes.forEach((name, coords) -> {
				MNU_PRECEPTES.add(creerAction(name, MNEMONIC_VIDE, name, () -> {
					Fenetre.getInstance().getJeu().placerPreceptes(Fenetre.getInstance().getJeuUI().getXClick(),
							Fenetre.getInstance().getJeuUI().getYClick(), coords);
				}));
			});
		}
	}
}