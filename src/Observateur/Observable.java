package Observateur;

/**
 * @author Meunier
 * @version 0.1 : Date : Tue Mar 22 10:21:25 CET 2022
 *
 */

/**
 * L'interface Observable permet de definir une classe comme observable C'est le
 * pattern observateur Cela indique que la classe est observable par des
 * "observateurs"
 */
public interface Observable {
	/**
	 * Le prototype de la methode qui permet d'ajouter un observateur
	 * 
	 * @param o L'observateur a attacher
	 */
	public void attacheObservateur(Observateur o);

	/**
	 * Le prototype de la methode qui permet de retirer un observateur
	 * 
	 * @param o L'observateur a retirer
	 */
	public void detacheObservateur(Observateur o);

	/**
	 * Le prototype de la methode qui permet d'indiquer a tous les observateurs un
	 * changement
	 */
	public void notifieObservateurs();
}