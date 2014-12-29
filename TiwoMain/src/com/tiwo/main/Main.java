package com.tiwo.main;

import java.awt.EventQueue;

import com.tiwo.forms.MainForm;

public class Main {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
				
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
		
		System.out.println("OS: " + System.getProperty("os.name"));

	}

}
