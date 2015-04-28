package edu.banda.coel.core.client.rbn;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import edu.banda.coel.core.client.gui.BNFrame;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class BNVisualClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			new RuntimeException("Look & feel problem occured!");
		} catch (InstantiationException e) {
			new RuntimeException("Look & feel problem occured!");
		} catch (IllegalAccessException e) {
			new RuntimeException("Look & feel problem occured!");
		} catch (UnsupportedLookAndFeelException e) {
			new RuntimeException("Look & feel problem occured!");
		}
		BNFrame frame = new BNFrame();
	}
}
