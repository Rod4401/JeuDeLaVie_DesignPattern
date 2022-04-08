package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 13:17:53 CET 2022
 *
 */
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import Action.ActionsMenu;
import JeuDeLaVie.JeuDeLaVie;
import JeuDeLaVie.JeuDeLaVieUI;
import Observateur.Observable;
import Observateur.Observateur;
import Visiteur.Visiteur;
import Visiteur.VisiteurClassique;
import Visiteur.VisiteurCrayon;
import Visiteur.VisiteurOutil;

/**
 * La classe fenetre represente la fenetre du jeu, elle contient les pricipaux
 * composants (widgets) qui permettent une utilisation complete du jeu.
 */
@SuppressWarnings("serial")
public class Fenetre extends JFrame implements Observateur {

	/**
	 * Les constantes pour le selecteur de temps d'execution minimal d'une
	 * generation
	 */
	final private static int SPEED_MIN = 0;
	final private static int SPEED_MAX = 1000;
	final private static int SPEED_DEFAULT = 500;

	/**
	 * Les constantes pour le selecteur de taille de dessin
	 */
	final private static int DRAW_DEFAULT_SIZE = 1;
	final private static int DRAW_MIN_SIZE = 1;
	final private static int DRAW_MAX_SIZE = 100;
	final private static int DRAW_STEP = 1;

	/**
	 * Methode qui permet d'obtenir l'instance de la fenetre
	 */
	public static Fenetre getInstance() {
		return instance;
	}

	/**
	 * L'instance unique de la classe
	 */
	private static Fenetre instance;

	/**
	 * Le fichier de properties pour obtenir l'ensembles des strings
	 */
	private static Properties properties;

	/**
	 * Methode pour obtenir le gestionnaire du fichier properties
	 * 
	 * @return La properties
	 */
	public static Properties getProperties() {
		return properties;
	}

	/**
	 * Liste des strings pour les taux de generation de cellule lors du remplissage
	 * aleatoire Liste des valeurs correspondantes
	 */
	static private String[] listeTaux = new String[] { "Peu (0.1)", "Moyen (0.5)", "Beaucoup (0.7)", "Remplir (1)" };
	static private double[] listeTauxVals = new double[] { 0.1, 0.5, 0.7, 1 };

	/*
	 * Les composants relatif a la fenetre
	 */
	/**
	 * Le bouton play
	 */
	private JButton btnPlay;

	/**
	 * Le bouton etape suivante
	 */
	private JButton btnNextStep;

	/**
	 * Le bouton generer
	 */
	private JButton btnRand;

	/**
	 * Le bouton reinitialiser
	 */
	private JButton btnReset;

	/**
	 * Le bouton crayon (pour dessiner)
	 */
	private JButton btnDessin;

	/**
	 * Le bouton gomme (pour gommer)
	 */
	private JButton btnGomme;

	/**
	 * Le spinner pour selectionner la taille de la gomme/crayon(dessin)
	 */
	private JSpinner selectSize;

	/**
	 * La combobox pour selectionner le taux de generation
	 */
	private JComboBox<String> boxRandTaux;

	/**
	 * Le label pour indiquer le nombre de generations
	 */
	private JLabel lblNbGenerations;

	/**
	 * Le label pour indiquer le nombre de cellules vivantes
	 */
	private JLabel lblNbCellulesVivante;

	/**
	 * Le panel de la grille du jeu (le JeuDeLaVieUI)
	 */
	private JPanel grilleJeu;

	/**
	 * La toolbar avec les differents outils de parametrage
	 */
	private JToolBar toolBar;

	/**
	 * Le slider pour le temps de chaque generations
	 */
	private JSlider sliderTemps;

	/**
	 * Le menu qui contient la liste des strategie possibles
	 */
	private JMenu mnuStrat;

	/**
	 * L'action play / Pause qu'il faut changer quand on fait pause ou play On a
	 * besoin de la memoriser pour pouvoir intervertir play pause (le nom)
	 */
	private AbstractAction actPlayPause;

	/**
	 * La variable menu est une JMenuBar et permet d'ajouter un menu a notre
	 * application
	 */
	private JMenuBar menu = null;

	/**
	 * Variable d'etat qui definie si le jeu doit s'arreter
	 */
	private boolean continuer = false;

	/**
	 * Le thread d'execution
	 */
	private Thread leThread;

