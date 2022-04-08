package Action;

/**
* @author Meunier
* @version 0.1 : Date : Thu Mar 31 09:26:06 CEST 2022
*
*/
/**
 * L'interface ActionExecute implemente une seule methode qui permet de
 * simplifier l'ecriture d'une action (cf ActionsMenu) Son unique methode se
 * nomme "executer", elle est invoquee lorsqu'on enclenche l'action
 */
public interface ActionExecute {
	/**
	 * Le prototype de la methode executer
	 */
	public void executer();
}