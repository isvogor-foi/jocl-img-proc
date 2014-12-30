package com.tiwo.main;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.tiwo.forms.MainForm;

public class Main {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		if(System.getProperty("os.name").contains("Windows")){
			setLookAndFeel("Windows");
		}
		else {
			setLookAndFeel("Nimbus");
		}
		
		/**
		 * Start main frame
		 */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void setLookAndFeel(String lookAndFeelName) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (lookAndFeelName.equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Look and feel not supported.");
		}
	}

}