	/**
	 * Le taux de remplisage de la grille lors d'une generation aleatoire
	 */
	private double taux = 0.2;

	/**
	 * Le jeu relie a cette fenetre
	 */
	private JeuDeLaVie jeu;

	/**
	 * L'option de remplissage pour le dessin C'est un visiteur que l'on peut
	 * choisir en selectionnant soit -la gomme (effacer un nombre de cellules) -le
	 * dessin (remplir un nombre de cellules)
	 */
	private VisiteurOutil optionDessin;

	/**
	 * Le constructeur de la classe
	 */
	public Fenetre() {
		// On appelle le constructeur de la superClass
		super();

		// Creation du jeu
		this.jeu = new JeuDeLaVie(500, 300);

		// On selectionne le crayon par default
		this.setOptionDessin(VisiteurCrayon.getInstance(this.jeu));
		// Creation de l'observateur (jpanel) du jeu
		// Cet observateur realise l'affichage du jeu dans un jpanel qu'on ajoute dans
		// notre fenetre
		this.grilleJeu = new JeuDeLaVieUI(this.jeu);
		// On ajoute dans la partie correspondante, le jpanel du jeu
		this.add(new JScrollPane(this.grilleJeu), BorderLayout.CENTER);
		// On ajoute la fenetre en tant qu'observateur dans la grille
		((Observable) this.grilleJeu).attacheObservateur((Observateur) this);

		// On set la size minimal de la fenetre
		this.setMinimumSize(new Dimension(1100, 700));

		// On rend la fenetre visible
		this.setVisible(true);

		/*
		 * Evenement sur la fenetre
		 */
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// On met a false la variable continuer
				// pour que le thread arrete son execution
				mettrePause();
				System.exit(0);
			}
		});

		/*
		 * Evenement clavier
		 */
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// Ici on regarde uniquement les ctrl+s et ctrl+z qui sont pour sauvegarder et
				// faire un retour arriere
				switch (e.getKeyCode()) {
				case KeyEvent.VK_Z:
					// Control + Z permet de faire un retour arriere
					if (e.isControlDown()) {
						mettrePause();
						jeu.reloadEtatGrille();
					}
					break;
				case KeyEvent.VK_S:
					// Constrol + S permet de faire une sauvegarde
					if (e.isControlDown()) {
						jeu.saveEtatGrille();
					}
					break;
				}
			}
		});

	}

	/**
	 * Methode qui initialise les composants
	 */
	@SuppressWarnings("removal")
	public void initComponents() {
		// On initialise les composants de la grilleJeu (JeuDeLaVieUI)
		((JeuDeLaVieUI) this.grilleJeu).initComponents();

		// Par default c'est la version dite classique du jeu de la vie qui est
		// selectionnee
		this.changerStrategie(VisiteurClassique.getInstance(this.getJeu()));

		// creation des differents composants
		// Description plus detaillee plus haut
		this.toolBar = new JToolBar();
		this.actPlayPause = ActionsMenu.ACT_PLAYPAUSE;
		this.btnNextStep = new JButton(ActionsMenu.ACT_NEXTSTEP);
		this.lblNbGenerations = new JLabel();
		this.lblNbCellulesVivante = new JLabel();
		this.btnPlay = new JButton(this.actPlayPause);
		this.btnReset = new JButton(ActionsMenu.ACT_RESET);
		this.btnRand = new JButton(ActionsMenu.ACT_RAND);
		this.boxRandTaux = new JComboBox<>();
		this.sliderTemps = new JSlider(JSlider.HORIZONTAL, SPEED_MIN, SPEED_MAX, SPEED_DEFAULT);
		this.btnDessin = new JButton(ActionsMenu.ACT_CRAYON);
		this.btnGomme = new JButton(ActionsMenu.ACT_GOMME);
		this.selectSize = new JSpinner(new SpinnerNumberModel(DRAW_DEFAULT_SIZE, // valeur initiale
				DRAW_MIN_SIZE, // valeur minimum
				DRAW_MAX_SIZE, // valeur maximum
				DRAW_STEP // pas
		));

		// Creation / ajout de la bare menu
		this.menu = this.createMenuBar();
		this.setJMenuBar(this.menu);

		// On ajoute les differentes entites dans le jeu
		this.jeu.setVisiteur(VisiteurClassique.getInstance(this.jeu));
		this.jeu.attacheObservateur((Observateur) this.grilleJeu);

		// ajout du label qui indique le nombre de generation et du label qui indique le
		// nombre de cellules vivante
		this.lblNbGenerations.setText(properties.getProperty("textNbGenerations") + "0");
		this.lblNbCellulesVivante.setText(properties.getProperty("textNbCellulesVivantes") + "0");

		// Combobox du taux de generation
		this.boxRandTaux.setModel(new javax.swing.DefaultComboBoxModel<>(listeTaux));

		// =====ToolBar=====
		JPanel panelBuff;
		this.toolBar.setLayout(new FlowLayout());
		this.toolBar.setBorder(BorderFactory.createLineBorder(Color.black));

		// Les outils
		panelBuff = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelBuff.add(this.btnDessin);
		panelBuff.add(this.btnGomme);
		panelBuff.add(this.selectSize);

		this.toolBar.add(panelBuff);

		this.toolBar.addSeparator();

		panelBuff = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// Bouton reset
		panelBuff.add(this.btnReset);
		// Bouton rand
		panelBuff.add(this.btnRand);
		// Ajout de la combobox du taux de generation
		panelBuff.add(this.boxRandTaux);

		this.toolBar.add(panelBuff);

		this.toolBar.addSeparator();

		panelBuff = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// Ajout du bouton Play
		panelBuff.add(this.btnPlay);
		// Ajout du bouton next
		panelBuff.add(this.btnNextStep);
		// Ajout du slider de la vitesse
		this.sliderTemps.setPaintTicks(true);
		this.sliderTemps.setPaintLabels(true);

		JPanel panelBuff2 = new JPanel(new BorderLayout());

		// Creation d'une hachTable qui permet de faire un "multi" label sous le slider
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(SPEED_MIN), new JLabel(properties.getProperty("SliderSpeed.min")));
		labelTable.put(new Integer(SPEED_MAX / 2), new JLabel(properties.getProperty("SliderSpeed.middle")));
		labelTable.put(new Integer(SPEED_MAX), new JLabel(properties.getProperty("SliderSpeed.max")));
		this.sliderTemps.setLabelTable(labelTable);
		panelBuff2.add(this.sliderTemps, BorderLayout.CENTER);
		panelBuff.add(panelBuff2);

		this.toolBar.add(panelBuff);

		this.toolBar.addSeparator();

		// Les informations sur les cellules (nb vivantes, nb generations )
		panelBuff = new JPanel();
		panelBuff.setLayout(new BoxLayout(panelBuff, BoxLayout.PAGE_AXIS));
		panelBuff.setPreferredSize(new Dimension(150, 40));
		panelBuff.setBorder(BorderFactory.createLineBorder(Color.black));
		this.lblNbGenerations.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelBuff.add(this.lblNbGenerations);
		this.lblNbCellulesVivante.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelBuff.add(this.lblNbCellulesVivante);
		this.toolBar.add(panelBuff);

		// Ajout de la toolBar
		this.add(this.toolBar, BorderLayout.NORTH);

		// Creation de label vide pour empecher de mettre la toolbar sur les cotes
		this.add(new JLabel(), BorderLayout.WEST);
		this.add(new JLabel(), BorderLayout.EAST);

		// On affiche les commandes
		this.afficherCommandes();

		// On pack le tout
		this.pack();

		/*
		 * Les evenements
		 */

		/**
		 * Evenement relatif au changement d'item dans le combo box des remplissages
		 */
		this.boxRandTaux.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				for (int i = 0; i < listeTaux.length; i++) {
					if (e.getItem().toString().equals(listeTaux[i])) {
						taux = listeTauxVals[i];
						return;
					}
				}
			}
		});

	}

	/**
	 * Methode qui permet d'obtenir le taux de generation aleatoire actuellement
	 * selectionne
	 * 
	 * @return Le taux de generation (densite)
	 */
	public double getTaux() {
		return this.taux;
	}

	/**
	 * Methode qui met en pause le jeu
	 */
	public void mettrePause() {
		this.continuer = false;
		this.actualiserActionPlayPause();
	}

	/**
	 * Methode qui reprend le jeu apres une pause
	 */
	public void mettrePlay() {
		this.continuer = true;
		lancerJeu();
		this.actualiserActionPlayPause();
	}

	/**
	 * Methode qui permet de savoir si le jeu est en cours d'execution Autrement
	 * dit, si le jeu n'est pas en pause
	 * 
	 * @return true si le jeu est en pause, false sinon
	 */
	public boolean isPause() {
		return !this.continuer;
	}

	/**
	 * Methode qui met a jour les composants crees a partir de l'action play pause
	 */
	private void actualiserActionPlayPause() {
		// Si c'est pause on change le nom du bouton en la valeur de "play"
		if (this.isPause()) {
			actPlayPause.putValue(Action.NAME, properties.getProperty("play"));
		} else {
			// Sinon on change le nom du bouton en la valeur de "pause"
			actPlayPause.putValue(Action.NAME, properties.getProperty("pause"));
		}
	}

	/**
	 * Methode qui creer un thread d'excution
	 */
	private void lancerJeu() {
		/*
		 * Il semble inutile de refaire un thread a chaque fois Ici l'interet est
		 * surtout si l'utilisateur met un delai (entre 2 generations) de disons 1s
		 * alors, ce thread sera en pause pendant 1 seconde, si il repasse a 10ms Il
		 * devra attendre que cette pause se termine
		 */
		this.leThread = new Thread(() -> {
			while (continuer) {
				try {
					Thread.sleep(SPEED_MAX - sliderTemps.getValue());
				} catch (InterruptedException e) {
					// On ne fait rien
				}
				// Si pendant la pause, l'utilisateur a mis pause
				// On n'execute pas la suite
				if (!continuer) {
					break;
				}
				// On realise l'etape suivante
				etapeSuivante();
			}
		});
		// On lance l'execution du thread
		this.leThread.start();
	}

	/**
	 * Methode qui fait jouer le jeu d'une etape On delegue le travail a la classe
	 * jeu
	 */
	public void etapeSuivante() {
		jeu.calculerGenerationSuivante();
	}

	/**
	 * La methode actualise, pour actualiser cet observateur
	 */
	@Override
	public void actualise() {
		if (this.lblNbGenerations != null)
			this.lblNbGenerations.setText(properties.getProperty("textNbGenerations") + this.jeu.getNbGenerations());
		if (this.lblNbCellulesVivante != null)
			this.lblNbCellulesVivante
					.setText(properties.getProperty("textNbCellulesVivantes") + this.jeu.getNbCellulesVivantes());
	}

	/**
	 * Methode qui creer la bare menu
	 */
	private JMenuBar createMenuBar() {
		// La barre de menu a proprement parler
		JMenuBar menuBar = new JMenuBar();

		// Definition du menu deroulant "File" et de son contenu
		// Ce menu correspond a la section de sauvegarde pour charger/sauvegarder le jeu
		JMenu mnuFile = new JMenu(properties.getProperty("nomMenuFile"));
		mnuFile.setMnemonic('F');

		// Ajoue d'un item Enregistrer qui permet d'enregistrer
		mnuFile.add(ActionsMenu.ACT_SAUVEGARDER);
		// Ajoue d'un item Charger qui permet de charger un jeu
		mnuFile.add(ActionsMenu.ACT_CHARGER);
		// Ajoue d'un item Capture qui permet de faire une capture du jeu en image
		mnuFile.add(ActionsMenu.ACT_TOIMAGE);

		menuBar.add(mnuFile);

		// Definition du menu deroulant "Options" et de son contenu
		// Ce menu correspond au changement des caracteristiques du jeu
		JMenu mnuOptions = new JMenu(properties.getProperty("mnuOptions"));
		mnuOptions.setMnemonic('O');

		// Ajoue d'un item Redimensionner qui permet de redimensionner le jeu
		JMenuItem mnuItemResize = new JMenuItem(properties.getProperty("mnuItemResize"));
		mnuItemResize.setMnemonic('R');
		/*
		 * Evenement lie a l'item Redimensionner
		 */
		mnuItemResize.addActionListener(e -> {
			// On force la pause
			mettrePause();
			// On creer la popup
			JDialog popup = new JDialog(this, properties.getProperty("nomPopupResize"));
			// On rend visible la popup
			popup.setVisible(true);
			// On centre la popup
			popup.setLocation((int) (getWidth() * 0.4), (int) (getHeight() * 0.4));
			// On defini sa taille
			popup.setMinimumSize(new Dimension(300, 200));
			// On rend la popup non redimentionnable
			popup.setResizable(false);
			// Creation du JPanel global
			JPanel panneauRedimentionner = new JPanel();
			panneauRedimentionner.setLayout(new BoxLayout(panneauRedimentionner, BoxLayout.PAGE_AXIS));
			// On ajoute le panneau global a la popup
			popup.setContentPane(panneauRedimentionner);

			JLabel label;
			// -----------------------------------
			// Creation du JPanel pour les Lignes
			// -----------------------------------
			JPanel panneauGetX = new JPanel();
			panneauGetX.setLayout(new FlowLayout(FlowLayout.LEFT));
			label = new JLabel(properties.getProperty("popupResizeLblLignes"));
			label.setPreferredSize(new Dimension(80, 30));
			panneauGetX.add(label);

			// Creation du textField
			JTextField nbLignes = new JTextField();
			nbLignes.setPreferredSize(new Dimension(150, 20));
			panneauGetX.add(nbLignes);

			// On termine par ajouter le panneau au panneau globale
			panneauRedimentionner.add(panneauGetX);

			// -----------------------------------
			// Creation du JPanel pour les Colonnes
			// -----------------------------------
			JPanel panneauGetY = new JPanel();
			panneauGetY.setLayout(new FlowLayout(FlowLayout.LEFT));
			label = new JLabel(properties.getProperty("popupResizeLblColonnes"));
			label.setPreferredSize(new Dimension(80, 30));
			panneauGetY.add(label);
			// Creation du textField
			JTextField nbColonnes = new JTextField();
			nbColonnes.setPreferredSize(new Dimension(150, 20));
			panneauGetY.add(nbColonnes);

			// On termine par ajouter le panneau au panneau globale
			panneauRedimentionner.add(panneauGetY);

			// -----------------------------------
			// Creation du JPanel pour Valider
			// -----------------------------------
			JPanel panneauValider = new JPanel();
			panneauValider.setLayout(new FlowLayout(FlowLayout.CENTER));
			JButton valider = new JButton(properties.getProperty("popupResizeBtnValidate"));
			panneauValider.add(valider);
			/*
			 * Evenement du bouton valider
			 */
			valider.addActionListener(ev -> {
				// On regarde si les chaines representent chacune un nombre et si oui, qu'ils ne
				// soient pas vide
				if (!nbColonnes.getText().matches("[+-]?\\d*(\\.\\d+)?")
						|| !nbLignes.getText().matches("[+-]?\\d*(\\.\\d+)?") || nbLignes.getText().isEmpty()
						|| nbColonnes.getText().isEmpty()) {
					// Si les nombres saisis ne sont pas correctes
					return;
				} else {
					if (Integer.parseInt(nbLignes.getText()) == 0 || Integer.parseInt(nbColonnes.getText()) == 0)
						return;
					jeu.changerTaille(Integer.parseInt(nbLignes.getText()), Integer.parseInt(nbColonnes.getText()));
					popup.dispose();
				}
			});

			// On termine par ajouter le panneau au panneau globale
			panneauRedimentionner.add(panneauValider);

		});
		mnuOptions.add(mnuItemResize);

		// Definition du menu deroulant "Strategie" et de son contenu
		// Ce menu correspond a la section de la strategie de jeu (classique, HighLife
		// ...)
		this.mnuStrat = new JMenu(properties.getProperty("mnuStrat"));
		this.mnuStrat.setMnemonic('S');

		// Ajoue de l'item qui permet de creer une strategie
		this.mnuStrat.add(ActionsMenu.ACT_CREATION_STRATEGIE);

		// Ajoue de l'item qui permet de selectionner la strategie classique
		this.mnuStrat.add(ActionsMenu.ACT_SRATEGIE_DEFAULT_CLASSIQUE);

		// Ajoue de l'item qui permet de selectionner la strategie highlife
		this.mnuStrat.add(ActionsMenu.ACT_SRATEGIE_DEFAULT_HIGHLIFE);

		// Ajoue de l'item qui permet de selectionner la strategie DayNight
		this.mnuStrat.add(ActionsMenu.ACT_SRATEGIE_DEFAULT_DAYNIGHT);

		// Ajoue de l'item qui permet de selectionner la strategie Foret en feu
		this.mnuStrat.add(ActionsMenu.ACT_SRATEGIE_DEFAULT_FORETENFEU);

		mnuOptions.add(this.mnuStrat);
		menuBar.add(mnuOptions);

		// Creation du menu Jeu (utile lorsque les commandes sont reduites)
		JMenu mnuJeu = new JMenu(properties.getProperty("mnuJeu"));
		mnuJeu.setMnemonic('J');
		// Creation du sous-menuTemps qui contient des actions relatives au temps
		JMenu mnuTemps = new JMenu(properties.getProperty("mnuTemps"));
		mnuTemps.setMnemonic('T');
		mnuTemps.add(this.actPlayPause);
		mnuTemps.add(ActionsMenu.ACT_NEXTSTEP);

		// Ajout du menu cellules
		JMenu mnuCellules = new JMenu(properties.getProperty("mnuCellules"));
		mnuCellules.setMnemonic('C');
		mnuCellules.add(ActionsMenu.ACT_RESET);
		mnuCellules.add(ActionsMenu.ACT_RAND);

		mnuJeu.add(mnuTemps);
		mnuJeu.add(mnuCellules);

		// On termine par ajouter le menu jeu a la toolbar
		menuBar.add(mnuJeu);

		// Creation du menu fenetre
		JMenu mnuFenetre = new JMenu(properties.getProperty("mnuFenetre"));
		mnuFenetre.setMnemonic('E');
		mnuFenetre.add(ActionsMenu.ACT_TOOLBAR_AFFICHER);
		mnuFenetre.add(ActionsMenu.ACT_TOOLBAR_MASQUER);
		mnuFenetre.add(ActionsMenu.ACT_QUADRILLAGE);
		menuBar.add(mnuFenetre);

		// On renvoie la barre
		return menuBar;
	}

	/**
	 * Methode qui permet de changer de strategie
	 * 
	 * @param visiteur La nouvelle strategie
	 */
	private void changerStrategie(Visiteur visiteur) {
		this.jeu.setVisiteur(visiteur);
	}

	/**
	 * Methode qui permet de masquer le menu des commandes
	 */
	public void masquerCommandes() {
		this.toolBar.setVisible(false);
	}

	/**
	 * Methode qui permet d'afficher le menu des commandes
	 */
	public void afficherCommandes() {
		this.toolBar.setVisible(true);
	}

	/**
	 * Methode qui permet d'obtenir le jeu relie a la fenetre
	 * 
	 * @return Le jeu de la vie
	 */
	public JeuDeLaVie getJeu() {
		return this.jeu;
	}

	/**
	 * Methode qui permet d'obtenir la grille de jeu (le panel -> JeuDeLaVieUI)
	 * 
	 * @return Le JeuDeLaVieUI
	 */
	public JeuDeLaVieUI getJeuUI() {
		return (JeuDeLaVieUI) this.grilleJeu;
	}

	/**
	 * Methode qui permet de recuperer la taille du dessin
	 * 
	 * @return La taille du dessin
	 */
	public int getTailleDessin() {
		return (Integer) this.selectSize.getValue();
	}

	/**
	 * Methode qui permet de changer l'option de dessin
	 * 
	 * @param option La nouvelle option de dessin
	 */
	public void setOptionDessin(VisiteurOutil option) {
		this.optionDessin = option;
	}

	/**
	 * Methode qui permet de recuperer l'option de dessin
	 * 
	 * @return L'outil de dessin
	 */
	public VisiteurOutil getOptionDessin() {
		return this.optionDessin;
	}

	/**
	 * Methode qui permet d'ajouter une nouvelle strategie
	 * 
	 * @param a La strategie (action du bouton) a ajouter
	 */
	public void addStrategie(AbstractAction a) {
		this.mnuStrat.add(a);
	}

	/**
	 * La methode main
	 */
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Si il y a une erreur, peut importe, on ne fait rien
		}
		// Creation de la fenetre
		instance = new Fenetre();
		// On charge les actions
		ActionsMenu.chargerActions();
		// On initialise les composants
		instance.initComponents();
	}

	/**
	 * Ce bloc Static permet de creer la variable de classe properties Cette
	 * variable permet d'acceder aux informations qui sont dans le fichier
	 * Donnees.xml qui contient toutes les chaines de caracteres du fichier. Cela
	 * permet de les regrouper pour pouvoir les modifier plus facilement
	 */
	static {
		try {
			InputStream inputStream = new FileInputStream("src/Donnees.xml");
			properties = new Properties();
			properties.loadFromXML(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}